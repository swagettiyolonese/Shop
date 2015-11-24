/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.shop.zahlungsmittelverwaltung.rest;


import de.shop.bestellverwaltung.rest.BestellungenResource;
import de.shop.kundenverwaltung.domain.AbstractKunde;
import de.shop.kundenverwaltung.rest.KundenResource;
import de.shop.util.Mock;
import de.shop.util.ShopRuntimeException;
import de.shop.util.rest.UriHelper;
//import java.lang.reflect.Method;
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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import static de.shop.util.Constants.UUID_PATTERN;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static javax.ws.rs.core.MediaType.TEXT_XML;




/**
 *
 * @author Philipp Diemert, 48616, philipp.diemert@outlook.com
 */

import de.shop.zahlungsmittelverwaltung.domain.AbstractZahlungsmittel;
import de.shop.zahlungsmittelverwaltung.domain.Kreditkarte;
import java.net.URI;
import java.util.List;
import javax.ws.rs.DELETE;
import javax.ws.rs.PUT;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Link;
import org.hibernate.annotations.UpdateTimestamp;

import static de.shop.kundenverwaltung.rest.KundenResource.DELETE;
import static de.shop.util.Constants.FIRST_LINK;
import static de.shop.util.Constants.LAST_LINK;
import static de.shop.util.Constants.SELF_LINK;
import static javax.accessibility.AccessibleTableModelChange.UPDATE;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import java.lang.reflect.Method;


@Path("/zahlungsmittel")
@Produces({ APPLICATION_JSON, APPLICATION_XML + ";qs=0.75", TEXT_XML + ";qs=0.5" })
@Consumes({ APPLICATION_JSON, APPLICATION_XML, TEXT_XML })
@RequestScoped

public class ZahlungsmittelResource {
    public static final String ZAHLUNGSMITTEL_ID_PATH_PARAM = "id";
    public static final String KUNDE_ID_PATH_PARAM = "id";
    
    public static final Method FIND_BY_ID;
    //public static final Method FIND_BY_KUNDE_ID;
    
    @Inject
    private UriHelper uriHelper;
    
    private final Mock mock = new Mock();

    static {
        try {
            FIND_BY_ID = ZahlungsmittelResource.class.getMethod("findById", UUID.class, UriInfo.class);
            //FIND_BY_KUNDE_ID = ZahlungsmittelResource.class.getMethod("findByKundeId", UUID.class, UriInfo.class);
        } catch (NoSuchMethodException | SecurityException e) {
            throw new ShopRuntimeException(e);
        }
    }
    
    @GET
    @Produces(APPLICATION_JSON)
    @Path("version")
    public String getVersion() {
        return "{version: \"1.0\"}";
    }
    
    //Get für FindbyID
    //Open: findZahlungsmittelById (mock), setStrucutralLinks, getTransitionallinks
    @GET
    @Path("{" + ZAHLUNGSMITTEL_ID_PATH_PARAM + ":" + UUID_PATTERN + "}")
    public Response findById(@PathParam(ZAHLUNGSMITTEL_ID_PATH_PARAM) UUID id,
                             @Context UriInfo uriInfo) {
        // TODO Anwendungskern statt Mock
        final Optional<AbstractZahlungsmittel> zahlungsmittelOpt = mock.findZahlungsmittelById(id);
        if (!zahlungsmittelOpt.isPresent()) {
            return Response.status(NOT_FOUND).build();
        }
        
        final AbstractZahlungsmittel zahlungsmittel = zahlungsmittelOpt.get();
        setStructuralLinks(zahlungsmittel, uriInfo);
        
        // Link-Header setzen
        return Response.ok(zahlungsmittel)
                       .links(getTransitionalLinks(zahlungsmittel, uriInfo))
                       .build();
    }
    
    
    @GET
    public Response findAll(@Context UriInfo uriInfo) {
        final Optional<List<AbstractZahlungsmittel>> zahlungsmittelOptionalOpt = mock.findAllZahlungsmittel();
        if (!zahlungsmittelOptionalOpt.isPresent()) {
            return Response.status(NOT_FOUND).build();
        }
        
        final List<AbstractZahlungsmittel> zahlungsmittel = zahlungsmittelOptionalOpt.get();
        // setStructuralLinks(artikel, uriInfo);
        
        // Link-Header setzen
        return Response.ok(new GenericEntity<List<AbstractZahlungsmittel>>(zahlungsmittel){})
                       .links(getTransitionalLinks(zahlungsmittel, uriInfo))
                       .build();
        
    }
    
    //Get für FindbyKundeID: Gibt alle auf den Kunden registrierte Zahlungsmittel zurück
    @GET
    @Path("kunde/{" + KUNDE_ID_PATH_PARAM + ":[1-9]\\d*}")
    public Response findbyKundeId(@PathParam(KUNDE_ID_PATH_PARAM)UUID id, @Context UriInfo uriInfo){
        
        
       final Optional<AbstractKunde> kundeOpt = mock.findKundeById(id);
        if (!kundeOpt.isPresent()) {
            return Response.status(NOT_FOUND).build();
        }
        
        final AbstractKunde kunde = kundeOpt.get();
        final Optional<List<AbstractZahlungsmittel>> zahlungsmittelOpt = mock.findZahlungsmittelbyKunde(kunde);
        if (!zahlungsmittelOpt.isPresent()) {
            return Response.status(NOT_FOUND).build();
        }
        
        final List<AbstractZahlungsmittel> zahlungsmittel = zahlungsmittelOpt.get();
        // URIs innerhalb der gefundenen Bestellungen anpassen
        zahlungsmittel.forEach(b -> setStructuralLinks(b, uriInfo));
        
        return Response.ok(new GenericEntity<List<AbstractZahlungsmittel>>(zahlungsmittel){})     //NOSONAR
                       .links(getTransitionalLinks(zahlungsmittel, uriInfo))
                       .build();
        
    }
    
    
    //@POST für neues Zahlungsmittel für Kunde X
    
    @DELETE
    @Path("{id:" + UUID_PATTERN + "}")
    public void delete(@PathParam("id") UUID zahlungsmittelId) {
        mock.deleteZahlungsmittel(zahlungsmittelId);
    }
    
    @PUT
    @Path("{id:" +UUID_PATTERN + "}")
    public void update(@PathParam("id") UUID zahlungsmittelId){
        mock.updateZahlungsmittel(zahlungsmittelId);
    }
    
    //--------Methoden für URIs und Links----------
    //---------------------------------------------
    
    public URI getUriZahlungsmittel(AbstractZahlungsmittel zahlungsmittel, UriInfo uriInfo) {
        return uriHelper.getUri(BestellungenResource.class, FIND_BY_ID, zahlungsmittel.getId(), uriInfo);
    }
    
     public void setStructuralLinks(AbstractZahlungsmittel zahlungsmittel, UriInfo uriInfo) {
        // URI fuer Kunde setzen
        final AbstractKunde kunde = zahlungsmittel.getKunde();
        if (kunde != null) {
            final URI kundeUri = uriHelper.getUri(KundenResource.class, KundenResource.FIND_BY_ID, kunde.getId(), uriInfo);
            zahlungsmittel.setKundeUri(kundeUri);
        }
    }
    
    private Link[] getTransitionalLinks(AbstractZahlungsmittel zahlungsmittel, UriInfo uriInfo) {
        final Link self = Link.fromUri(getUriZahlungsmittel(zahlungsmittel, uriInfo))
                              .rel(SELF_LINK)
                              .build();
        return new Link[] { self };
    }
    
        
    private Link[] getTransitionalLinks(List<AbstractZahlungsmittel> zahlungsmittel, UriInfo uriInfo) {
        if (zahlungsmittel == null || zahlungsmittel.isEmpty()) {
            return new Link[0];
        }
   
        final Link first = Link.fromUri(getUriZahlungsmittel(zahlungsmittel.get(0), uriInfo))
                               .rel(FIRST_LINK)
                               .build();
        final int lastPos = zahlungsmittel.size() - 1;
        
        final Link last = Link.fromUri(getUriZahlungsmittel(zahlungsmittel.get(lastPos), uriInfo))
                              .rel(LAST_LINK)
                              .build();
        
        return new Link[] { first, last };
    }
    
    
}
