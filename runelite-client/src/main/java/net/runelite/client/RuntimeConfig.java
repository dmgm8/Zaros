/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Strings
 */
package net.runelite.client;

import com.google.common.base.Strings;

import java.util.*;
import javax.swing.SwingUtilities;
import net.runelite.client.RuneLiteProperties;
import net.runelite.client.ui.FatalErrorDialog;
import net.runelite.client.util.LinkBrowser;

public class RuntimeConfig {
    private Map<String, ?> props = Collections.emptyMap();
    private Map<String, String> sysProps = Collections.emptyMap();
    private String outageMessage;
    private Map<String, String> outageLinks;
    private Set<Integer> ignoreDeadNpcs;
    private Set<Integer> forceDeadNpcs;
    private Set<Integer> resetDeadOnChangeNpcs;
    private Set<Integer> forceDeadAnimations;
    private Set<Integer> nonAttackNpcs;
    private Map<String, List<String>> hiscoreMapping;

    public boolean showOutageMessage() {
        if (Strings.isNullOrEmpty(this.getOutageMessage())) {
            return false;
        }
        SwingUtilities.invokeLater(() -> {
            FatalErrorDialog fed = new FatalErrorDialog(this.getOutageMessage());
            if (this.getOutageLinks() == null) {
                fed.addButton("OSRS Twitter", () -> LinkBrowser.browse(RuneLiteProperties.getOSRSTwitterLink()));
            } else {
                for (Map.Entry<String, String> e : this.getOutageLinks().entrySet()) {
                    fed.addButton(e.getKey(), () -> LinkBrowser.browse(e.getValue()));
                }
            }
            fed.open();
        });
        return true;
    }

    public Map<String, ?> getProps() {
        return this.props;
    }

    public Map<String, String> getSysProps() {
        return this.sysProps;
    }

    public String getOutageMessage() {
        return this.outageMessage;
    }

    public Map<String, String> getOutageLinks() {
        return this.outageLinks;
    }

    public Set<Integer> getIgnoreDeadNpcs() {
        return this.ignoreDeadNpcs;
    }

    public Set<Integer> getForceDeadNpcs() {
        return this.forceDeadNpcs;
    }

    public Set<Integer> getResetDeadOnChangeNpcs() {
        return this.resetDeadOnChangeNpcs;
    }

    public Set<Integer> getForceDeadAnimations() {
        return this.forceDeadAnimations;
    }

    public Set<Integer> getNonAttackNpcs() {
        return this.nonAttackNpcs;
    }

    public Map<String, List<String>> getHiscoreMapping() {
        return this.hiscoreMapping;
    }

    public void setProps(Map<String, ?> props) {
        this.props = props;
    }

    public void setSysProps(Map<String, String> sysProps) {
        this.sysProps = sysProps;
    }

    public void setOutageMessage(String outageMessage) {
        this.outageMessage = outageMessage;
    }

    public void setOutageLinks(Map<String, String> outageLinks) {
        this.outageLinks = outageLinks;
    }

    public void setIgnoreDeadNpcs(Set<Integer> ignoreDeadNpcs) {
        this.ignoreDeadNpcs = ignoreDeadNpcs;
    }

    public void setForceDeadNpcs(Set<Integer> forceDeadNpcs) {
        this.forceDeadNpcs = forceDeadNpcs;
    }

    public void setResetDeadOnChangeNpcs(Set<Integer> resetDeadOnChangeNpcs) {
        this.resetDeadOnChangeNpcs = resetDeadOnChangeNpcs;
    }

    public void setForceDeadAnimations(Set<Integer> forceDeadAnimations) {
        this.forceDeadAnimations = forceDeadAnimations;
    }

    public void setNonAttackNpcs(Set<Integer> nonAttackNpcs) {
        this.nonAttackNpcs = nonAttackNpcs;
    }

    public void setHiscoreMapping(Map<String, List<String>> hiscoreMapping) {
        this.hiscoreMapping = hiscoreMapping;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RuntimeConfig)) {
            return false;
        }
        RuntimeConfig other = (RuntimeConfig)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Map<String, ?> this$props = this.getProps();
        Map<String, ?> other$props = other.getProps();
        if (this$props == null ? other$props != null : !((Object)this$props).equals(other$props)) {
            return false;
        }
        Map<String, String> this$sysProps = this.getSysProps();
        Map<String, String> other$sysProps = other.getSysProps();
        if (this$sysProps == null ? other$sysProps != null : !((Object)this$sysProps).equals(other$sysProps)) {
            return false;
        }
        String this$outageMessage = this.getOutageMessage();
        String other$outageMessage = other.getOutageMessage();
        if (!Objects.equals(this$outageMessage, other$outageMessage)) {
            return false;
        }
        Map<String, String> this$outageLinks = this.getOutageLinks();
        Map<String, String> other$outageLinks = other.getOutageLinks();
        if (this$outageLinks == null ? other$outageLinks != null : !((Object)this$outageLinks).equals(other$outageLinks)) {
            return false;
        }
        Set<Integer> this$ignoreDeadNpcs = this.getIgnoreDeadNpcs();
        Set<Integer> other$ignoreDeadNpcs = other.getIgnoreDeadNpcs();
        if (this$ignoreDeadNpcs == null ? other$ignoreDeadNpcs != null : !((Object)this$ignoreDeadNpcs).equals(other$ignoreDeadNpcs)) {
            return false;
        }
        Set<Integer> this$forceDeadNpcs = this.getForceDeadNpcs();
        Set<Integer> other$forceDeadNpcs = other.getForceDeadNpcs();
        if (this$forceDeadNpcs == null ? other$forceDeadNpcs != null : !((Object)this$forceDeadNpcs).equals(other$forceDeadNpcs)) {
            return false;
        }
        Set<Integer> this$resetDeadOnChangeNpcs = this.getResetDeadOnChangeNpcs();
        Set<Integer> other$resetDeadOnChangeNpcs = other.getResetDeadOnChangeNpcs();
        if (this$resetDeadOnChangeNpcs == null ? other$resetDeadOnChangeNpcs != null : !((Object)this$resetDeadOnChangeNpcs).equals(other$resetDeadOnChangeNpcs)) {
            return false;
        }
        Set<Integer> this$forceDeadAnimations = this.getForceDeadAnimations();
        Set<Integer> other$forceDeadAnimations = other.getForceDeadAnimations();
        if (this$forceDeadAnimations == null ? other$forceDeadAnimations != null : !((Object)this$forceDeadAnimations).equals(other$forceDeadAnimations)) {
            return false;
        }
        Set<Integer> this$nonAttackNpcs = this.getNonAttackNpcs();
        Set<Integer> other$nonAttackNpcs = other.getNonAttackNpcs();
        if (this$nonAttackNpcs == null ? other$nonAttackNpcs != null : !((Object)this$nonAttackNpcs).equals(other$nonAttackNpcs)) {
            return false;
        }
        Map<String, List<String>> this$hiscoreMapping = this.getHiscoreMapping();
        Map<String, List<String>> other$hiscoreMapping = other.getHiscoreMapping();
        return !(this$hiscoreMapping == null ? other$hiscoreMapping != null : !((Object)this$hiscoreMapping).equals(other$hiscoreMapping));
    }

    protected boolean canEqual(Object other) {
        return other instanceof RuntimeConfig;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Map<String, ?> $props = this.getProps();
        result = result * 59 + ($props == null ? 43 : ((Object)$props).hashCode());
        Map<String, String> $sysProps = this.getSysProps();
        result = result * 59 + ($sysProps == null ? 43 : ((Object)$sysProps).hashCode());
        String $outageMessage = this.getOutageMessage();
        result = result * 59 + ($outageMessage == null ? 43 : $outageMessage.hashCode());
        Map<String, String> $outageLinks = this.getOutageLinks();
        result = result * 59 + ($outageLinks == null ? 43 : ((Object)$outageLinks).hashCode());
        Set<Integer> $ignoreDeadNpcs = this.getIgnoreDeadNpcs();
        result = result * 59 + ($ignoreDeadNpcs == null ? 43 : ((Object)$ignoreDeadNpcs).hashCode());
        Set<Integer> $forceDeadNpcs = this.getForceDeadNpcs();
        result = result * 59 + ($forceDeadNpcs == null ? 43 : ((Object)$forceDeadNpcs).hashCode());
        Set<Integer> $resetDeadOnChangeNpcs = this.getResetDeadOnChangeNpcs();
        result = result * 59 + ($resetDeadOnChangeNpcs == null ? 43 : ((Object)$resetDeadOnChangeNpcs).hashCode());
        Set<Integer> $forceDeadAnimations = this.getForceDeadAnimations();
        result = result * 59 + ($forceDeadAnimations == null ? 43 : ((Object)$forceDeadAnimations).hashCode());
        Set<Integer> $nonAttackNpcs = this.getNonAttackNpcs();
        result = result * 59 + ($nonAttackNpcs == null ? 43 : ((Object)$nonAttackNpcs).hashCode());
        Map<String, List<String>> $hiscoreMapping = this.getHiscoreMapping();
        result = result * 59 + ($hiscoreMapping == null ? 43 : ((Object)$hiscoreMapping).hashCode());
        return result;
    }

    public String toString() {
        return "RuntimeConfig(props=" + this.getProps() + ", sysProps=" + this.getSysProps() + ", outageMessage=" + this.getOutageMessage() + ", outageLinks=" + this.getOutageLinks() + ", ignoreDeadNpcs=" + this.getIgnoreDeadNpcs() + ", forceDeadNpcs=" + this.getForceDeadNpcs() + ", resetDeadOnChangeNpcs=" + this.getResetDeadOnChangeNpcs() + ", forceDeadAnimations=" + this.getForceDeadAnimations() + ", nonAttackNpcs=" + this.getNonAttackNpcs() + ", hiscoreMapping=" + this.getHiscoreMapping() + ")";
    }
}

