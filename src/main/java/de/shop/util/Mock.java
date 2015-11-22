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
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.IntStream;

import static java.lang.System.out;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.UUID.randomUUID;

/**
 * Emulation des Anwendungskerns
 * @author <a href="mailto:Juergen.Zimmermann@HS-Karlsruhe.de">J&uuml;rgen Zimmermann</a>
 */
public class Mock {
    private static final long MAX_ID = 0xFFF_000_000_000L;
    private static final int MAX_KUNDEN = 8;
    private static final int MAX_BESTELLUNGEN = 4;
    private static final int MAX_ARTIKEL = 10;

    public Optional<AbstractKunde> findKundeById(UUID id) {
        return findKundeById(id, true);
    }
    
    private Optional<AbstractKunde> findKundeById(UUID id, boolean checkId) {
        final String idStr = id.toString();
        // Take only the last 12 hex ziffern (2 stupid 2 translate)
        final long tmp = Long.decode("0x" + idStr.substring(idStr.length() - 12));
        if (checkId && tmp > MAX_ID) {
            return empty();
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
        
        kunde.setNachname("Nachname" + id);
        kunde.setEmail("" + id + "@hska.de");
        
        // adress needs an id
        saveAdresse(randomUUID(), kunde);
        
        if (kunde.getClass().equals(Privatkunde.class)) {
            final Privatkunde privatkunde = (Privatkunde) kunde;
            final Set<HobbyType> hobbies = new HashSet<>();
            hobbies.add(HobbyType.LESEN);
            hobbies.add(HobbyType.REISEN);
            privatkunde.setHobbies(hobbies);
        }
        
        return of(kunde);
    }

    private static void saveAdresse(UUID id, AbstractKunde kunde) {
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

    public Optional<List<AbstractKunde>> findAllKunden() {
        final int anzahl = MAX_KUNDEN;
        final List<AbstractKunde> kunden = new ArrayList<>(anzahl);
        IntStream.rangeClosed(1, anzahl)
                 .forEach(i -> {
            final AbstractKunde kunde = findKundeById(randomUUID(), false).get();
            kunden.add(kunde);            
        });
        return of(kunden);
    }

    public Optional<List<AbstractKunde>> findKundenByNachname(String nachname) {
        final int anzahl = nachname.length();
        final List<AbstractKunde> kunden = new ArrayList<>(anzahl);
        IntStream.rangeClosed(1, anzahl)
                 .forEach(i -> {
            final AbstractKunde kunde = findKundeById(randomUUID(), false).get();
            kunde.setNachname(nachname);
            kunden.add(kunde);            
        });
        return of(kunden);
    }
    

    public Optional<List<Bestellung>> findBestellungenByKunde(AbstractKunde kunde) {
        // Beziehungsgeflecht zwischen Kunde und Bestellungen aufbauen:
        // 1, 2, 3 oder 4 Bestellungen
        final int anzahl = (int) (kunde.getId().getLeastSignificantBits() % MAX_BESTELLUNGEN) + 1;
        final List<Bestellung> bestellungen = new ArrayList<>(anzahl);
        IntStream.rangeClosed(1, anzahl)
                 .forEach(i -> {
            final Bestellung bestellung = findBestellungById(randomUUID()).get();
            bestellung.setKunde(kunde);
            bestellungen.add(bestellung);            
        });
        kunde.setBestellungen(bestellungen);
        
        return of(bestellungen);
    }

    public Optional<Bestellung> findBestellungById(UUID id) {
        // andere ID fuer den Kunden
        final AbstractKunde kunde = findKundeById(randomUUID(), false).get();

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
        
        return of(bestellung);
    }

    public AbstractKunde saveKunde(AbstractKunde kunde) {
        // Neue IDs fuer Kunde und zugehoerige Adresse
        // Ein neuer Kunde hat auch keine Bestellungen
        // SecureRandom ist eigentlich sicherer, aber auch l0x langsamer (hier: nur Mock-Klasse)
        final UUID id = randomUUID();

        // Das private Attribut "id" setzen, ohne dass es eine set-Methode gibt
        try {
            final Field idField = AbstractKunde.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(kunde, id);
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
        
        out.println("Neuer Kunde: " + kunde);                  //NOSONAR
        out.println("Neue Adresse: " + adresse);               //NOSONAR
        return kunde;
    }

    public void updateKunde(AbstractKunde kunde) {
        out.println("Aktualisierter Kunde: " + kunde);         //NOSONAR
    }

    public void deleteKunde(UUID kundeId) {
        out.println("Kunde mit ID=" + kundeId + " geloescht");   //NOSONAR
    }
    
    public Optional<Artikel> findArtikelById(UUID artikelId) {
        final String idStr = artikelId.toString();
        // Take only the last 12 hex digits
        final long tmp = Long.decode("0x" + idStr.substring(idStr.length() - 12));
        if (tmp > MAX_ID) {
            return empty();
        }
        
        final Artikel artikel = new Artikel();
        
        // Das private Attribut "id" setzen, ohne dass es eine set-Methode gibt
        try {
            final Field idField = Artikel.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(artikel, artikelId);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            throw new ShopRuntimeException(e);
        }
        
        artikel.setArtikelName("Samsung Galaxy S3");
        artikel.setLagerBestand(100);
        artikel.setPreis((float)399.99);
        
        return of(artikel);
    }
    
    public Optional<List<Artikel>> findAllArtikel() {
        final int anzahl = MAX_ARTIKEL;
        final List<Artikel> artikelList = new ArrayList<>(anzahl);
        IntStream.rangeClosed(1, anzahl)
                 .forEach(i -> {
            final Artikel artikel = findArtikelById(randomUUID()).get();
            artikelList.add(artikel);            
        });
        return of(artikelList);
    }
    
    // TODO: Add relationship btw. artikel and bestellung
//    public Optional<List<Artikel>> findArtikelByBestellung(Bestellung bestellung) {
//        return null;
//    }
    
    public void deleteArtikel(UUID artikelId) {
        out.println("Artikel mit ID=" + artikelId + " geloescht");
    }
}
