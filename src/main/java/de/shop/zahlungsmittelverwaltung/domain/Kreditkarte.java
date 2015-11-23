/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.shop.zahlungsmittelverwaltung.domain;

import java.util.Date;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


/**
 *
 * @author Philipp Diemert, 48616, philipp.diemert@outlook.com
 */
public class Kreditkarte extends AbstractZahlungsmittel {
   
    @NotNull
    Date gueltigVon;
    
    @NotNull
    @Future
    Date gueltigBis;
    
    
    //Nummer
    @NotNull
    //Pattern für Kartennummern [xxxx-xxxx-xxxx-xxx]
    @Pattern(regexp = "\\d{16}[0-9]")
    private String kreditkartennummer;
    
    //SecurityCode
    @NotNull
    @Pattern(regexp = "\\d{3}[0-9]")
    private String securitycode;
     
    //Kartentyp (VISA, MC, AMEX usw)
    @NotNull
    //Als Pattern ein Enum implementieren für VISA, MasterCard, Amex usw
    private String karteninstitut;
    
    //ToString (JSON formatiert)
    @Override
    public String toString(){
        
        return "AbstractZahlungsmittel{Kunde=["+ kunde.toString() +"], besitzer= "+getBesitzer()+ ", "
                + "GueltigVon=" + gueltigVon + ", GueltigBis="+ gueltigBis +", id= "+ getId() + "}";
    }

    public Date getGueltigVon() {
        return gueltigVon;
    }

    public void setGueltigVon(Date gueltigVon) {
        this.gueltigVon = gueltigVon;
    }

    public Date getGueltigBis() {
        return gueltigBis;
    }

    public void setGueltigBis(Date gueltigBis) {
        this.gueltigBis = gueltigBis;
    }

    public String getKreditkartennummer() {
        return kreditkartennummer;
    }

    public void setKreditkartennummer(String kreditkartennummer) {
        this.kreditkartennummer = kreditkartennummer;
    }

    public String getSecuritycode() {
        return securitycode;
    }

    public void setSecuritycode(String securitycode) {
        this.securitycode = securitycode;
    }

    public String getKarteninstitut() {
        return karteninstitut;
    }

    public void setKarteninstitut(String karteninstitut) {
        this.karteninstitut = karteninstitut;
    }
    
    
    
    
    
    
}
