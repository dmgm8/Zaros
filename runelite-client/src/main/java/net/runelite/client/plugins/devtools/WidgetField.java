/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.runelite.api.widgets.Widget
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.slf4j.helpers.MessageFormatter
 */
package net.runelite.client.plugins.devtools;

import java.util.function.BiConsumer;
import java.util.function.Function;
import net.runelite.api.widgets.Widget;
import net.runelite.client.plugins.devtools.WidgetInspector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;

public class WidgetField<T> {
    private static final Logger log = LoggerFactory.getLogger(WidgetField.class);
    private final String name;
    private final Function<Widget, T> getter;
    private final BiConsumer<Widget, T> setter;
    private final Class<T> type;

    WidgetField(String name, Function<Widget, T> getter) {
        this(name, getter, null, null);
    }

    WidgetField(String name, Function<Widget, T> getter, BiConsumer<Widget, T> setter, Class<T> type) {
        this.name = name;
        this.getter = getter;
        this.setter = setter;
        this.type = type;
    }

    Object getValue(Widget widget) {
        T value = this.getter.apply(widget);
        if (value instanceof Boolean || value instanceof Number || value instanceof String) {
            return value;
        }
        if (value instanceof Widget) {
            return WidgetInspector.getWidgetIdentifier((Widget)value);
        }
        return MessageFormatter.format((String)"{}", value).getMessage();
    }

    void setValue(Widget widget, Object inValue) {
        Object value = null;
        if ("null".equals(inValue)) {
            value = null;
        }
        if (this.type.isAssignableFrom(inValue.getClass())) {
            value = inValue;
        } else if (this.type == Boolean.class) {
            value = Boolean.valueOf((String)inValue);
        } else if (this.type == Integer.class) {
            value = Integer.valueOf((String)inValue);
        } else {
            log.warn("Type {} is not supported for editing", this.type);
        }
        this.setter.accept(widget, (Widget)value);
    }

    boolean isSettable() {
        return this.setter != null;
    }

    public String getName() {
        return this.name;
    }
}

