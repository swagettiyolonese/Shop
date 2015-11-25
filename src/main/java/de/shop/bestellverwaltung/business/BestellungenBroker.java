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

package de.shop.bestellverwaltung.business;

import de.shop.bestellverwaltung.domain.Bestellung;
import de.shop.kundenverwaltung.domain.AbstractKunde;
import de.shop.util.Mock;
import de.shop.util.interceptor.Log;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;

/**
 * @author <a href="mailto:Juergen.Zimmermann@HS-Karlsruhe.de">J&uuml;rgen Zimmermann</a>
 */
@Dependent
@Log
public class BestellungenBroker {
    private final Instance<Event<Bestellung>> eventInstance;
    private final Mock mock;
    
    @Inject
    BestellungenBroker(@NeueBestellung Instance<Event<Bestellung>> eventInstance, Mock mock) {
        super();
        this.eventInstance = eventInstance;
        this.mock = mock;
    }
    
    /**
     * Bestellung anhand der ID suchen
     * @param id ID der gesuchten Bestellung
     * @return Bestellungsobjekt oder NotFoundException
     */
    public Optional<Bestellung> findById(UUID id) {
        // TODO Datenbanzugriffsschicht statt Mock
        final Bestellung bestellung = mock.findBestellungById(id);
        return ofNullable(bestellung);
    }

    /**
     * Bestellung zu einem gegebenem Kunden suchen
     * @param kunde Das Kundenobjekt
     * @return Die Bestellungsobjekte zum Kundenobjekt oder NotFoundException
     */
    public Optional<List<Bestellung>> findByKunde(AbstractKunde kunde) {
        // TODO Datenbanzugriffsschicht statt Mock
        final List<Bestellung> bestellungen = mock.findBestellungenByKunde(kunde);
        return bestellungen.isEmpty() ? empty() : of(bestellungen);
    }

    /**
     * Eine neue Bestellung zu einem existierenden Kunden anlegen
     * @param bestellung Das neue Bestellungsobjekt
     * @param kunde Das existierende Bestellungsobjekt
     * @return Das Bestellungsobjekt mit generierter ID
     */
    public Bestellung save(Bestellung bestellung, AbstractKunde kunde) {
        // TODO Datenbanzugriffsschicht statt Mock
        final Bestellung result = mock.saveBestellung(bestellung, kunde);
        
        eventInstance.get().fireAsync(result);
        
        return result;
    }
}
