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

import de.shop.bestellverwaltung.business.BestellungenBroker;
import de.shop.bestellverwaltung.domain.Bestellung;
import de.shop.kundenverwaltung.business.KundenBroker;
import de.shop.kundenverwaltung.domain.AbstractKunde;
import de.shop.kundenverwaltung.rest.KundenResource;
import de.shop.util.ShopRuntimeException;
import de.shop.util.interceptor.Log;
import de.shop.util.rest.UriHelper;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import static de.shop.util.Constants.ADD_LINK;
import static de.shop.util.Constants.FIRST_LINK;
import static de.shop.util.Constants.LAST_LINK;
import static de.shop.util.Constants.SELF_LINK;
import static de.shop.util.Constants.UUID_PATTERN;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static javax.ws.rs.core.MediaType.TEXT_XML;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;


/**
 * @author <a href="mailto:Juergen.Zimmermann@HS-Karlsruhe.de">J&uuml;rgen Zimmermann</a>
 */
@Path("/bestellungen")
@Produces({ APPLICATION_JSON, APPLICATION_XML + ";qs=0.75", TEXT_XML + ";qs=0.5" })
@Consumes({ APPLICATION_JSON, APPLICATION_XML, TEXT_XML })
@RequestScoped
@Log
public class BestellungenResource {
    public static final Method FIND_BY_ID;
    public static final Method FIND_BY_KUNDE_ID;

    public static final String ID_PATH_PARAM = "id";

    private BestellungenBroker bestellungenBroker;
    private UriHelper uriHelper;
    private Instance<KundenBroker> kundenBrokerInstance;

    static {
        try {
            FIND_BY_ID = BestellungenResource.class.getMethod("findById", UUID.class, UriInfo.class);
            FIND_BY_KUNDE_ID = BestellungenResource.class.getMethod("findByKundeId", UUID.class, UriInfo.class);
        } catch (NoSuchMethodException | SecurityException e) {
            throw new ShopRuntimeException(e);
        }
    }

    /**
     * Public Default-Konstruktor f&uuml;r JAX-RS
     */
    public BestellungenResource() {
        super();
    }
    
    /**
     * Package-private Konstruktor mit "Constructor Injection" f&uuml;r CDI
     * @param bestellungenBroker zu injizierendes Objekt f&uuml;r BestellungenBroker
     * @param uriHelperInstance zu injizierendes Instance-Objekt f&uuml;r UriHelper
     * @param kundenBrokerInstance zu injizierendes Instance-Objekt f&uuml;r KundenBroker
     */
    @Inject
    BestellungenResource(BestellungenBroker bestellungenBroker,
                         UriHelper uriHelper,
                         Instance<KundenBroker> kundenBrokerInstance) {
        super();
        this.bestellungenBroker = bestellungenBroker;
        this.uriHelper = uriHelper;
        this.kundenBrokerInstance = kundenBrokerInstance;
    }

    @GET
    @Path("{" + ID_PATH_PARAM + ":" + UUID_PATTERN + "}")
    public Response findById(@PathParam(ID_PATH_PARAM) UUID id, @Context UriInfo uriInfo) {
        final Optional<Bestellung> bestellungOpt = bestellungenBroker.findById(id);
        if (!bestellungOpt.isPresent()) {
            return Response.status(NOT_FOUND).build();
        }
        
        final Bestellung bestellung = bestellungOpt.get();
        setStructuralLinks(bestellung, uriInfo);
        
        // Link-Header setzen
        return Response.ok(bestellung)
                       .links(getTransitionalLinks(bestellung, uriInfo))
                       .build();
    }
    
        
    @GET
    @Path("kunde/{id:" + UUID_PATTERN + "}")
    public Response findByKundeId(@PathParam("id") UUID kundeId, @Context UriInfo uriInfo) {
        final Optional<AbstractKunde> kundeOpt = kundenBrokerInstance.get().findById(kundeId);
        if (!kundeOpt.isPresent()) {
            return Response.status(NOT_FOUND).build();
        }
        
        final AbstractKunde kunde = kundeOpt.get();
        final Optional<List<Bestellung>> bestellungenOpt = bestellungenBroker.findByKunde(kunde);
        // URIs innerhalb der gefundenen Bestellungen anpassen
        if (!bestellungenOpt.isPresent()) {
            return Response.status(NOT_FOUND).build();
        }
        
        final List<Bestellung> bestellungen = bestellungenOpt.get();
        bestellungen.forEach(b -> setStructuralLinks(b, uriInfo));
        return Response.ok(new GenericEntity<List<Bestellung>>(bestellungen) {})      //NOSONAR
                       .links(getTransitionalLinks(bestellungen, uriInfo))
                       .build();
    }
    
    //--------------------------------------------------------------------------
    // Methoden fuer URIs und Links
    //--------------------------------------------------------------------------
    
    private URI getUriBestellung(Bestellung bestellung, UriInfo uriInfo) {
        return uriHelper.getUri(BestellungenResource.class, FIND_BY_ID, bestellung.getId(), uriInfo);
    }

    private void setStructuralLinks(Bestellung bestellung, UriInfo uriInfo) {
        // URI fuer Kunde setzen
        final AbstractKunde kunde = bestellung.getKunde();
        if (kunde != null) {
            final URI kundeUri = uriHelper.getUri(KundenResource.class, KundenResource.FIND_BY_ID, kunde.getId(), uriInfo);
            bestellung.setKundeUri(kundeUri);
        }
    }
    
    private Link[] getTransitionalLinks(Bestellung bestellung, UriInfo uriInfo) {
        final Link self = Link.fromUri(getUriBestellung(bestellung, uriInfo))
                              .rel(SELF_LINK)
                              .build();
        final Link add = Link.fromUri(uriHelper.getUri(BestellungenResource.class, uriInfo))
                             .rel(ADD_LINK)
                             .build();

        return new Link[] { self, add };
    }
    
    private Link[] getTransitionalLinks(List<Bestellung> bestellungen, UriInfo uriInfo) {
        if (bestellungen == null || bestellungen.isEmpty()) {
            return new Link[0];
        }
        
        final Link first = Link.fromUri(getUriBestellung(bestellungen.get(0), uriInfo))
                               .rel(FIRST_LINK)
                               .build();
        
        final int lastPos = bestellungen.size() - 1;
        final Link last = Link.fromUri(getUriBestellung(bestellungen.get(lastPos), uriInfo))
                              .rel(LAST_LINK)
                              .build();
        
        return new Link[] { first, last };
    }
}
