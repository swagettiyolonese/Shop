/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.shop.warenkorbverwaltung.rest;

import de.shop.kundenverwaltung.domain.AbstractKunde;
import de.shop.kundenverwaltung.rest.KundenResource;
import de.shop.util.Mock;
import de.shop.util.ShopRuntimeException;
import de.shop.util.rest.UriHelper;
import de.shop.warenkorbverwaltung.domain.Warenkorb;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.List;
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
import javax.ws.rs.core.GenericEntity;
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
 *
 * @author Dennis Heer
 */
@Path("/warenkorb")
@Produces( {APPLICATION_JSON, APPLICATION_XML + ";qs=0.75", TEXT_XML + ";qs=0.5" })
@Consumes( {APPLICATION_JSON, APPLICATION_XML, TEXT_XML })
@RequestScoped
public class WarenkorbResource {
    public static final String WARENKORB_ID_PATH_PARAM = "id"; 
    public static final String KUNDE_ID_PATH_PARAM = "id";
    
    public static final Method FIND_BY_ID;
    public static final Method FIND_BY_KUNDE_ID;
    
    @Inject
    private UriHelper uriHelper;
    
    private final Mock mock = new Mock();
    
    static {
        try {
            FIND_BY_ID = WarenkorbResource.class.getMethod("findById", UUID.class, UriInfo.class);
            FIND_BY_KUNDE_ID = WarenkorbResource.class.getMethod("findByKundeId", UUID.class, UriInfo.class);
        } catch (NoSuchMethodException | SecurityException e) {
            throw new ShopRuntimeException(e);
        }
    }
    
    @GET
    @Path("{" + WARENKORB_ID_PATH_PARAM + ":" + UUID_PATTERN + "}")
    public Response findById(@PathParam(WARENKORB_ID_PATH_PARAM) UUID id, 
                             @Context UriInfo uriInfo) {
        final Optional<Warenkorb> warenkorbOpt = mock.findWarenkorbById(id);
        if(!warenkorbOpt.isPresent()) {
            return Response.status(NOT_FOUND).build();
        }
        
        final Warenkorb warenkorb = warenkorbOpt.get();
        setStructuralLinks(warenkorb, uriInfo);
        
        //Link-Header setzen
        return Response.ok(warenkorb)
                       .links(getTransitionalLinks(warenkorb, uriInfo))
                       .build();
    }
    
    @GET
    @Path("kunde/{" + KUNDE_ID_PATH_PARAM + ":[1-9]\\d*}")
    public Response findByKundeId(@PathParam(KUNDE_ID_PATH_PARAM) UUID kundeId,
                                  @Context UriInfo uriInfo) {
        // TODO Anwendungskern statt Mock
        final Optional<AbstractKunde> kundeOpt = mock.findKundeById(kundeId);
        if (!kundeOpt.isPresent()) {
            return Response.status(NOT_FOUND).build();
        }
        
        final AbstractKunde kunde = kundeOpt.get();
        final Optional<Warenkorb> warenkorbOpt = mock.findWarenkorbByKunde(kunde);
        if (!warenkorbOpt.isPresent()) {
            return Response.status(NOT_FOUND).build();
        }
        
        final Warenkorb warenkorb = warenkorbOpt.get();
        // URIs innerhalb der gefundenen Bestellungen anpassen
        setStructuralLinks(warenkorb, uriInfo);
        
        return Response.ok(new GenericEntity<Warenkorb>(warenkorb){})     //NOSONAR
                       .links(getTransitionalLinks(warenkorb, uriInfo))
                       .build();
    }
    
    //--------------------------------------------------------------------------
    // Methoden fuer URIs und Links
    //--------------------------------------------------------------------------
    public URI getUriWarenkorb(Warenkorb warenkorb, UriInfo uriInfo) {
        return uriHelper.getUri(WarenkorbResource.class, FIND_BY_ID, warenkorb.getId(), uriInfo);
    }
    
    public void setStructuralLinks(Warenkorb warenkorb, UriInfo uriInfo) {
        // URI fuer Kunde setzen
        final AbstractKunde kunde = warenkorb.getKunde();
        if (kunde != null) {
            final URI kundeUri = uriHelper.getUri(KundenResource.class, KundenResource.FIND_BY_ID, kunde.getId(), uriInfo);
            warenkorb.setKundeUri(kundeUri);
        }
    }
    
    private Link[] getTransitionalLinks(Warenkorb warenkorb, UriInfo uriInfo) {
        final Link self = Link.fromUri(getUriWarenkorb(warenkorb, uriInfo))
                              .rel(SELF_LINK)
                              .build();
        return new Link[] { self };
    }
    
}
