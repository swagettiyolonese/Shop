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
import java.util.List;
import org.assertj.core.api.AbstractAssert;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="mailto:Juergen.Zimmermann@HS-Karlsruhe.de">J&uuml;rgen Zimmermann</a>
 */
public class KundenAssert extends AbstractAssert<KundenAssert, List<? extends AbstractKunde>> {
    private KundenAssert(List<? extends AbstractKunde> actual) {
        super(actual, KundenAssert.class);
    }
    
    public static KundenAssert assertThatKunden(List<? extends AbstractKunde> actual) {
        return new KundenAssert(actual);
    }
    
    public KundenAssert isNotEmpty() {
        assertThat(actual)
            .overridingErrorMessage("Es gibt zwar eine Liste von Kunden, die aber leer ist")
            .isNotEmpty();
        return this;
    }

    public KundenAssert doesNotContainNull() {
        assertThat(actual)
            .overridingErrorMessage("Innerhalb der Liste von Kunden gibt es mindestens 1-mal null")
            .doesNotContainNull();
        return this;
    }
    
    public KundenAssert hasSameNachname(String nachname) {
        assertThat(actual.stream()
                         .map(AbstractKunde::getNachname)
                         .collect(toList()))
            .overridingErrorMessage("Jeder Kunde muss den Nachnamen %s haben", nachname)
            .containsOnly(nachname);
        return this;
    }
}
