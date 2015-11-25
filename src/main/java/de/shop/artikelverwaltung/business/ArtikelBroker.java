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

package de.shop.artikelverwaltung.business;

import de.shop.artikelverwaltung.domain.Artikel;
import de.shop.util.Mock;
import de.shop.util.interceptor.Log;
import java.util.Optional;
import java.util.UUID;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import static java.util.Optional.ofNullable;

/**
 * @author <a href="mailto:Juergen.Zimmermann@HS-Karlsruhe.de">J&uuml;rgen Zimmermann</a>
 */
@Dependent
@Log
public class ArtikelBroker {
    private final Mock mock;
    
    @Inject
    ArtikelBroker(Mock mock) {
        super();
        this.mock = mock;
    }
    
    /**
     * Suche eines Artikels zu gegebener ID.
     * falls nichts gefunden wird.
     * @param id Artikel-ID
     * @return Der gefundene Artikel.
     */
    public Optional<Artikel> findById(UUID id) {
        // TODO id pruefen
        // TODO Datenbanzugriffsschicht statt Mock
        final Artikel artikel = mock.findArtikelById(id);
        return ofNullable(artikel);
    }
}
