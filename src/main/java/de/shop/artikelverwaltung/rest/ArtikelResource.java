/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.shop.artikelverwaltung.rest;

import de.shop.artikelverwaltung.domain.Artikel;
import de.shop.util.Mock;
import de.shop.util.ShopRuntimeException;
import de.shop.util.rest.UriHelper;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import static de.shop.util.Constants.FIRST_LINK;
import static de.shop.util.Constants.LAST_LINK;
import static de.shop.util.Constants.SELF_LINK;
import static de.shop.util.Constants.UUID_PATTERN;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static javax.ws.rs.core.MediaType.TEXT_XML;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;

/**
 * The resource class of Artikel
 * @author Jan
 */
@Path("/artikel")
@Produces({ APPLICATION_JSON, APPLICATION_XML + ";qs=0.75", TEXT_XML + ";qs=0.5" })
@Consumes({ APPLICATION_JSON, APPLICATION_XML, TEXT_XML })
@RequestScoped
public class ArtikelResource {
    public static final String ARTIKEL_ID_PATH_PARAM = "id";
    public static final String BESTELLUNG_ID_PATH_PARAM = "id";
    
    public static final Method FIND_BY_ID;
    public static final Method FIND_BY_BESTELLUNG_ID;
    
    @Inject
    private UriHelper uriHelper;
    
    private final Mock mock = new Mock();

    static {
        try {
            FIND_BY_ID = ArtikelResource.class.getMethod("findById", UUID.class, UriInfo.class);
            FIND_BY_BESTELLUNG_ID = ArtikelResource.class.getMethod("findByBestellungId", UUID.class, UriInfo.class);
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
    
    /**
     * Example: /artikel/ID
     * @param id
     * @param uriInfo
     * @return 
     */
    @GET
    @Path("{" + ARTIKEL_ID_PATH_PARAM + ":" + UUID_PATTERN + "}")
    public Response findById(@PathParam(ARTIKEL_ID_PATH_PARAM) UUID id,
                             @Context UriInfo uriInfo) {
        // TODO Anwendungskern statt Mock
        final Optional<Artikel> artikelOpt = mock.findArtikelById(id);
        if (!artikelOpt.isPresent()) {
            return Response.status(NOT_FOUND).build();
        }
        
        final Artikel artikel = artikelOpt.get();
        
        // Link-Header setzen
        return Response.ok(artikel)
                       .links(getTransitionalLinks(artikel, uriInfo))
                       .build();
    }
    
    /**
     * Example: /artikel
     * @param uriInfo
     * @return 
     */
    @GET
    public Response findAll(@Context UriInfo uriInfo) {
        final Optional<List<Artikel>> artikelOpt = mock.findAllArtikel();
        if (!artikelOpt.isPresent()) {
            return Response.status(NOT_FOUND).build();
        }
        
        final List<Artikel> artikel = artikelOpt.get();
        
        // Link-Header setzen
        return Response.ok(new GenericEntity<List<Artikel>>(artikel){})
                       .links(getTransitionalLinks(artikel, uriInfo))
                       .build();
        
    }
    
    @DELETE
    @Path("{id:" + UUID_PATTERN + "}")
    public void delete(@PathParam("id") UUID artikelId) {
        mock.deleteArtikel(artikelId);
    }
    
    @PUT
    public void update(@Valid Artikel artikel) {
        mock.updateArtikel(artikel);
    }
    
    /**
     * Example: /artikel/bestellung/ID
     * @param bestellungID
     * @param uriInfo
     * @return 
     */    
    @GET
    @Path("bestellung/{" + BESTELLUNG_ID_PATH_PARAM + ":" + UUID_PATTERN + "}")
    public Response findByBestellungId(@PathParam(BESTELLUNG_ID_PATH_PARAM) UUID bestellungID,
                                  @Context UriInfo uriInfo) {
        return findAll(uriInfo);
    }

    
    //--------------------------------------------------------------------------
    // Methoden fuer URIs und Links // HIER ALLES FERTIG
    //--------------------------------------------------------------------------
    public URI getUriArtikel(Artikel artikel, UriInfo uriInfo) {
        return uriHelper.getUri(ArtikelResource.class, FIND_BY_ID, artikel.getId(), uriInfo);
    }
        
    private Link[] getTransitionalLinks(Artikel artikel, UriInfo uriInfo) {
        final Link self = Link.fromUri(getUriArtikel(artikel, uriInfo))
                              .rel(SELF_LINK)
                              .build();
        return new Link[] { self };
    }
    
        
    private Link[] getTransitionalLinks(List<Artikel> artikel, UriInfo uriInfo) {
        if (artikel == null || artikel.isEmpty()) {
            return new Link[0];
        }
   
        final Link first = Link.fromUri(getUriArtikel(artikel.get(0), uriInfo))
                               .rel(FIRST_LINK)
                               .build();
        final int lastPos = artikel.size() - 1;
        
        final Link last = Link.fromUri(getUriArtikel(artikel.get(lastPos), uriInfo))
                              .rel(LAST_LINK)
                              .build();
        
        return new Link[] { first, last };
    }
}
