/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.shop.artikelverwaltung.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.shop.bestellverwaltung.domain.Bestellung;
import de.shop.kundenverwaltung.domain.AbstractKunde;
import java.net.URI;
import java.util.Objects;
import java.util.UUID;
import javax.xml.bind.annotation.XmlTransient;

import static de.shop.util.Constants.HASH_PRIME;

/**
 * This is the domain class of an article. An order needs to implement at least one article.
 * @author Jan
 */
public class Artikel { // TODO: Add bean validation!
    private UUID id;
    
    private String artikelName;
    private Integer lagerBestand;
    private Float preis;
    private String artikelBeschreibung;
    
    @XmlTransient
    @JsonIgnore
    private Bestellung bestellung;
    
    private URI bestellungURI;

    /**
     * @return the artikelName
     */
    public String getArtikelName() {
        return artikelName;
    }

    /**
     * @param artikelName the artikelName to set
     */
    public void setArtikelName(String artikelName) {
        this.artikelName = artikelName;
    }

    /**
     * @return the lagerBestand
     */
    public Integer getLagerBestand() {
        return lagerBestand;
    }

    /**
     * @param lagerBestand the lagerBestand to set
     */
    public void setLagerBestand(Integer lagerBestand) {
        this.lagerBestand = lagerBestand;
    }

    /**
     * @return the preis
     */
    public Float getPreis() {
        return preis;
    }

    /**
     * @param preis the preis to set
     */
    public void setPreis(Float preis) {
        this.preis = preis;
    }

    /**
     * @return the artikelBeschreibung
     */
    public String getArtikelBeschreibung() {
        return artikelBeschreibung;
    }

    /**
     * @param artikelBeschreibung the artikelBeschreibung to set
     */
    public void setArtikelBeschreibung(String artikelBeschreibung) {
        this.artikelBeschreibung = artikelBeschreibung;
    }

    /**
     * @return the bestellung
     */
    public Bestellung getBestellung() {
        return bestellung;
    }

    /**
     * @param bestellung the bestellung to set
     */
    public void setBestellung(Bestellung bestellung) {
        this.bestellung = bestellung;
    }

    /**
     * @return the bestellungURI
     */
    public URI getBestellungURI() {
        return bestellungURI;
    }

    /**
     * @param bestellungURI the bestellungURI to set
     */
    public void setBestellungURI(URI bestellungURI) {
        this.bestellungURI = bestellungURI;
    }

    @Override
    public int hashCode() {
        final int prime = HASH_PRIME;
        return prime + Objects.hashCode(id);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Artikel other = (Artikel) obj;
        return Objects.equals(id, other.id);
    }
    
    @Override
    public String toString() {
        return "Artikel {id=" + id + //
                ", artikelname=" + artikelName + //
                ", lagerbestand=" + lagerBestand + //
                ", preis=" + preis + //
                ", artikelbeschreibung=" + artikelBeschreibung + //
                ", bestellungURI=" + bestellungURI + '}';
    }
}