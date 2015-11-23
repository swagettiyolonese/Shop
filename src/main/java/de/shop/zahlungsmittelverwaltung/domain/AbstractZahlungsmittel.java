/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.shop.zahlungsmittelverwaltung.domain;

//@TODO: 
// Zuweisung Zahlungsmittel an Kundenobjekt
// Felder für Adressdaten (Müssen mit Kundendaten übereinstimmen)
// Erweiterung der Bean Validation für reguläre Ausdrücke usw.


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.Date;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import de.shop.kundenverwaltung.domain.AbstractKunde;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.xml.bind.annotation.XmlTransient;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

/**
 *
 * @author Philipp Diemert, 48616, philipp.diemert@outlook.com
 */
@XmlRootElement
@XmlSeeAlso({Kreditkarte.class, ECKarte.class, PaypalZahlung.class})

@JsonTypeInfo(use = NAME, property = "type")
@JsonSubTypes({
    @Type(value = Kreditkarte.class, name = AbstractZahlungsmittel.KREDITKARTE),
    @Type(value = ECKarte.class, name = AbstractZahlungsmittel.ECKARTE),
    @Type(value = PaypalZahlung.class, name = AbstractZahlungsmittel.PAYPALZAHLUNG)
})

public abstract class AbstractZahlungsmittel {
    
    private static final int NACHNAME_LENGTH_MIN = 2;
    private static final int NACHNAME_LENGTH_MAX = 32;
    
    
    //Für spätere Auswertungen
    public static final String KREDITKARTE ="KK";
    public static final String ECKARTE = "EC";
    public static final String PAYPALZAHLUNG = "PP";
    
    //Besitzer des Zahlungsmittel -> Kann sich vom Kunde unterscheiden
    @NotNull(message = "{besitzer.name.notnull}")
    @Min(NACHNAME_LENGTH_MIN)
    @Max(NACHNAME_LENGTH_MAX)            
    @Pattern(regexp=de.shop.kundenverwaltung.domain.AbstractKunde.NACHNAME_PATTERN, message = "{besitzer.name.pattern}")
    private String besitzer;

   
    @NotNull
    @Valid
    AbstractKunde kunde;
    
    
    @NotNull(message = "{gültigkeit.ablaufdatum.notnull}")
    @Future
    private Date ablaufdatum;
    
    private String id;
    
    @XmlTransient
    List <AbstractZahlungsmittel> zahlungsmittel;

    public List<AbstractZahlungsmittel> getZahlungsmittel() {
        return zahlungsmittel;
    }
    
    
    public void setBesitzer(String besitzer) {
        this.besitzer = besitzer;
    }

    public void setAblaufdatum(Date ablaufdatum) {
        this.ablaufdatum = ablaufdatum;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public String getBesitzer() {
        return besitzer;
    }

    public Date getAblaufdatum() {
        return ablaufdatum;
    }

    public String getId() {
        return id;
    }

    public AbstractKunde getKunde() {
        return kunde;
    }

    public void setKunde(AbstractKunde kunde) {
        this.kunde = kunde;
    }
    
    //ToString im JSON Format
    @Override
    public String toString(){
        return "AbstractZahlungsmittel{Kunde=["+ kunde.toString() +"], besitzer= "+besitzer+ ", "
                + "ablaufdatum=" + ablaufdatum + ", id= "+ id + "}";
    }
    
    
}
