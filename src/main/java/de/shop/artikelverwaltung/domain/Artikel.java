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

package de.shop.artikelverwaltung.domain;

import java.util.Objects;
import java.util.UUID;

import static de.shop.util.Constants.HASH_PRIME;


/**
 * @author <a href="mailto:Juergen.Zimmermann@HS-Karlsruhe.de">J&uuml;rgen Zimmermann</a>
 */
public class Artikel {
    private UUID id;
    
    // TODO Bean Validation
    private String bezeichnung;
    
    public UUID getId() {
        return id;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }
    
    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }
    
    @Override
    public int hashCode() {
        return HASH_PRIME + Objects.hashCode(bezeichnung);
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
        return Objects.equals(bezeichnung, other.bezeichnung);
    }

    @Override
    public String toString() {
        return "Artikel {id=" + id + ", bezeichnung=" + bezeichnung + '}';
    }
}
