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

import de.shop.artikelverwaltung.rest.ArtikelResource;
import de.shop.bestellverwaltung.rest.BestellungenResource;
import de.shop.kundenverwaltung.rest.KundenResource;
import java.nio.file.Path;
import java.nio.file.Paths;

import static de.shop.util.Constants.REST_PATH;


/**
 * @author <a href="mailto:Juergen.Zimmermann@HS-Karlsruhe.de">J&uuml;rgen Zimmermann</a>
 */
public final class TestConstants {
    static final String WEB_PROJEKT = "shop";
    static final String TEST_WAR = WEB_PROJEKT + ".war";
    
    // https und TLS
    static final String HTTPS = "https";
    static final String HOST = "localhost";
    static final int PORT = 8443;
    static final String TLS12 = "TLSv1.2";
    static final String KEYSTORE_TYPE = "PKCS12";
    static final Path TRUSTSTORE_PATH = Paths.get("src", "test", "resources", "https", "truststore.p12");
    static final char[] TRUSTSTORE_PASSWORD = "zimmermann".toCharArray();
    
	// Basis-URI
	private static final String BASE_URI = HTTPS + "://" + HOST + ":" + PORT + "/" + WEB_PROJEKT + REST_PATH;
	
	// Pfade und Pfad-Parameter
	public static final String KUNDEN_URI = BASE_URI + "/kunden";
	public static final String KUNDEN_PRIVAT_URI = KUNDEN_URI + "/privat";
	public static final String KUNDEN_ID_URI = KUNDEN_URI + "/{" + KundenResource.ID_PATH_PARAM + "}";
	
	public static final String BESTELLUNGEN_URI = BASE_URI + "/bestellungen";
	public static final String BESTELLUNGEN_ID_URI = BESTELLUNGEN_URI + "/{" + BestellungenResource.ID_PATH_PARAM + "}";
	public static final String BESTELLUNGEN_ID_KUNDE_URI = BESTELLUNGEN_ID_URI + "/kunde";
	
	public static final String ARTIKEL_URI = BASE_URI + "/artikel";
	public static final String ARTIKEL_ID_URI = ARTIKEL_URI + "/{" + ArtikelResource.ID_PATH_PARAM + "}";
	
	public static final String NOT_EXISTING_URI = BASE_URI + "/NOT_EXISTING";

    // Logging
    public static final String BEGINN = "BEGINN";
    public static final String ENDE = "ENDE";
    
    private TestConstants() {
    }
}
