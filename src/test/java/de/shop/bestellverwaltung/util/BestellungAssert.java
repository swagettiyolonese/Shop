/*
 * Copyright (C) 2015 Juergen Zimmermann, Hochschule Karlsruhe
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
package de.shop.bestellverwaltung.util;

import de.shop.bestellverwaltung.domain.Bestellung;
import java.util.UUID;
import org.assertj.core.api.AbstractAssert;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="mailto:Juergen.Zimmermann@HS-Karlsruhe.de">J&uuml;rgen Zimmermann</a>
 */
public class BestellungAssert extends AbstractAssert<BestellungAssert, Bestellung> {
     private BestellungAssert(Bestellung actual) {
        super(actual, BestellungAssert.class);
    }
    
    public static BestellungAssert assertThatBestellung(Bestellung actual) {
        return new BestellungAssert(actual);
    }
    
    public BestellungAssert hasId(UUID id) {
        assertThat(actual.getId())
            .overridingErrorMessage("Die ID der Bestellung muss %s sein, ist aber %s", id, actual.getId())
            .isEqualTo(id);
        return this;
    }
    
    public BestellungAssert hasKundeUri() {
        assertThat(actual.getKundeUri())
            .overridingErrorMessage("Die Bestellung muss eine URI fuer den Kunden haben")
            .isNotNull();
        return this;
    }
}
