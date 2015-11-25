/*
 * Copyright (C) 2013 - 2015 Juergen Zimmermann, Hochschule Karlsruhe
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

package de.shop.util;

import de.shop.artikelverwaltung.domain.Artikel;
import de.shop.bestellverwaltung.domain.Bestellung;
import de.shop.kundenverwaltung.domain.AbstractKunde;
import de.shop.kundenverwaltung.domain.Adresse;
import de.shop.kundenverwaltung.domain.Firmenkunde;
import de.shop.kundenverwaltung.domain.HobbyType;
import de.shop.kundenverwaltung.domain.Privatkunde;
import de.shop.util.interceptor.Log;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import javax.enterprise.context.Dependent;

import static java.lang.Math.abs;
import static java.util.Arrays.asList;
import static java.util.UUID.randomUUID;

/**
 * Emulation der Datenbankzugriffsschicht
 * @author <a href="mailto:Juergen.Zimmermann@HS-Karlsruhe.de">J&uuml;rgen Zimmermann</a>
 */
@Dependent
@Log
public class Mock {
    private static final Logger LOGGER = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    private static final long MAX_ID = 0xFFF_000_000_000L;;
    private static final int MAX_KUNDEN = 8;
    
    private static final List<String> NACHNAMEN = asList("Alpha", "Beta", "Gamma", "Delta", "Epsilon");
    private static final Random RANDOM = new Random();
    
    private static final int JAHR = 2001;
    // bei Calendar werden die Monate von 0 bis 11 gezaehlt
    private static final int MONAT = 0;
    // bei Calendar die Monatstage ab 1 gezaehlt
    private static final int TAG = 31;

    Mock() {
        super();
    }
    
    public AbstractKunde findKundeById(UUID id) {
        return findKundeById(id, true);
    }

    public AbstractKunde findKundeById(UUID id, boolean checkId) {
        final String idStr = id.toString();
        final long tmp = Long.decode("0x" + idStr.substring(idStr.length() - 12));
        if (checkId && tmp > MAX_ID) {
            return null;
        }
        
        final AbstractKunde kunde = tmp % 2 == 1 ? new Privatkunde() : new Firmenkunde();   //NOSONAR

        // Das private Attribut "id" setzen, ohne dass es eine set-Methode gibt
        try {
            final Field idField = AbstractKunde.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(kunde, id);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            throw new ShopRuntimeException(e);
        }

        final int idx = abs(RANDOM.nextInt()) % NACHNAMEN.size();
        final String nachname = NACHNAMEN.get(idx);
        kunde.setNachname(nachname);
        kunde.setEmail(nachname + "@hska.de");
        final GregorianCalendar seitCal = new GregorianCalendar(JAHR, MONAT, TAG);
        final Date seit = seitCal.getTime();
        kunde.setSeit(seit);
        
        saveAdresse(randomUUID(), kunde);
        
        if (kunde.getClass().equals(Privatkunde.class)) {
            final Privatkunde privatkunde = (Privatkunde) kunde;
            final Set<HobbyType> hobbys = new HashSet<>();
            hobbys.add(HobbyType.LESEN);
            hobbys.add(HobbyType.REISEN);
            privatkunde.setHobbys(hobbys);
        }
        
        return kunde;
    }
    
    private void saveAdresse(UUID id, AbstractKunde kunde) {
        final Adresse adresse = new Adresse();
        // Das private Attribut "id" setzen, ohne dass es eine set-Methode gibt
        try {
            final Field idField = Adresse.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(adresse, id);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            throw new ShopRuntimeException(e);
        }

        adresse.setPlz("12345");
        adresse.setOrt("Testort");

        adresse.setKunde(kunde);
        kunde.setAdresse(adresse);
    }

    public List<AbstractKunde> findAllKunden() {
        final int anzahl = MAX_KUNDEN;
        final List<AbstractKunde> kunden = new ArrayList<>(anzahl);
        IntStream.rangeClosed(1, anzahl)
                 .forEach(i -> {
            final AbstractKunde kunde = findKundeById(randomUUID(), false);
            kunden.add(kunde);            
        });
        return kunden;
    }

    public List<AbstractKunde> findKundenByNachname(String nachname) {
        final int anzahl = nachname.length();
        final List<AbstractKunde> kunden = new ArrayList<>(anzahl);
        IntStream.rangeClosed(1, anzahl)
                 .forEach(i -> {
            final AbstractKunde kunde = findKundeById(randomUUID(), false);
            kunde.setNachname(nachname);
            kunden.add(kunde);            
        });
        return kunden;
    }
    
    public AbstractKunde findKundeByEmail(String email) {
        if (email.startsWith("x")) {
            return null;
        }
        
        final AbstractKunde kunde = findKundeById(randomUUID(), false);
        kunde.setEmail(email);
        return kunde;
    }
    
    public List<Bestellung> findBestellungenByKunde(AbstractKunde kunde) {
        // Beziehungsgeflecht zwischen Kunde und Bestellungen aufbauen:
        // 1, 2, 3 oder 4 Bestellungen
        final int anzahl = kunde.getNachname().length();
        final List<Bestellung> bestellungen = new ArrayList<>(anzahl);
        IntStream.rangeClosed(1, anzahl)
                 .forEach(i -> {
            final Bestellung bestellung = findBestellungById(randomUUID(), false);
            bestellung.setKunde(kunde);
            bestellungen.add(bestellung);            
        });
        kunde.setBestellungen(bestellungen);
        
        return bestellungen;
    }

    public Bestellung findBestellungById(UUID id) {
        return findBestellungById(id, true);
    }
    
    private Bestellung findBestellungById(UUID id, boolean check) {
        final String idStr = id.toString();
        final long tmp = Long.decode("0x" + id.toString().substring(idStr.length() - 12));
        if (tmp > MAX_ID) {
            return null;
        }

        // andere ID fuer den Kunden
        final AbstractKunde kunde = findKundeById(randomUUID(), false);

        final Bestellung bestellung = new Bestellung();
        // Das private Attribut "id" setzen, ohne dass es eine set-Methode gibt
        try {
            final Field idField = Bestellung.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(bestellung, id);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            throw new ShopRuntimeException(e);
        }
        bestellung.setAusgeliefert(false);
        bestellung.setKunde(kunde);
        
        return bestellung;
    }

    public <T extends AbstractKunde> T saveKunde(T kunde) {
        // Neue IDs fuer Kunde und zugehoerige Adresse
        // Ein neuer Kunde hat auch keine Bestellungen
        // SecureRandom ist eigentlich sicherer, aber auch l0x langsamer (hier: nur Mock-Klasse)
        // Das private Attribut "id" setzen, ohne dass es eine set-Methode gibt
        try {
            final Field idField = AbstractKunde.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(kunde, randomUUID());
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            throw new ShopRuntimeException(e);
        }
        
        final Adresse adresse = kunde.getAdresse();
        // Das private Attribut "id" setzen, ohne dass es eine set-Methode gibt
        try {
            final Field idField = Adresse.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(adresse, randomUUID());
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            throw new ShopRuntimeException(e);
        }
        
        adresse.setKunde(kunde);
        kunde.setBestellungen(null);
        
        LOGGER.info("Neuer Kunde: " + kunde);
        LOGGER.info("Neuer Adresse: " + kunde.getAdresse());
        return kunde;
    }

    public void updateKunde(AbstractKunde kunde) {
        LOGGER.info("Aktualisierter Kunde: " + kunde);
    }

    public void deleteKunde(AbstractKunde kunde) {
        LOGGER.info("Geloeschter Kunde: " + kunde);
    }

    public Bestellung saveBestellung(Bestellung bestellung, AbstractKunde kunde) {
        // SecureRandom ist eigentlich sicherer, aber auch l0x langsamer (hier: nur Mock-Klasse)
        // Das private Attribut "id" setzen, ohne dass es eine set-Methode gibt
        try {
            final Field idField = Bestellung.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(bestellung, randomUUID());
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            throw new ShopRuntimeException(e);
        }
        
        LOGGER.info("Neue Bestellung: " + bestellung + " fuer Kunde: " + kunde);
        return bestellung;
    }

    public Artikel findArtikelById(UUID id) {
        final Artikel artikel = new Artikel();
        // Das private Attribut "id" setzen, ohne dass es eine set-Methode gibt
        try {
            final Field idField = Artikel.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(artikel, id);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            throw new ShopRuntimeException(e);
        }
        
        artikel.setBezeichnung("Bezeichnung_" + id);
        return artikel;
    }
}
