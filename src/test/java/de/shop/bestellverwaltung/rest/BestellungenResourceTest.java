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

package de.shop.bestellverwaltung.rest;

import de.shop.bestellverwaltung.domain.Bestellung;
import de.shop.util.AbstractResourceTest;
import java.lang.invoke.MethodHandles;
import java.util.UUID;
import java.util.logging.Logger;
import javax.ws.rs.core.Response;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.junit.Test;
import org.junit.runner.RunWith;

import static de.shop.bestellverwaltung.util.BestellungAssert.assertThatBestellung;
import static de.shop.util.ResponseAssert.assertThatResponse;
import static de.shop.util.TestConstants.BEGINN;
import static de.shop.util.TestConstants.BESTELLUNGEN_ID_URI;
import static de.shop.util.TestConstants.ENDE;
import static java.util.UUID.fromString;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;



/**
 * @author <a href="mailto:Juergen.Zimmermann@HS-Karlsruhe.de">J&uuml;rgen Zimmermann</a>
 */
@RunWith(Arquillian.class)
public class BestellungenResourceTest extends AbstractResourceTest {
    private static final Logger LOGGER = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());
    
    private static final UUID BESTELLUNG_ID = fromString("00000004-0000-0000-0000-000000000001");
    
    @Test
    @InSequence(1)
    public void findById() {
        LOGGER.finer(BEGINN);
        
        // Given
        
        // When
        Response response = getHttpsClient().target(BESTELLUNGEN_ID_URI)
                                            .resolveTemplate(BestellungenResource.BESTELLUNGEN_ID_PATH_PARAM, BESTELLUNG_ID)
                                            .request()
                                            .accept(APPLICATION_JSON)
                                            .get();
            
        // Then
        assertThatResponse(response).hasStatusOk();
        final Bestellung bestellung = response.readEntity(Bestellung.class);
        
        assertThatBestellung(bestellung)
            .hasId(BESTELLUNG_ID)
            .hasKundeUri(); 

        LOGGER.finer(ENDE);
    }
}
