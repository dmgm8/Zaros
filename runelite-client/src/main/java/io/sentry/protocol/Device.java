/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$Internal
 *  org.jetbrains.annotations.ApiStatus$ScheduledForRemoval
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.TestOnly
 */
package io.sentry.protocol;

import io.sentry.IUnknownPropertiesConsumer;
import io.sentry.util.CollectionUtils;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;

public final class Device
implements IUnknownPropertiesConsumer,
Cloneable {
    public static final String TYPE = "device";
    private String name;
    private String manufacturer;
    private String brand;
    private String family;
    private String model;
    private String modelId;
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    private String arch;
    private String[] archs;
    private Float batteryLevel;
    private Boolean charging;
    private Boolean online;
    private DeviceOrientation orientation;
    private Boolean simulator;
    private Long memorySize;
    private Long freeMemory;
    private Long usableMemory;
    private Boolean lowMemory;
    private Long storageSize;
    private Long freeStorage;
    private Long externalStorageSize;
    private Long externalFreeStorage;
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    private String screenResolution;
    private Integer screenWidthPixels;
    private Integer screenHeightPixels;
    private Float screenDensity;
    private Integer screenDpi;
    private Date bootTime;
    private TimeZone timezone;
    private String id;
    private String language;
    private String connectionType;
    private Float batteryTemperature;
    private Map<String, Object> unknown;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManufacturer() {
        return this.manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getBrand() {
        return this.brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getFamily() {
        return this.family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getModel() {
        return this.model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getModelId() {
        return this.modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    @Deprecated
    @ApiStatus.ScheduledForRemoval
    public String getArch() {
        return this.arch;
    }

    @Deprecated
    @ApiStatus.ScheduledForRemoval
    public void setArch(String arch) {
        this.arch = arch;
    }

    public Float getBatteryLevel() {
        return this.batteryLevel;
    }

    public void setBatteryLevel(Float batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public Boolean isCharging() {
        return this.charging;
    }

    public void setCharging(Boolean charging) {
        this.charging = charging;
    }

    public Boolean isOnline() {
        return this.online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public DeviceOrientation getOrientation() {
        return this.orientation;
    }

    public void setOrientation(DeviceOrientation orientation) {
        this.orientation = orientation;
    }

    public Boolean isSimulator() {
        return this.simulator;
    }

    public void setSimulator(Boolean simulator) {
        this.simulator = simulator;
    }

    public Long getMemorySize() {
        return this.memorySize;
    }

    public void setMemorySize(Long memorySize) {
        this.memorySize = memorySize;
    }

    public Long getFreeMemory() {
        return this.freeMemory;
    }

    public void setFreeMemory(Long freeMemory) {
        this.freeMemory = freeMemory;
    }

    public Long getUsableMemory() {
        return this.usableMemory;
    }

    public void setUsableMemory(Long usableMemory) {
        this.usableMemory = usableMemory;
    }

    public Boolean isLowMemory() {
        return this.lowMemory;
    }

    public void setLowMemory(Boolean lowMemory) {
        this.lowMemory = lowMemory;
    }

    public Long getStorageSize() {
        return this.storageSize;
    }

    public void setStorageSize(Long storageSize) {
        this.storageSize = storageSize;
    }

    public Long getFreeStorage() {
        return this.freeStorage;
    }

    public void setFreeStorage(Long freeStorage) {
        this.freeStorage = freeStorage;
    }

    public Long getExternalStorageSize() {
        return this.externalStorageSize;
    }

    public void setExternalStorageSize(Long externalStorageSize) {
        this.externalStorageSize = externalStorageSize;
    }

    public Long getExternalFreeStorage() {
        return this.externalFreeStorage;
    }

    public void setExternalFreeStorage(Long externalFreeStorage) {
        this.externalFreeStorage = externalFreeStorage;
    }

    @Deprecated
    @ApiStatus.ScheduledForRemoval
    public String getScreenResolution() {
        return this.screenResolution;
    }

    @Deprecated
    @ApiStatus.ScheduledForRemoval
    public void setScreenResolution(String screenResolution) {
        this.screenResolution = screenResolution;
    }

    public Float getScreenDensity() {
        return this.screenDensity;
    }

    public void setScreenDensity(Float screenDensity) {
        this.screenDensity = screenDensity;
    }

    public Integer getScreenDpi() {
        return this.screenDpi;
    }

    public void setScreenDpi(Integer screenDpi) {
        this.screenDpi = screenDpi;
    }

    public Date getBootTime() {
        Date bootTimeRef = this.bootTime;
        return bootTimeRef != null ? (Date)bootTimeRef.clone() : null;
    }

    public void setBootTime(Date bootTime) {
        this.bootTime = bootTime;
    }

    public TimeZone getTimezone() {
        return this.timezone;
    }

    public void setTimezone(TimeZone timezone) {
        this.timezone = timezone;
    }

    public String[] getArchs() {
        return this.archs;
    }

    public void setArchs(String[] archs) {
        this.archs = archs;
    }

    public Integer getScreenWidthPixels() {
        return this.screenWidthPixels;
    }

    public void setScreenWidthPixels(Integer screenWidthPixels) {
        this.screenWidthPixels = screenWidthPixels;
    }

    public Integer getScreenHeightPixels() {
        return this.screenHeightPixels;
    }

    public void setScreenHeightPixels(Integer screenHeightPixels) {
        this.screenHeightPixels = screenHeightPixels;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLanguage() {
        return this.language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getConnectionType() {
        return this.connectionType;
    }

    public void setConnectionType(String connectionType) {
        this.connectionType = connectionType;
    }

    public Float getBatteryTemperature() {
        return this.batteryTemperature;
    }

    public void setBatteryTemperature(Float batteryTemperature) {
        this.batteryTemperature = batteryTemperature;
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
    public Device clone() throws CloneNotSupportedException {
        Device clone = (Device)super.clone();
        String[] archsRef = this.archs;
        clone.archs = archsRef != null ? (String[])this.archs.clone() : null;
        TimeZone timezoneRef = this.timezone;
        clone.timezone = timezoneRef != null ? (TimeZone)this.timezone.clone() : null;
        clone.unknown = CollectionUtils.shallowCopy(this.unknown);
        return clone;
    }

    public static enum DeviceOrientation {
        PORTRAIT,
        LANDSCAPE;

    }
}

