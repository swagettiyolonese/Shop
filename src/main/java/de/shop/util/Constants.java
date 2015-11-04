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

package de.shop.util;

/**
 * @author <a href="mailto:Juergen.Zimmermann@HS-Karlsruhe.de">J&uuml;rgen Zimmermann</a>
 */
public final class Constants {
    public static final String REST_PATH = "/rest";
    
    public static final String UUID_PATTERN = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}";
    
    public static final int HASH_PRIME = 31;

    // Bean Validation
    public static final String EMAIL_PATTERN = "[\\w.%-]+@[\\w.%-]+\\.[A-Za-z]{2,4}";
    
    // Header-Links
    public static final String SELF_LINK = "self";
    public static final String ADD_LINK = "add";
    public static final String UPDATE_LINK = "update";
    public static final String REMOVE_LINK = "remove";
    public static final String FIRST_LINK = "first";
    public static final String LAST_LINK = "last";

    private Constants() {
    }
}
