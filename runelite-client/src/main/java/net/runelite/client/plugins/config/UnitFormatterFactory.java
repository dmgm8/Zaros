/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.config;

import java.util.HashMap;
import java.util.Map;
import javax.swing.JFormattedTextField;
import net.runelite.client.config.Units;
import net.runelite.client.plugins.config.UnitFormatter;

final class UnitFormatterFactory
extends JFormattedTextField.AbstractFormatterFactory {
    private final Units units;
    private final Map<JFormattedTextField, JFormattedTextField.AbstractFormatter> formatters = new HashMap<JFormattedTextField, JFormattedTextField.AbstractFormatter>();

    @Override
    public JFormattedTextField.AbstractFormatter getFormatter(JFormattedTextField tf) {
        return this.formatters.computeIfAbsent(tf, key -> new UnitFormatter(this.units));
    }

    public UnitFormatterFactory(Units units) {
        this.units = units;
    }
}

