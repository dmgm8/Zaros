/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$Internal
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.TestOnly
 */
package io.sentry.protocol;

import io.sentry.IUnknownPropertiesConsumer;
import io.sentry.util.CollectionUtils;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;

public final class Gpu
implements IUnknownPropertiesConsumer,
Cloneable {
    public static final String TYPE = "gpu";
    private String name;
    private Integer id;
    private Integer vendorId;
    private String vendorName;
    private Integer memorySize;
    private String apiType;
    private Boolean multiThreadedRendering;
    private String version;
    private String npotSupport;
    private Map<String, Object> unknown;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getVendorId() {
        return this.vendorId;
    }

    public void setVendorId(Integer vendorId) {
        this.vendorId = vendorId;
    }

    public String getVendorName() {
        return this.vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public Integer getMemorySize() {
        return this.memorySize;
    }

    public void setMemorySize(Integer memorySize) {
        this.memorySize = memorySize;
    }

    public String getApiType() {
        return this.apiType;
    }

    public void setApiType(String apiType) {
        this.apiType = apiType;
    }

    public Boolean isMultiThreadedRendering() {
        return this.multiThreadedRendering;
    }

    public void setMultiThreadedRendering(Boolean multiThreadedRendering) {
        this.multiThreadedRendering = multiThreadedRendering;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getNpotSupport() {
        return this.npotSupport;
    }

    public void setNpotSupport(String npotSupport) {
        this.npotSupport = npotSupport;
    }

    @TestOnly
    Map<String, Object> getUnknown() {
        return this.unknown;
    }

    @Override
    @ApiStatus.Internal
    public void acceptUnknownProperties(Map<String, Object> unknown) {
        this.unknown = new ConcurrentHashMap<String, Object>(unknown);
    }

    @NotNull
    public Gpu clone() throws CloneNotSupportedException {
        Gpu clone = (Gpu)super.clone();
        clone.unknown = CollectionUtils.shallowCopy(this.unknown);
        return clone;
    }
}

