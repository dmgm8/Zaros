/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.config;

import java.text.ParseException;
import javax.swing.JFormattedTextField;
import net.runelite.client.config.Units;

final class UnitFormatter
extends JFormattedTextField.AbstractFormatter {
    private final String units;

    UnitFormatter(Units units) {
        this.units = units.value();
    }

    @Override
    public Object stringToValue(String text) throws ParseException {
        String trimmedText = text.endsWith(this.units) ? text.substring(0, text.length() - this.units.length()) : text;
        try {
            return Integer.valueOf(trimmedText);
        }
        catch (NumberFormatException e) {
            throw new ParseException(trimmedText + " is not an integer.", 0);
        }
    }

    @Override
    public String valueToString(Object value) {
        return value + this.units;
    }
}

