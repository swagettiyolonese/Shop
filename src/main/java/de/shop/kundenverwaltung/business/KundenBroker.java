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

package de.shop.kundenverwaltung.business;

import de.shop.kundenverwaltung.domain.AbstractKunde;
import de.shop.util.Mock;
import de.shop.util.interceptor.Log;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;

/**
 * @author <a href="mailto:Juergen.Zimmermann@HS-Karlsruhe.de">J&uuml;rgen Zimmermann</a>
 */
@Dependent
@Log
public class KundenBroker {
    private final Mock mock;
    
    @Inject
    KundenBroker(Mock mock) {
        super();
        this.mock = mock;
    }
    
    public Optional<AbstractKunde> findById(UUID id) {
        // TODO Datenbanzugriffsschicht statt Mock
        final AbstractKunde kunde = mock.findKundeById(id);
        return ofNullable(kunde);
    }
    
    public Optional<AbstractKunde> findByEmail(String email) {
        // TODO Datenbanzugriffsschicht statt Mock
        final AbstractKunde kunde = mock.findKundeByEmail(email);
        return ofNullable(kunde);
    }
    
    public Optional<List<AbstractKunde>> findAll() {
        // TODO Datenbanzugriffsschicht statt Mock
        final List<AbstractKunde> kunden = mock.findAllKunden();
        return kunden.isEmpty() ? empty() : of(kunden);
    }
    
    public Optional<List<AbstractKunde>> findByNachname(String nachname) {
        // TODO Datenbanzugriffsschicht statt Mock
        final List<AbstractKunde> kunden =  mock.findKundenByNachname(nachname);
        return kunden.isEmpty() ? empty() : of(kunden);
    }

    public <K extends AbstractKunde> K save(K kunde) {
        // Kein Aufruf als Business-Methode
        final Optional<AbstractKunde> tmp = findByEmail(kunde.getEmail());
        if (tmp.isPresent()) {
            throw new EmailExistsException(kunde.getEmail());
        }
        // TODO Datenbanzugriffsschicht statt Mock
        return mock.saveKunde(kunde);
    }
    
    public <K extends AbstractKunde> K update(K kunde) {
        // Pruefung, ob die Email-Adresse schon existiert
        // Kein Aufruf als Business-Methode
        final Optional<AbstractKunde> vorhandenerKundeOpt = findByEmail(kunde.getEmail());
        if (vorhandenerKundeOpt.isPresent()) {
            final UUID id = vorhandenerKundeOpt.get().getId();
            if (!Objects.equals(id, kunde.getId())) {
                throw new EmailExistsException(kunde.getEmail());
            }
        }

        // TODO Datenbanzugriffsschicht statt Mock
        mock.updateKunde(kunde);
        
        return kunde;
    }

    public void delete(UUID id) {
        // Kein Aufruf als Business-Methode
        Optional<AbstractKunde> kundeOpt = findById(id);
        if (!kundeOpt.isPresent()) {
            return;
        }
        final AbstractKunde kunde = kundeOpt.get();

        // Gibt es Bestellungen?
        if (kunde.getBestellungen() != null && !kunde.getBestellungen().isEmpty()) {
            throw new KundeDeleteBestellungException(kunde);
        }
        
        // TODO Datenbanzugriffsschicht statt Mock
        mock.deleteKunde(kunde);
    }
}
