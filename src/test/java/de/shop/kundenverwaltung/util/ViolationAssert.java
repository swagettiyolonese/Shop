/*
 * Copyright (C) 2015 Juergen Zimmermann, Hochschule Karlsruhe
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
package de.shop.kundenverwaltung.util;

import java.util.Collection;
import java.util.Optional;
import org.assertj.core.api.AbstractAssert;
import org.jboss.resteasy.api.validation.ResteasyConstraintViolation;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="mailto:Juergen.Zimmermann@HS-Karlsruhe.de">J&uuml;rgen Zimmermann</a>
 */
public class ViolationAssert extends AbstractAssert<ViolationAssert, Collection<ResteasyConstraintViolation>> {
    private ViolationAssert(Collection<ResteasyConstraintViolation> actual) {
        super(actual, ViolationAssert.class);
    }
    
    public static ViolationAssert assertThatViolations(Collection<ResteasyConstraintViolation> actual) {
        return new ViolationAssert(actual);
    }

    public ViolationAssert haveAtLeastOneViolation() {
        assertThat(actual)
            .overridingErrorMessage("Es muss mindestens 1 Verletzung der Bean Validation geben")
            .isNotEmpty();
        return this;
    }
    
    public ViolationAssert haveNachnameSizeViolation(String nachname) {
        final String expectedMsg = "A lastname must have at least 2 and may only have up to 32 characters.";
        final Optional<ResteasyConstraintViolation> violation = actual.stream()
                                                                      .filter(v -> v.getMessage().equals(expectedMsg))
                                                                      .findAny();
        assertThat(violation)
            .overridingErrorMessage("Der Meldung bei der Verletzung des Nachnamens muss <%s> sein", expectedMsg)
            .isPresent();
        assertThat(violation.get().getValue())
            .overridingErrorMessage("Der Wert bei der Verletzung des Nachnamens muss %s sein, ist aber %s", nachname, violation.get().getValue())
            .isEqualTo(nachname);
        
        return this;
    }
    
    public ViolationAssert haveNachnamePatternViolation(String nachname) {
        final String expectedPrefix = "A lastname must start with exactly";
        final Optional<ResteasyConstraintViolation> violation = actual.stream()
                                                                      .filter(v -> v.getMessage().startsWith(expectedPrefix))
                                                                      .findAny();
        assertThat(violation)
            .overridingErrorMessage("Der Meldung bei der Verletzung des Nachnamens muss mit <%s> beginnen", expectedPrefix)
            .isPresent();
        assertThat(violation.get().getValue())
            .overridingErrorMessage("Der Wert bei der Verletzung des Nachnamens muss %s sein, ist aber %s", nachname, violation.get().getValue())
            .isEqualTo(nachname);
        
        return this;
    }
    
    public ViolationAssert haveEmailViolation(String email) {
        final String expectedMsg = "The email address " + email + " is invalid.";
        final Optional<ResteasyConstraintViolation> violation = actual.stream()
                                                                      .filter(v -> v.getMessage().equals(expectedMsg))
                                                                      .findAny();
        assertThat(violation)
            .overridingErrorMessage("Der Meldung bei der Verletzung der Email muss <%s> sein", expectedMsg)
            .isPresent();
        assertThat(violation.get().getValue())
            .overridingErrorMessage("Der Wert bei der Verletzung der Email muss %s sein, ist aber %s", email, violation.get().getValue())
            .isEqualTo(email);
        
        return this;
    }
    
    public ViolationAssert havePlzViolation(String plz) {
        final String expectedMsg = "The ZIP code " + plz + " doesn't have 5 digits.";
        final Optional<ResteasyConstraintViolation> violation = actual.stream()
                                                                      .filter(v -> v.getMessage().equals(expectedMsg))
                                                                      .findAny();
        assertThat(violation)
            .overridingErrorMessage("Der Meldung bei der Verletzung der Plz muss <%s> sein", expectedMsg)
            .isPresent();
        assertThat(violation.get().getValue())
            .overridingErrorMessage("Der Wert bei der Verletzung der Plz muss %s sein, ist aber %s", plz, violation.get().getValue())
            .isEqualTo(plz);
        
        return this;
    }
}
