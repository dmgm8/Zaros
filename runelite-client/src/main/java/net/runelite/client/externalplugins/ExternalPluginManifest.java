/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.hash.Hashing
 *  com.google.common.io.Files
 *  javax.annotation.Nullable
 */
package net.runelite.client.externalplugins;

import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import javax.annotation.Nullable;
import net.runelite.client.RuneLite;

public class ExternalPluginManifest {
    private String internalName;
    private String commit;
    private String hash;
    private int size;
    private String[] plugins;
    private String displayName;
    private String version;
    private String author;
    @Nullable
    private String description;
    @Nullable
    private String warning;
    @Nullable
    private String[] tags;
    private URL support;
    private boolean hasIcon;

    public boolean hasIcon() {
        return this.hasIcon;
    }

    File getJarFile() {
        return new File(RuneLite.PLUGINS_DIR, this.internalName + this.commit + ".jar");
    }

    boolean isValid() {
        File file = this.getJarFile();
        try {
            String hash;
            if (file.exists() && this.hash.equals(hash = Files.asByteSource((File)file).hash(Hashing.sha256()).toString())) {
                return true;
            }
        }
        catch (IOException iOException) {
            // empty catch block
        }
        return false;
    }

    public String getInternalName() {
        return this.internalName;
    }

    public String getCommit() {
        return this.commit;
    }

    public String getHash() {
        return this.hash;
    }

    public int getSize() {
        return this.size;
    }

    public String[] getPlugins() {
        return this.plugins;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public String getVersion() {
        return this.version;
    }

    public String getAuthor() {
        return this.author;
    }

    @Nullable
    public String getDescription() {
        return this.description;
    }

    @Nullable
    public String getWarning() {
        return this.warning;
    }

    @Nullable
    public String[] getTags() {
        return this.tags;
    }

    public URL getSupport() {
        return this.support;
    }

    public boolean isHasIcon() {
        return this.hasIcon;
    }

    public void setInternalName(String internalName) {
        this.internalName = internalName;
    }

    public void setCommit(String commit) {
        this.commit = commit;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setPlugins(String[] plugins) {
        this.plugins = plugins;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    public void setWarning(@Nullable String warning) {
        this.warning = warning;
    }

    public void setTags(@Nullable String[] tags) {
        this.tags = tags;
    }

    public void setSupport(URL support) {
        this.support = support;
    }

    public void setHasIcon(boolean hasIcon) {
        this.hasIcon = hasIcon;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ExternalPluginManifest)) {
            return false;
        }
        ExternalPluginManifest other = (ExternalPluginManifest)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getSize() != other.getSize()) {
            return false;
        }
        if (this.isHasIcon() != other.isHasIcon()) {
            return false;
        }
        String this$internalName = this.getInternalName();
        String other$internalName = other.getInternalName();
        if (this$internalName == null ? other$internalName != null : !this$internalName.equals(other$internalName)) {
            return false;
        }
        String this$commit = this.getCommit();
        String other$commit = other.getCommit();
        if (this$commit == null ? other$commit != null : !this$commit.equals(other$commit)) {
            return false;
        }
        String this$hash = this.getHash();
        String other$hash = other.getHash();
        if (this$hash == null ? other$hash != null : !this$hash.equals(other$hash)) {
            return false;
        }
        if (!Arrays.deepEquals(this.getPlugins(), other.getPlugins())) {
            return false;
        }
        String this$displayName = this.getDisplayName();
        String other$displayName = other.getDisplayName();
        if (this$displayName == null ? other$displayName != null : !this$displayName.equals(other$displayName)) {
            return false;
        }
        String this$version = this.getVersion();
        String other$version = other.getVersion();
        if (this$version == null ? other$version != null : !this$version.equals(other$version)) {
            return false;
        }
        String this$author = this.getAuthor();
        String other$author = other.getAuthor();
        if (this$author == null ? other$author != null : !this$author.equals(other$author)) {
            return false;
        }
        String this$description = this.getDescription();
        String other$description = other.getDescription();
        if (this$description == null ? other$description != null : !this$description.equals(other$description)) {
            return false;
        }
        String this$warning = this.getWarning();
        String other$warning = other.getWarning();
        if (this$warning == null ? other$warning != null : !this$warning.equals(other$warning)) {
            return false;
        }
        return Arrays.deepEquals(this.getTags(), other.getTags());
    }

    protected boolean canEqual(Object other) {
        return other instanceof ExternalPluginManifest;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getSize();
        result = result * 59 + (this.isHasIcon() ? 79 : 97);
        String $internalName = this.getInternalName();
        result = result * 59 + ($internalName == null ? 43 : $internalName.hashCode());
        String $commit = this.getCommit();
        result = result * 59 + ($commit == null ? 43 : $commit.hashCode());
        String $hash = this.getHash();
        result = result * 59 + ($hash == null ? 43 : $hash.hashCode());
        result = result * 59 + Arrays.deepHashCode(this.getPlugins());
        String $displayName = this.getDisplayName();
        result = result * 59 + ($displayName == null ? 43 : $displayName.hashCode());
        String $version = this.getVersion();
        result = result * 59 + ($version == null ? 43 : $version.hashCode());
        String $author = this.getAuthor();
        result = result * 59 + ($author == null ? 43 : $author.hashCode());
        String $description = this.getDescription();
        result = result * 59 + ($description == null ? 43 : $description.hashCode());
        String $warning = this.getWarning();
        result = result * 59 + ($warning == null ? 43 : $warning.hashCode());
        result = result * 59 + Arrays.deepHashCode(this.getTags());
        return result;
    }

    public String toString() {
        return "ExternalPluginManifest(internalName=" + this.getInternalName() + ", commit=" + this.getCommit() + ", hash=" + this.getHash() + ", size=" + this.getSize() + ", plugins=" + Arrays.deepToString(this.getPlugins()) + ", displayName=" + this.getDisplayName() + ", version=" + this.getVersion() + ", author=" + this.getAuthor() + ", description=" + this.getDescription() + ", warning=" + this.getWarning() + ", tags=" + Arrays.deepToString(this.getTags()) + ", support=" + this.getSupport() + ", hasIcon=" + this.isHasIcon() + ")";
    }
}

