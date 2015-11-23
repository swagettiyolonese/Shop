/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.shop.zahlungsmittelverwaltung.domain;

import java.util.Date;
import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 *
 * @author Philipp Diemert, 48616, philipp.diemert@outlook.com
 */
public class ECKarte extends AbstractZahlungsmittel {
    
    
    @NotNull
    @Pattern(regexp="\\d{2}[A-Z]")
    //IBAN-Ländercode
    private String ibanCountry;
    
    //IBAN-Nummer
    @NotNull
    @Pattern(regexp="\\d{18}[0-9]")
    private String ibanNumber;
    
    
    //IBAN-Komplett
    @Valid
    private String iban = ibanCountry + ibanNumber;
    
    
    //GültigBis
    @NotNull
    @Future
    private Date gueltigBis;
    
    //

    public String getIban() {
        return iban;
    }

    //Ist mit @Valid gegen ungewollte Werte abgesichert
    public void setIban(String ibanCountry, String ibanNumber) {
        this.iban = ibanCountry + ibanNumber;
    }

    public Date getGueltigBis() {
        return gueltigBis;
    }

    public void setGueltigBis(Date gueltigBis) {
        this.gueltigBis = gueltigBis;
    }
    
    @Override
    public String toString(){
        
        return "AbstractZahlungsmittel{Kunde=["+ kunde.toString() +"], besitzer= "+getBesitzer()+ ", "
                + ", IBAN="+ iban + ", GueltigBis="+ gueltigBis +", id= "+ getId() + "}";
        
    }
    
}
