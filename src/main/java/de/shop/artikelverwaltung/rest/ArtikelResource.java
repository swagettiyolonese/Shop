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

package de.shop.artikelverwaltung.rest;

import de.shop.artikelverwaltung.business.ArtikelBroker;
import de.shop.artikelverwaltung.domain.Artikel;
import de.shop.util.ShopRuntimeException;
import de.shop.util.interceptor.Log;
import de.shop.util.rest.UriHelper;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.Optional;
import java.util.UUID;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import static de.shop.util.Constants.SELF_LINK;
import static de.shop.util.Constants.UUID_PATTERN;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static javax.ws.rs.core.MediaType.TEXT_XML;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;

/**
 * @author <a href="mailto:Juergen.Zimmermann@HS-Karlsruhe.de">J&uuml;rgen Zimmermann</a>
 */
@Path("/artikel")
@Produces({ APPLICATION_JSON, APPLICATION_XML + ";qs=0.75", TEXT_XML + ";qs=0.5" })
@Consumes({APPLICATION_JSON, APPLICATION_XML, TEXT_XML })
@RequestScoped
@Log
public class ArtikelResource {
    public static final Method FIND_BY_ID;
    
    public static final String ID_PATH_PARAM = "id";

    private ArtikelBroker artikelBroker;
    private UriHelper uriHelper;

    static {
        try {
            FIND_BY_ID = ArtikelResource.class.getMethod("findById", UUID.class, UriInfo.class);
        } catch (NoSuchMethodException | SecurityException e) {
            throw new ShopRuntimeException(e);
        }
    }

    /**
     * Public Default-Konstruktor f&uuml;r JAX-RS
     */
    public ArtikelResource() {
        super();
    }

    /**
     * Package-private Konstruktor mit "Constructor Injection" f&uuml;r CDI
     * @param artikelBroker zu injizierendes Objekt f&uuml;r ArtikelBroker
     * @param uriHelper zu injizierendes Objekt f&uuml;r UriHelper
     */
    @Inject
    ArtikelResource(ArtikelBroker artikelBroker, UriHelper uriHelper) {
        super();
        this.artikelBroker = artikelBroker;
        this.uriHelper = uriHelper;
    }
    
    @GET
    @Path("{" + ID_PATH_PARAM + ":" + UUID_PATTERN + "}")
    public Response findById(@PathParam(ID_PATH_PARAM) UUID id, @Context UriInfo uriInfo) {
        final Optional<Artikel> artikelOpt = artikelBroker.findById(id);
        if (!artikelOpt.isPresent()) {
            return Response.status(NOT_FOUND).build();
        }
        
        final Artikel artikel = artikelOpt.get();
        return Response.ok(artikel)
                       .links(getTransitionalLinks(artikel, uriInfo))
                       .build();
    }
    
    private Link[] getTransitionalLinks(Artikel artikel, UriInfo uriInfo) {
        final Link self = Link.fromUri(getUriArtikel(artikel, uriInfo))
                              .rel(SELF_LINK)
                              .build();

        return new Link[] { self };
    }
    
    private URI getUriArtikel(Artikel artikel, UriInfo uriInfo) {
        return uriHelper.getUri(ArtikelResource.class, FIND_BY_ID, artikel.getId(), uriInfo);
    }
}
