/*
 * Copyright (C) 2013 Juergen Zimmermann, Hochschule Karlsruhe
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.shop.util;

import java.io.File;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ExplodedImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.ConfigurableMavenResolverSystem;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;

import static de.shop.util.TestConstants.TEST_WAR;

/**
 * @author <a href="mailto:Juergen.Zimmermann@HS-Karlsruhe.de">J&uuml;rgen Zimmermann</a>
 */
public enum ArchiveBuilder {
	INSTANCE;

	// http://exitcondition.alrubinger.com/2012/09/13/shrinkwrap-resolver-new-api
    private final ConfigurableMavenResolverSystem mavenResolver;
    private final String picketlinkVersion;
    private final String deltaspikeVersion;
    private final String primefacesVersion;
    private final String primefacesAllThemesVersion;
    private final String atmosphereVersion;
    private final String commonsFileuploadVersion;
    private final String assertjVersion;
    private final String mockitoVersion;
    
	private final WebArchive archive = ShrinkWrap.create(WebArchive.class, TEST_WAR);
    private final WebArchive baseTestArchive = ShrinkWrap.create(WebArchive.class, TEST_WAR);
    private File[] assertjJars;
    private File[] mockitoJars;
    
    // Funktioniert nicht wegen der Metamodell-Klassen von JPA fuer Criteria-Queries:
    // org.jboss.shrinkwrap.resolver.impl.maven.archive.packaging.AbstractCompilingProcessor.compile()
    //
    //private final WebArchive archive = ShrinkWrap.create(MavenImporter.class, TEST_WAR)
    //                                             .loadPomFromFile("pom.xml")
    //                                             .importBuildOutput()
    //                                             .as(WebArchive.class);
    //private final WebArchive archive = ShrinkWrap.create(MavenImporter.class, TEST_WAR)
    //                                             .loadPomFromFile("pom.xml")
    //                                             .importBuildOutput()
    //                                             .importTestBuildOutput()
    //                                             .as(WebArchive.class);

	private ArchiveBuilder() {
        mavenResolver = "TRUE".equals(System.getProperty("mavenOffline").toUpperCase())
                        ? Maven.configureResolver().workOffline()
                        : Maven.configureResolver();

        // siehe build.gradle
        picketlinkVersion = System.getProperty("picketlinkVersion");
        deltaspikeVersion = System.getProperty("deltaspikeVersion");
        primefacesVersion = System.getProperty("primefacesVersion");
        primefacesAllThemesVersion = System.getProperty("primefacesAllThemesVersion");
        atmosphereVersion = System.getProperty("atmosphereVersion");
        commonsFileuploadVersion = System.getProperty("commonsFileuploadVersion");
        assertjVersion = System.getProperty("assertjVersion");
        mockitoVersion = System.getProperty("mockitoVersion");

		addWebInfWebseiten();
		addKlassenRessourcen();
		addJars();
        baseTestArchive.merge(archive);
	}
	
	private void addWebInfWebseiten() {
        // https://community.jboss.org/wiki/HowDoIAddAllWebResourcesToTheShrinkWrapArchive
        // https://issues.jboss.org/browse/SHRINKWRAP-247
        
		// XML-Konfigurationsdateien und Webseiten als JAR einlesen
		final GenericArchive tmp = ShrinkWrap.create(GenericArchive.class)
		                                     .as(ExplodedImporter.class)
		                                     .importDirectory("src/main/webapp")
		                                     .as(GenericArchive.class);
		archive.merge(tmp, "/");
	}
	
	private void addKlassenRessourcen() {
		GenericArchive tmp = ShrinkWrap.create(GenericArchive.class)
		                               .as(ExplodedImporter.class)
			                           .importDirectory("build/classes/main")
			                           .as(GenericArchive.class);
		archive.merge(tmp, "WEB-INF/classes");
        tmp = ShrinkWrap.create(GenericArchive.class)
		                .as(ExplodedImporter.class)
			            .importDirectory("build/resources/main")
			            .as(GenericArchive.class);
        archive.merge(tmp, "WEB-INF/classes");
	}
	
	private void addJars() {
        archive.addAsLibraries(mavenResolver.resolve("org.picketlink:picketlink-deltaspike:" + picketlinkVersion,
                                                     "org.apache.deltaspike.modules:deltaspike-security-module-api:" + deltaspikeVersion,
                                                     "org.apache.deltaspike.modules:deltaspike-security-module-impl:" + deltaspikeVersion,
                                                     "org.apache.deltaspike.core:deltaspike-core-api:" + deltaspikeVersion,
                                                     "org.apache.deltaspike.core:deltaspike-core-impl:" + deltaspikeVersion,
                                                     "org.primefaces:primefaces:" + primefacesVersion,
                                                     "org.primefaces.themes:all-themes:" + primefacesAllThemesVersion,
                                                     "org.atmosphere:atmosphere-runtime:" + atmosphereVersion,
                                                     "commons-fileupload:commons-fileupload:" + commonsFileuploadVersion)
                                            .withoutTransitivity()
				                            .asFile());
	}
    
	public static ArchiveBuilder getInstance() {
		return INSTANCE;
	}
	
	public Archive<?> getArchive(Class<?>... testklassen) {
        if (testklassen.length == 0) {
            return archive;
        }
        
        if (assertjJars == null) {
            assertjJars = mavenResolver.resolve("org.assertj:assertj-core:" + assertjVersion)
                                       .withoutTransitivity()
                                       .asFile();
        }
        
        if (mockitoJars == null) {
            mockitoJars = mavenResolver.resolve("org.mockito:mockito-core:" + mockitoVersion)
                                       .withTransitivity()
                                       .asFile();
        }
        
        final WebArchive testArchive = ShrinkWrap.create(WebArchive.class, TEST_WAR);
        testArchive.merge(baseTestArchive);
        testArchive.addClasses(testklassen);
        testArchive.addAsLibraries(assertjJars);
        testArchive.addAsLibraries(mockitoJars);
        return testArchive;
	}
}
