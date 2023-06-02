/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$Internal
 */
package io.sentry.protocol;

import io.sentry.IUnknownPropertiesConsumer;
import java.util.Map;
import org.jetbrains.annotations.ApiStatus;

public final class DebugImage
implements IUnknownPropertiesConsumer {
    private String uuid;
    private String type;
    private String debugId;
    private String debugFile;
    private String codeFile;
    private String imageAddr;
    private Long imageSize;
    private String arch;
    private String codeId;
    private Map<String, Object> unknown;

    public String getUuid() {
        return this.uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDebugId() {
        return this.debugId;
    }

    public void setDebugId(String debugId) {
        this.debugId = debugId;
    }

    public String getDebugFile() {
        return this.debugFile;
    }

    public void setDebugFile(String debugFile) {
        this.debugFile = debugFile;
    }

    public String getCodeFile() {
        return this.codeFile;
    }

    public void setCodeFile(String codeFile) {
        this.codeFile = codeFile;
    }

    public String getImageAddr() {
        return this.imageAddr;
    }

    public void setImageAddr(String imageAddr) {
        this.imageAddr = imageAddr;
    }

    public Long getImageSize() {
        return this.imageSize;
    }

    public void setImageSize(Long imageSize) {
        this.imageSize = imageSize;
    }

    public String getArch() {
        return this.arch;
    }

    public void setArch(String arch) {
        this.arch = arch;
    }

    public String getCodeId() {
        return this.codeId;
    }

    public void setCodeId(String codeId) {
        this.codeId = codeId;
    }

    @Override
    @ApiStatus.Internal
    public void acceptUnknownProperties(Map<String, Object> unknown) {
        this.unknown = unknown;
    }
}

