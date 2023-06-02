/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package io.sentry.protocol;

import io.sentry.protocol.App;
import io.sentry.protocol.Browser;
import io.sentry.protocol.Device;
import io.sentry.protocol.Gpu;
import io.sentry.protocol.OperatingSystem;
import io.sentry.protocol.SentryRuntime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.NotNull;

public final class Contexts
extends ConcurrentHashMap<String, Object>
implements Cloneable {
    private static final long serialVersionUID = 252445813254943011L;

    private <T> T toContextType(String key, Class<T> clazz) {
        Object item = this.get(key);
        return clazz.isInstance(item) ? (T)clazz.cast(item) : null;
    }

    public App getApp() {
        return this.toContextType("app", App.class);
    }

    public void setApp(App app) {
        this.put("app", app);
    }

    public Browser getBrowser() {
        return this.toContextType("browser", Browser.class);
    }

    public void setBrowser(Browser browser) {
        this.put("browser", browser);
    }

    public Device getDevice() {
        return this.toContextType("device", Device.class);
    }

    public void setDevice(Device device) {
        this.put("device", device);
    }

    public OperatingSystem getOperatingSystem() {
        return this.toContextType("os", OperatingSystem.class);
    }

    public void setOperatingSystem(OperatingSystem operatingSystem) {
        this.put("os", operatingSystem);
    }

    public SentryRuntime getRuntime() {
        return this.toContextType("runtime", SentryRuntime.class);
    }

    public void setRuntime(SentryRuntime runtime) {
        this.put("runtime", runtime);
    }

    public Gpu getGpu() {
        return this.toContextType("gpu", Gpu.class);
    }

    public void setGpu(Gpu gpu) {
        this.put("gpu", gpu);
    }

    @Override
    @NotNull
    public Contexts clone() throws CloneNotSupportedException {
        Contexts clone = new Contexts();
        for (Entry entry : this.entrySet()) {
            if (entry == null) continue;
            Object value = entry.getValue();
            if ("app".equals(entry.getKey()) && value instanceof App) {
                clone.setApp(((App)value).clone());
                continue;
            }
            if ("browser".equals(entry.getKey()) && value instanceof Browser) {
                clone.setBrowser(((Browser)value).clone());
                continue;
            }
            if ("device".equals(entry.getKey()) && value instanceof Device) {
                clone.setDevice(((Device)value).clone());
                continue;
            }
            if ("os".equals(entry.getKey()) && value instanceof OperatingSystem) {
                clone.setOperatingSystem(((OperatingSystem)value).clone());
                continue;
            }
            if ("runtime".equals(entry.getKey()) && value instanceof SentryRuntime) {
                clone.setRuntime(((SentryRuntime)value).clone());
                continue;
            }
            if ("gpu".equals(entry.getKey()) && value instanceof Gpu) {
                clone.setGpu(((Gpu)value).clone());
                continue;
            }
            clone.put((String)entry.getKey(), value);
        }
        return clone;
    }
}

