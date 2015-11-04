/*
 * Copyright (C) 2014 Juergen Zimmermann, Hochschule Karlsruhe
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
 *
 * @author <a href="mailto:Juergen.Zimmermann@HS-Karlsruhe.de">J&uuml;rgen Zimmermann</a>
 */
public class ShopRuntimeException extends AbstractShopException {
    private static final long serialVersionUID = 7953020293725526521L;
    
    public ShopRuntimeException(String msg) {
        super(msg);
    }

    public ShopRuntimeException(Throwable t) {
        super(t);
    }
    public ShopRuntimeException(String msg, Throwable t) {
        super(msg, t);
    }
    
    @Override
    public String getMessageKey() {
        throw new UnsupportedOperationException("getMessageKey() ist fuer ShopRuntimeException nicht implementiert.");
    }
    
}
