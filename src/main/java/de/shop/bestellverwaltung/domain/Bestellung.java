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

package de.shop.bestellverwaltung.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.shop.artikelverwaltung.domain.Artikel;
import de.shop.kundenverwaltung.domain.AbstractKunde;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import static de.shop.util.Constants.HASH_PRIME;

/**
 * @author <a href="mailto:Juergen.Zimmermann@HS-Karlsruhe.de">J&uuml;rgen Zimmermann</a>
 */
@XmlRootElement
public class Bestellung {
    private UUID id;
    private boolean ausgeliefert;
    
    @XmlTransient
    @JsonIgnore
    private List<Artikel> artikel;
    
    @XmlTransient
    @JsonIgnore
    private AbstractKunde kunde;
    
    private URI artikelUri;
    private URI kundeUri;
    
    public UUID getId() {
        return id;
    }

    public boolean isAusgeliefert() {
        return ausgeliefert;
    }
    
    public void setAusgeliefert(boolean ausgeliefert) {
        this.ausgeliefert = ausgeliefert;
    }
    
    public List<Artikel> getArtikel() {
        return artikel;
    }

    public void setArtikel(List<Artikel> artikel) {
        this.artikel = artikel;
    }

    public URI getArtikelUri() {
        return artikelUri;
    }

    public void setArtikelUri(URI artikelUri) {
        this.artikelUri = artikelUri;
    }
    
    public AbstractKunde getKunde() {
        return kunde;
    }
    
    public void setKunde(AbstractKunde kunde) {
        this.kunde = kunde;
    }
    
    public URI getKundeUri() {
        return kundeUri;
    }
    public void setKundeUri(URI kundeUri) {
        this.kundeUri = kundeUri;
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
        final Bestellung other = (Bestellung) obj;
        return Objects.equals(id, other.id);
    }
    
    @Override
    public String toString() {
        return "Bestellung {id=" + id //
                + ", ausgeliefert=" + ausgeliefert //
                + ", kundeUri=" + kundeUri //
                + ", artikelUri=" + artikelUri
                + '}';
    }
}
