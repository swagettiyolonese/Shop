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
import de.shop.util.mail.AbsenderEmail;
import de.shop.util.mail.AbsenderName;
import java.io.UnsupportedEncodingException;
import java.lang.invoke.MethodHandles;
import java.util.logging.Logger;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.ObservesAsync;
import javax.inject.Inject;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.jboss.weld.experimental.Priority;

import static java.util.logging.Level.FINER;
import static java.util.logging.Level.FINEST;
import static java.util.logging.Level.SEVERE;
import static javax.interceptor.Interceptor.Priority.APPLICATION;

/**
 * @author <a href="mailto:Juergen.Zimmermann@HS-Karlsruhe.de">J&uuml;rgen Zimmermann</a>
 */
@Dependent
public class BestellungObserver {
    private static final Logger LOGGER = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());
    
    private final Session session;
    private final String absenderEmail;
    private final String absenderName;

    @Inject
    BestellungObserver(Session session, @AbsenderEmail String absenderEmail, @AbsenderName String absenderName) {
        super();
        this.session = session;
        this.absenderEmail = absenderEmail;
        this.absenderName = absenderName;

        if (absenderEmail == null) {
            // Warning oder Fehler?
            LOGGER.warning("Der Absender fuer Bestellung-Emails ist nicht gesetzt.");
            return;
        }
        LOGGER.info("Absender fuer Bestellung-Emails: " + absenderName + " <" + absenderEmail + ">");
    }
    
    private void onCreateBestellung(@ObservesAsync
                                    @Priority(APPLICATION + 100)
                                    @NeueBestellung
                                    Bestellung bestellung) {
        if (LOGGER.isLoggable(FINER)) {
            LOGGER.finer("onCreateBestellung BEGINN: " + bestellung);
        }
        
        final AbstractKunde kunde = bestellung.getKunde();
        final String empfaengerEmail = kunde.getEmail();
        if (absenderEmail == null || empfaengerEmail == null) {
            return;
        }
        final String empfaengerName = kunde.getNachname();
        
        final MimeMessage message = new MimeMessage(session);

        try {
            // Absender setzen
            final InternetAddress absenderObj = new InternetAddress(absenderEmail, absenderName);
            message.setFrom(absenderObj);
            
            // Empfaenger setzen
            final InternetAddress empfaenger = new InternetAddress(empfaengerEmail, empfaengerName);
            // RecipientType: TO, CC, BCC
            message.setRecipient(RecipientType.TO, empfaenger);

            // Subject setzen
            message.setSubject("Neue Bestellung Nr. " + bestellung.getId());
            
            // Text setzen mit MIME Type "text/plain"
            final String text = "<h3>Neue Bestellung Nr. <b>" + bestellung.getId() + "</b></h3>";
            if (LOGGER.isLoggable(FINEST)) {
                LOGGER.finest(text);
            }
            message.setContent(text, "text/html;charset=iso-8859-1");
            Transport.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {
            LOGGER.log(SEVERE, e.getMessage(), e);
            return;
        }
        
        if (LOGGER.isLoggable(FINER)) {
            LOGGER.finer("onCreateBestellung ENDE");
        }
    }
}
