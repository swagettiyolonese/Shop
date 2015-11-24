/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.shop.zahlungsmittelverwaltung.domain;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static de.shop.util.Constants.EMAIL_PATTERN;

/**
 *
 * @author Philipp Diemert, 48616, philipp.diemert@outlook.com
 */



public class PaypalZahlung extends AbstractZahlungsmittel{
    
    private static final int EMAIL_LENGTH_MAX = 128;
    
    //Mailadresse
    @NotNull(message = "{paypal.paypalmailadresse.notNull}")
    @Pattern(regexp=EMAIL_PATTERN, message = "{paypalzahlung.email.pattern}")
    @Size(max = EMAIL_LENGTH_MAX, message = "{kunde.email.length}")
    String paypalmailadresse;
    //Namen
    
}
