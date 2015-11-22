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
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;

import static de.shop.util.Constants.HASH_PRIME;

/**
 * This is the domain class of an article. An order needs to implement at least one article.
 * @author Jan
 */
public class Artikel { // TODO: Validate bean validation
    
    private static final int ARTIKELNAME_LENGTH_MIN = 2;
    private static final int ARTIKELNAME_LENGTH_MAX = 32;
    
    private static final int LAGERBESTAND_MAX = 999999;
    
    private static final int PREIS_MAX_DIGITS = 6; // Vorkommastellen
    private static final int PREIS_MAX_FRACTION = 2; // Nachkommastellen
    
    private static final int ARTIKELBESCHREIBUNG_LENGTH_MAX = 280;
    
    private UUID id;
    
    @NotNull(message = "{artikel.artikelName.notNull}")
    @Size(min = ARTIKELNAME_LENGTH_MIN, //
          max = ARTIKELNAME_LENGTH_MAX, //
          message = "{artikel.artikelName.length}")
    private String artikelName;
    
    @NotNull(message = "{artikel.lagerBestand.notNull}")
    @Max(value = LAGERBESTAND_MAX, //
         message = "{artikel.lagerBestand.max}")
    private Integer lagerBestand;
    
    @NotNull(message = "{artikel.preis.notNull}")
    @Digits(integer = PREIS_MAX_DIGITS, //
            fraction = PREIS_MAX_FRACTION, //
            message = "{artikel.preis.digits}")
    private Float preis;
    
    @Size(max = ARTIKELBESCHREIBUNG_LENGTH_MAX, //
          message = "{artikel.artikelBeschreibung.length}")
    private String artikelBeschreibung;
    
    /**
     * @return the id
     */
    public UUID getId() {
        return id;
    }

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
                ", artikelbeschreibung=" + artikelBeschreibung + '}';
    }
}