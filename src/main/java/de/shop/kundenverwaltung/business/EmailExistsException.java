/*
 * Copyright (C) 2013-2015 Juergen Zimmermann, Hochschule Karlsruhe
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

/**
 * @author <a href="mailto:Juergen.Zimmermann@HS-Karlsruhe.de">J&uuml;rgen Zimmermann</a>
 */
public class EmailExistsException extends AbstractKundenverwaltungException {
    private static final long serialVersionUID = 4867667611097919943L;
    
    private static final String MESSAGE_KEY = "kunde.emailExists";
    private final String email;
    
    public EmailExistsException(String email) {
        super("Die Email-Adresse " + email + " existiert bereits");
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getMessageKey() {
        return MESSAGE_KEY;
    }
}
