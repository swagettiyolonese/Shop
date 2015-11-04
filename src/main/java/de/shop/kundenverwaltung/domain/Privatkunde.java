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

package de.shop.kundenverwaltung.domain;

import java.util.Set;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author <a href="mailto:Juergen.Zimmermann@HS-Karlsruhe.de">J&uuml;rgen Zimmermann</a>
 */
@XmlRootElement
//MOXy statt Jackson
//@XmlDiscriminatorValue(AbstractKunde.FIRMENKUNDE)
public class Privatkunde extends AbstractKunde {
    private static final long serialVersionUID = -3177911520687689458L;
    
    private Set<HobbyType> hobbies;

    public Set<HobbyType> getHobbies() {
        return hobbies;
    }
    public void setHobbies(Set<HobbyType> hobbies) {
        this.hobbies = hobbies;
    }
    @Override
    public String toString() {
        return "Privatkunde {" + super.toString() + ", hobbies=" + hobbies + '}';
    }
}
