/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.shop.warenkorbverwaltung.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.shop.artikelverwaltung.domain.Artikel;
import de.shop.kundenverwaltung.domain.AbstractKunde;
import java.net.URI;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlTransient;

import static de.shop.util.Constants.HASH_PRIME;

/**
 *
 * @author Dennis Heer
 */
public class Warenkorb {
    private UUID id;
    
    @XmlTransient
    @JsonIgnore
    private AbstractKunde kunde;
    
    private URI kundeUri;
    
    private List<Artikel> artikel;
    
    @NotNull
    private int value;    
    
    public UUID getId() {
        return id;
    }
    
    public AbstractKunde getKunde() {
        return kunde;
    }
    
    public void setKunde(AbstractKunde kunde) {
        if(kunde == null) {
            throw new NullPointerException();
        }
        
        this.kunde = kunde;
    }
    
    public URI getKundeUri() {
        return kundeUri;
    }
    
    public void setKundeUri(URI kundeUri) {
        if(kundeUri == null) {
            throw new NullPointerException();
        }
        this.kundeUri = kundeUri;
    }
    
    public int getValue() {
        return value;
    }
    
    public void setValue(int value) {
        if(value < 0) {
            throw new InvalidParameterException();
        }
        
        this.value = value;
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
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Warenkorb other = (Warenkorb) obj;
        return Objects.equals(this.id, other.id);
    }
    
    @Override
    public String toString() {
        return "Warenkorb {id=" + id + "}, artikel: [" + artikel.toString() + "], value: " + value + ", kundeUri=" + kundeUri;
    }
}

