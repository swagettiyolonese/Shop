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

import de.shop.kundenverwaltung.domain.Adresse;
import de.shop.kundenverwaltung.domain.Privatkunde;

/**
 * @author <a href="mailto:Juergen.Zimmermann@HS-Karlsruhe.de">J&uuml;rgen Zimmermann</a>
 */
public class PrivatkundeBuilder {
    private final Privatkunde kunde = new Privatkunde();

    public PrivatkundeBuilder nachname(String nachname) {
        kunde.setNachname(nachname);
        return this;
    }
    
    public PrivatkundeBuilder email(String email) {
        kunde.setEmail(email);
        return this;
    }
    
    public PrivatkundeBuilder adresse(Adresse adresse) {
        kunde.setAdresse(adresse);
        adresse.setKunde(kunde);
        return this;
    }

    public Privatkunde build() {
        return kunde;
    }
}
