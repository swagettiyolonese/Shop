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
package de.shop.kundenverwaltung.util;

import de.shop.kundenverwaltung.domain.AbstractKunde;
import java.util.UUID;
import org.assertj.core.api.AbstractAssert;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="mailto:Juergen.Zimmermann@HS-Karlsruhe.de">J&uuml;rgen Zimmermann</a>
 */
public class KundeAssert extends AbstractAssert<KundeAssert, AbstractKunde> {
    private KundeAssert(AbstractKunde actual) {
        super(actual, KundeAssert.class);
    }
    
    public static KundeAssert assertThatKunde(AbstractKunde actual) {
        return new KundeAssert(actual);
    }
    
    public KundeAssert hasId(UUID id) {
        assertThat(actual.getId())
            .overridingErrorMessage("Die ID des Kunden muss %s sein, ist aber %s", id, actual.getId())
            .isEqualTo(id);
        return this;
    }
    
    public KundeAssert hasBestellungenUri() {
        assertThat(actual.getBestellungenUri())
            .overridingErrorMessage("Die URI fuer die Bestellungen ist null")
            .isNotNull();
        return this;
    }

    public KundeAssert hasBestellungenUriEndingWith(String suffix) {
        assertThat(actual.getBestellungenUri().toString())
            .overridingErrorMessage("Die URI fuer die Bestellungen muss mit %s enden", suffix)
            .endsWith(suffix);
        return this;
    }
}
