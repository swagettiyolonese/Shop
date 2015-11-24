/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.shop.warenkorbverwaltung.domain;

import de.shop.artikelverwaltung.domain.Artikel;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import static de.shop.util.Constants.HASH_PRIME;

/**
 *
 * @author Dennis Heer
 */
public class Warenkorb {
    private UUID id;
    
    private final int WARENKORB_MIN_VALUE = 0;
    private final int WARENKORB_MAX_VALUE = 999;
    
    @NotNull(message = "{warenkorb.kundeUri.notNull}")
    private URI kundeUri;
    
    private List<Artikel> artikel;
    
    @Min(value = WARENKORB_MIN_VALUE, message = "{warenkorb.value.min}")
    @Max(value = WARENKORB_MAX_VALUE, message = "{warenkorb.value.max}")
    @NotNull(message = "{warenkorb.value.notNull}")
    private int value;    
    
    public List<Artikel> getArtikel() {
        return artikel;
    }

    public void setArtikel(List<Artikel> artikel) {
        this.artikel = artikel;
    }
    
    public void addArtikel(Artikel artikel) {
        this.artikel.add(artikel);
    }
    
    public UUID getId() {
        return id;
    }
    
    public URI getKundeUri() {
        return kundeUri;
    }
    
    public void setKundeUri(URI kundeUri) {
        this.kundeUri = kundeUri;
    }
    
    public int getValue() {
        return value;
    }
    
    public void setValue(int value) {
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
        return "Warenkorb {id=" + id + "}, artikel: [" + artikel.toString() + "], value: " + value;
    }
}

