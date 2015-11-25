/*
 * Copyright (C) 2013 - 2015 Juergen Zimmermann, Hochschule Karlsruhe
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

package de.shop.kundenverwaltung.rest;

import de.shop.kundenverwaltung.domain.AbstractKunde;
import de.shop.kundenverwaltung.domain.Adresse;
import de.shop.kundenverwaltung.domain.Privatkunde;
import de.shop.kundenverwaltung.util.AdresseBuilder;
import de.shop.kundenverwaltung.util.PrivatkundeBuilder;
import de.shop.util.AbstractResourceTest;
import java.lang.invoke.MethodHandles;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.resteasy.api.validation.ResteasyConstraintViolation;
import org.jboss.resteasy.api.validation.ViolationReport;
import org.junit.Test;
import org.junit.runner.RunWith;

import static de.shop.kundenverwaltung.rest.KundenResource.ID_PATH_PARAM;
import static de.shop.kundenverwaltung.rest.KundenResource.NACHNAME_QUERY_PARAM;
import static de.shop.kundenverwaltung.util.KundeAssert.assertThatKunde;
import static de.shop.kundenverwaltung.util.KundenAssert.assertThatKunden;
import static de.shop.kundenverwaltung.util.ViolationAssert.assertThatViolations;
import static de.shop.util.Constants.FIRST_LINK;
import static de.shop.util.Constants.LAST_LINK;
import static de.shop.util.ResponseAssert.assertThatResponse;
import static de.shop.util.TestConstants.BEGINN;
import static de.shop.util.TestConstants.ENDE;
import static de.shop.util.TestConstants.KUNDEN_ID_URI;
import static de.shop.util.TestConstants.KUNDEN_URI;
import static java.util.Locale.ENGLISH;
import static java.util.Locale.GERMAN;
import static java.util.UUID.fromString;
import static javax.ws.rs.client.Entity.json;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;



/**
 * @author <a href="mailto:Juergen.Zimmermann@HS-Karlsruhe.de">J&uuml;rgen Zimmermann</a>
 */
@RunWith(Arquillian.class)
public class KundenResourceTest extends AbstractResourceTest {
    private static final Logger LOGGER = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());
    
    private static final UUID KUNDE_ID = fromString("00000001-0000-0000-0000-000000000001");;
    private static final UUID KUNDE_ID_NICHT_VORHANDEN = fromString("00000001-0000-0000-0000-FFFFFFFFFFFF");;
    private static final UUID KUNDE_ID_UPDATE = fromString("00000001-0000-0000-0000-000000000002");;
    private static final UUID KUNDE_ID_DELETE = fromString("00000001-0000-0000-0000-000000000005");;
    private static final String NACHNAME = "Alpha";
    private static final String NEUER_NACHNAME = "Nachnameneu";
    private static final String NEUER_NACHNAME_INVALID = "!";
    private static final String NEUE_EMAIL = "x." + NEUER_NACHNAME + "@test.de";
    private static final String NEUE_EMAIL_INVALID = "?";
    private static final int TAG = 1;
    private static final int MONAT = Calendar.FEBRUARY;
    private static final int JAHR = 2014;
    private static final Date NEU_SEIT = new GregorianCalendar(JAHR, MONAT, TAG).getTime();
    private static final String NEUE_PLZ = "76133";
    private static final String NEUE_PLZ_INVALID = "1234";
    private static final String NEUER_ORT = "Karlsruhe";


    @Test
    @InSequence(1)
    public void findById() {
        LOGGER.finer(BEGINN);
        
        // Given
        
        // When
        final Response response = getHttpsClient().target(KUNDEN_ID_URI)
                                                  .resolveTemplate(ID_PATH_PARAM, KUNDE_ID)
                                                  .request()
                                                  .accept(APPLICATION_JSON)
                                                  .get();
        
        // Then
        assertThatResponse(response)
            .hasStatusOk()
            .hasLinks()
            .hasSelfLinkMitId(KUNDE_ID);

        final AbstractKunde kunde = response.readEntity(AbstractKunde.class);
        assertThatKunde(kunde)
            .hasId(KUNDE_ID)
            .hasBestellungenUri()
            .hasBestellungenUriEndingWith("/bestellungen/kunde/" + KUNDE_ID);
        
        LOGGER.finer(ENDE);
    }
    
    @Test
    @InSequence(2)
    public void findByIdNichtVorhanden() {
        LOGGER.finer(BEGINN);
        
        // Given
        
        // When
        final Response response = getHttpsClient().target(KUNDEN_ID_URI)
                                                  .resolveTemplate(ID_PATH_PARAM, KUNDE_ID_NICHT_VORHANDEN)
                                                  .request()
                                                  .accept(APPLICATION_JSON)
                                                  .acceptLanguage(GERMAN)
                                                  .get();

        // Then
        assertThatResponse(response).hasStatusNotFound();
        response.close();
        
        LOGGER.finer(ENDE);
    }

    @Test
    @InSequence(10)
    public void findByNachname() {
        LOGGER.finer(BEGINN);
        
        // Given

        // When
        final Response response = getHttpsClient().target(KUNDEN_URI)
                                                  .queryParam(NACHNAME_QUERY_PARAM, NACHNAME)
                                                  .request()
                                                  .accept(APPLICATION_JSON)
                                                  .get();
        
        assertThatResponse(response)
            .hasStatusOk()
            .hasLinks()
            .hasLink(FIRST_LINK)
            .hasLink(LAST_LINK);
        
        final List<AbstractKunde> kunden = response.readEntity(new GenericType<List<AbstractKunde>>() { });
        assertThatKunden(kunden)
            .isNotEmpty()
            .doesNotContainNull()
            .hasSameNachname(NACHNAME);
        
        LOGGER.finer(ENDE);
    }
    
    @Test
    @InSequence(20)
    public void save() {
        LOGGER.finer(BEGINN);
        
        // Given
        
        // When
        final Adresse adresse = new AdresseBuilder()
                                .plz(NEUE_PLZ)
                                .ort(NEUER_ORT)
                                .build();
        final Privatkunde kunde = new PrivatkundeBuilder()
                                  .nachname(NEUER_NACHNAME)
                                  .email(NEUE_EMAIL)
                                  .adresse(adresse)
                                  .build();
        
        final Response response = getHttpsClient().target(KUNDEN_URI)
                                                  .request()
                                                  .post(json(kunde));
        
        // Then
        assertThatResponse(response)
            .hasStatusCreated()
            .hasId();

        LOGGER.finer(ENDE);
    }
    
    
    @Test
    @InSequence(22)
    public void saveInvalid() {
        LOGGER.finer(BEGINN);
        
        // Given
        final Adresse adresse = new AdresseBuilder()
                                .plz(NEUE_PLZ_INVALID)
                                .ort(NEUER_ORT)
                                .build();
        final Privatkunde kunde = new PrivatkundeBuilder()
                                  .nachname(NEUER_NACHNAME_INVALID)
                                  .email(NEUE_EMAIL_INVALID)
                                  .seit(NEU_SEIT)
                                  .adresse(adresse)
                                  .build();
        
        // When
        final Response response = getHttpsClient().target(KUNDEN_URI)
                                                  .request()
                                                  .accept(APPLICATION_JSON)
                                                  .acceptLanguage(ENGLISH)
                                                  .post(json(kunde));

        // Then
        assertThatResponse(response)
                .hasStatusBadRequest()
                .hasValidationException();
        
        final Collection<ResteasyConstraintViolation> violations = response.readEntity(ViolationReport.class)
                                                                           .getParameterViolations();
        assertThatViolations(violations)
            .haveAtLeastOneViolation()
            .haveNachnameSizeViolation(NEUER_NACHNAME_INVALID)
            .haveNachnamePatternViolation(NEUER_NACHNAME_INVALID)
            .haveEmailViolation(NEUE_EMAIL_INVALID)
            .havePlzViolation(NEUE_PLZ_INVALID);
        
        LOGGER.finer(ENDE);
    }

    @Test
    @InSequence(30)
    public void update() {
        LOGGER.finer(BEGINN);
        
        // Given
        Response response = getHttpsClient().target(KUNDEN_ID_URI)
                                            .resolveTemplate(ID_PATH_PARAM, KUNDE_ID_UPDATE)
                                            .request()
                                            .accept(APPLICATION_JSON)
                                            .get();
        
        final AbstractKunde kunde = response.readEntity(AbstractKunde.class);
        assertThatKunde(kunde).hasId(KUNDE_ID_UPDATE);
        
        // Aus den gelesenen JSON-Werten ein neues JSON-Objekt mit neuem Nachnamen bauen
        kunde.setNachname(NEUER_NACHNAME);
        kunde.setEmail(NEUE_EMAIL);
        
        // When
        response = getHttpsClient().target(KUNDEN_URI)
                                   .request()
                                   .put(json(kunde));
        
        // Then
        assertThatResponse(response).hasStatusNoContent();
        response.close();

        LOGGER.finer(ENDE);
    }
    
    @Test
    @InSequence(40)
    public void delete() {
        LOGGER.finer(BEGINN);
        
        // Given
        Response response = getHttpsClient().target(KUNDEN_ID_URI)
                                            .resolveTemplate(ID_PATH_PARAM, KUNDE_ID_DELETE)
                                            .request()
                                            .accept(APPLICATION_JSON)
                                            .get();
        assertThatResponse(response).hasStatusOk();
        response.close();
        
        // When
        response = getHttpsClient().target(KUNDEN_ID_URI)
                                   .resolveTemplate(ID_PATH_PARAM, KUNDE_ID_DELETE)
                                   .request()
                                   .delete();
            
        // Then
        assertThatResponse(response).hasStatusNoContent();
        response.close();

        LOGGER.finer(ENDE);
    }
}
