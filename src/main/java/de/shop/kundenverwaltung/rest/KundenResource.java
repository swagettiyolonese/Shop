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

package de.shop.kundenverwaltung.rest;

import de.shop.bestellverwaltung.business.BestellungenBroker;
import de.shop.bestellverwaltung.rest.BestellungenResource;
import de.shop.kundenverwaltung.business.KundenBroker;
import de.shop.kundenverwaltung.domain.AbstractKunde;
import de.shop.util.ShopRuntimeException;
import de.shop.util.interceptor.Log;
import de.shop.util.rest.UriHelper;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import static de.shop.util.Constants.ADD_LINK;
import static de.shop.util.Constants.EMAIL_PATTERN;
import static de.shop.util.Constants.FIRST_LINK;
import static de.shop.util.Constants.LAST_LINK;
import static de.shop.util.Constants.LIST_LINK;
import static de.shop.util.Constants.REMOVE_LINK;
import static de.shop.util.Constants.SELF_LINK;
import static de.shop.util.Constants.UPDATE_LINK;
import static de.shop.util.Constants.UUID_PATTERN;
import static java.util.Optional.empty;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import static javax.ws.rs.core.MediaType.TEXT_XML;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.NOT_IMPLEMENTED;

/**
 * @author <a href="mailto:Juergen.Zimmermann@HS-Karlsruhe.de">J&uuml;rgen Zimmermann</a>
 */
@Path("/kunden")
@Produces({ APPLICATION_JSON, APPLICATION_XML + ";qs=0.75", TEXT_XML + ";qs=0.5" })
@Consumes({ APPLICATION_JSON, APPLICATION_XML, TEXT_XML })
@RequestScoped
@Log
public class KundenResource {
    public static final String ID_PATH_PARAM = "kundeId";
    public static final String NACHNAME_QUERY_PARAM = "nachname";
    public static final String EMAIL_QUERY_PARAM = "email";
    public static final String PLZ_QUERY_PARAM = "plz";
    
    public static final Method FIND_BY_ID;
    public static final Method DELETE;

    private KundenBroker kundenBroker;
    private UriHelper uriHelper;
    private Instance<BestellungenBroker> bestellungenBrokerInstance;

    static {
        try {
            FIND_BY_ID = KundenResource.class.getMethod("findById", UUID.class, UriInfo.class);
            DELETE = KundenResource.class.getMethod("delete", UUID.class);
                    
        } catch (NoSuchMethodException | SecurityException e) {
            throw new ShopRuntimeException(e);
        }
    }

    /**
     * Public Default-Konstruktor f&uuml;r JAX-RS
     */
    public KundenResource() {
        super();
    }

    /**
     * Package-private Konstruktor mit "Constructor Injection" f&uuml;r CDI
     * @param kundenBroker zu injizierendes Objekt f&uuml;r KundenBroker
     * @param bestellungenBrokerInstance zu injizierendes Instance-Objekt f&uuml;r BestellungenBroker
     * @param uriHelperInstance zu injizierendes Instance-Objekt f&uuml;r UriHelper
     */
    @Inject
    KundenResource(KundenBroker kundenBroker,
                   UriHelper uriHelper,
                   Instance<BestellungenBroker> bestellungenBrokerInstance) {
        super();
        this.kundenBroker = kundenBroker;
        this.uriHelper = uriHelper;
        this.bestellungenBrokerInstance = bestellungenBrokerInstance;
    }
    
    @GET
    @Produces(APPLICATION_JSON)
    @Path("version")
    public String getVersion() {
        return "{version: \"1.0\"}";
    }
    
    @GET
    @Path("{" + ID_PATH_PARAM + ":" + UUID_PATTERN + "}")
    public Response findById(@PathParam(ID_PATH_PARAM) UUID id, @Context UriInfo uriInfo) {
        final Optional<AbstractKunde> kundeOpt = kundenBroker.findById(id);
        if (!kundeOpt.isPresent()) {
            return Response.status(NOT_FOUND).build();
        }
        
        final AbstractKunde kunde = kundeOpt.get();
        setStructuralLinks(kunde, uriInfo);
        return Response.ok(kunde)
                       .links(getTransitionalLinks(kunde, uriInfo))
                       .build();
    }
    
    @GET
    public Response find(@QueryParam(NACHNAME_QUERY_PARAM)
                         @Pattern(regexp = AbstractKunde.NACHNAME_PATTERN, message = "{kunde.nachname.pattern}")
                         String nachname,
                         @QueryParam(EMAIL_QUERY_PARAM)
                         @Pattern(regexp = EMAIL_PATTERN, message = "{kunde.email.pattern}")
                         String email,
                         @QueryParam(PLZ_QUERY_PARAM)
                         @Pattern(regexp = "\\d{5}", message = "{adresse.plz}")
                         String plz,
                         @Context UriInfo uriInfo) {
        Optional<List<AbstractKunde>> kundenOpt = empty();
        Optional<AbstractKunde> kundeOpt = empty();
        if (nachname != null) {
            kundenOpt = kundenBroker.findByNachname(nachname);
        } else if (email != null) {
            kundeOpt = kundenBroker.findByEmail(email);
        } else if (plz != null) {
            // TODO Beispiel fuer ein TODO bei fehlender Implementierung
            return Response.status(NOT_IMPLEMENTED)
                           .entity("Suche nach PLZ noch nicht implementiert")
                           .build();
        } else {
            kundenOpt = kundenBroker.findAll();
        }
        
        Object entity = null;
        Link[] links = null;
        if (kundenOpt.isPresent()) {
            final List<AbstractKunde> kunden = kundenOpt.get();
            kunden.forEach(k -> setStructuralLinks(k, uriInfo));
            entity = new GenericEntity<List<? extends AbstractKunde>>(kunden) {};   //NOSONAR
            links = getTransitionalLinksKunden(kunden, uriInfo);
        } else if (kundeOpt.isPresent()) {
            final AbstractKunde kunde = kundeOpt.get();
            entity = kunde;
            links = getTransitionalLinks(kunde, uriInfo);
        }
        
        return Response.ok(entity)
                       .links(links)
                       .build();
    }
    
    private Link[] getTransitionalLinksKunden(List<? extends AbstractKunde> kunden, UriInfo uriInfo) {
        if (kunden == null || kunden.isEmpty()) {
            return new Link[0];
        }
        
        final Link first = Link.fromUri(getUriKunde(kunden.get(0), uriInfo))
                               .rel(FIRST_LINK)
                               .build();
        final int lastPos = kunden.size() - 1;
        final Link last = Link.fromUri(getUriKunde(kunden.get(lastPos), uriInfo))
                              .rel(LAST_LINK)
                              .build();
        
        return new Link[] { first, last };
    }
    
    @POST
    public Response save(@Valid AbstractKunde kunde, @Context UriInfo uriInfo) {
        // Rueckwaertsverweis von Adresse zu Kunde setzen
        kunde.getAdresse().setKunde(kunde);
        final AbstractKunde result = kundenBroker.save(kunde);
        
        return Response.created(getUriKunde(result, uriInfo))
                       .build();
    }
    
    @PUT
    public void update(@Valid AbstractKunde kunde) {
        kundenBroker.update(kunde);
    }
    
    @PUT
    @Path("{id:" + UUID_PATTERN + "}")
    public void update(@PathParam("id") UUID kundeId, @Valid AbstractKunde kunde) {
        if (Objects.equals(kundeId, kunde.getId())) {
            update(kunde);
        }
    }
    
    @DELETE
    @Produces
    @Consumes
    @Path("{id:" + UUID_PATTERN + "}")
    public void delete(@PathParam("id") UUID kundeId) {               //NOSONAR
        kundenBroker.delete(kundeId);
    }
    
    //--------------------------------------------------------------------------
    // Methoden fuer URIs und Links
    //--------------------------------------------------------------------------
    
    private URI getUriKunde(AbstractKunde kunde, UriInfo uriInfo) {
        return uriHelper.getUri(KundenResource.class, FIND_BY_ID, kunde.getId(), uriInfo);
    }

    private void setStructuralLinks(AbstractKunde kunde, UriInfo uriInfo) {
        // URI fuer Bestellungen setzen
        final URI uri = uriHelper.getUri(BestellungenResource.class, BestellungenResource.FIND_BY_KUNDE_ID, kunde.getId(), uriInfo);
        kunde.setBestellungenUri(uri);
    }
    
    private Link[] getTransitionalLinks(AbstractKunde kunde, UriInfo uriInfo) {
        final Link self = Link.fromUri(getUriKunde(kunde, uriInfo))
                              .rel(SELF_LINK)
                              .build();

        final Link list = Link.fromUri(uriHelper.getUri(KundenResource.class, uriInfo))
                              .rel(LIST_LINK)
                              .build();

        final Link add = Link.fromUri(uriHelper.getUri(KundenResource.class, uriInfo))
                             .rel(ADD_LINK)
                             .build();
        
        final Link update = Link.fromUri(uriHelper.getUri(KundenResource.class, uriInfo))
                                .rel(UPDATE_LINK)
                                .build();
        
        final Link remove = Link.fromUri(uriHelper.getUri(KundenResource.class, DELETE, kunde.getId(), uriInfo))
                                .rel(REMOVE_LINK)
                                .build();
        
        return new Link[] { self, list, add, update, remove };
    }
}
