/*
 * Copyright (C) 2015 Juergen Zimmermann, Hochschule Karlsruhe
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

import java.util.UUID;
import javax.ws.rs.core.Response;
import org.assertj.core.api.AbstractAssert;

import static de.shop.util.Constants.SELF_LINK;
import static de.shop.util.LocationHelper.extractId;
import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_CREATED;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;
import static java.net.HttpURLConnection.HTTP_NO_CONTENT;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="mailto:Juergen.Zimmermann@HS-Karlsruhe.de">J&uuml;rgen Zimmermann</a>
 */
public class ResponseAssert extends AbstractAssert<ResponseAssert, Response> {
    protected ResponseAssert(Response actual) {
        super(actual, ResponseAssert.class);
    }
    
    public static ResponseAssert assertThatResponse(Response actual) {
        return new ResponseAssert(actual);
    }
    
    public ResponseAssert hasStatusOk() {
        return hasStatus(HTTP_OK);
    }
    
    public ResponseAssert hasStatusCreated() {
        return hasStatus(HTTP_CREATED);
    }

    public ResponseAssert hasStatusNoContent() {
        return hasStatus(HTTP_NO_CONTENT);
    }

    public ResponseAssert hasStatusBadRequest() {
        return hasStatus(HTTP_BAD_REQUEST);
    }

    public ResponseAssert hasStatusNotFound() {
        return hasStatus(HTTP_NOT_FOUND);
    }
    
    private ResponseAssert hasStatus(int status) {
        assertThat(actual.getStatus())
            .overridingErrorMessage("Der Status des Response muss %d sein, ist aber %d", status, actual.getStatus())
            .isEqualTo(status);
        return this;
    }
    

    public ResponseAssert hasLinks() {
        assertThat(actual.getLinks())
            .overridingErrorMessage("Es gibt keinen Link-Header")
            .isNotEmpty();
        return this;
    }
    
    public ResponseAssert hasLink(String link) {
        assertThat(actual.getLink(link))
            .overridingErrorMessage("Es gibt keinen Link-Header mit dem Namen %s", link)
            .isNotNull();
        return this;
    }
    
    public ResponseAssert hasSelfLinkMitId(UUID id) {
        assertThat(actual.getLink(SELF_LINK).getUri().toString())
            .overridingErrorMessage("Es gibt keinen Link-Header self, der die ID %s enthaelt", id)
            .contains(id.toString());
        return this;
    }
    
    public ResponseAssert hasId() {
        final UUID actualId = extractId(actual);
        assertThat(actualId)
            .overridingErrorMessage("Im Location-Header muss die ID vorhanden sein", actualId)
            .isNotNull();
        return this;
    }
    
    public ResponseAssert hasValidationException() {
        final String headerKey = "validation-exception";
        final String headerValue = actual.getHeaderString(headerKey).toLowerCase();
        assertThat(headerValue)
            .overridingErrorMessage("Im Header muss der Eintrag %s den Wert true haben, hat aber den Wert %s", headerKey, headerValue)
            .isEqualTo("true");
        return this;
    }
}
