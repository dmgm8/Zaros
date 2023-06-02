/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.gson.annotations.SerializedName
 *  org.jetbrains.annotations.ApiStatus$Internal
 */
package io.sentry.protocol;

import com.google.gson.annotations.SerializedName;
import io.sentry.IUnknownPropertiesConsumer;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.ApiStatus;

public final class SentryStackFrame
implements IUnknownPropertiesConsumer {
    private List<String> preContext;
    private List<String> postContext;
    private Map<String, String> vars;
    private List<Integer> framesOmitted;
    private String filename;
    private String function;
    private String module;
    private Integer lineno;
    private Integer colno;
    private String absPath;
    private String contextLine;
    private Boolean inApp;
    @SerializedName(value="package")
    private String _package;
    @SerializedName(value="native")
    private Boolean _native;
    private String platform;
    private String imageAddr;
    private String symbolAddr;
    private String instructionAddr;
    private Map<String, Object> unknown;
    private String rawFunction;

    public List<String> getPreContext() {
        return this.preContext;
    }

    public void setPreContext(List<String> preContext) {
        this.preContext = preContext;
    }

    public List<String> getPostContext() {
        return this.postContext;
    }

    public void setPostContext(List<String> postContext) {
        this.postContext = postContext;
    }

    public Map<String, String> getVars() {
        return this.vars;
    }

    public void setVars(Map<String, String> vars) {
        this.vars = vars;
    }

    public List<Integer> getFramesOmitted() {
        return this.framesOmitted;
    }

    public void setFramesOmitted(List<Integer> framesOmitted) {
        this.framesOmitted = framesOmitted;
    }

    public String getFilename() {
        return this.filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFunction() {
        return this.function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getModule() {
        return this.module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public Integer getLineno() {
        return this.lineno;
    }

    public void setLineno(Integer lineno) {
        this.lineno = lineno;
    }

    public Integer getColno() {
        return this.colno;
    }

    public void setColno(Integer colno) {
        this.colno = colno;
    }

    public String getAbsPath() {
        return this.absPath;
    }

    public void setAbsPath(String absPath) {
        this.absPath = absPath;
    }

    public String getContextLine() {
        return this.contextLine;
    }

    public void setContextLine(String contextLine) {
        this.contextLine = contextLine;
    }

    public Boolean isInApp() {
        return this.inApp;
    }

    public void setInApp(Boolean inApp) {
        this.inApp = inApp;
    }

    public String getPackage() {
        return this._package;
    }

    public void setPackage(String _package) {
        this._package = _package;
    }

    public String getPlatform() {
        return this.platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getImageAddr() {
        return this.imageAddr;
    }

    public void setImageAddr(String imageAddr) {
        this.imageAddr = imageAddr;
    }

    public String getSymbolAddr() {
        return this.symbolAddr;
    }

    public void setSymbolAddr(String symbolAddr) {
        this.symbolAddr = symbolAddr;
    }

    public String getInstructionAddr() {
        return this.instructionAddr;
    }

    public void setInstructionAddr(String instructionAddr) {
        this.instructionAddr = instructionAddr;
    }

    public Boolean isNative() {
        return this._native;
    }

    public void setNative(Boolean _native) {
        this._native = _native;
    }

    @Override
    @ApiStatus.Internal
    public void acceptUnknownProperties(Map<String, Object> unknown) {
        this.unknown = unknown;
    }

    public String getRawFunction() {
        return this.rawFunction;
    }

    public void setRawFunction(String rawFunction) {
        this.rawFunction = rawFunction;
    }
}

