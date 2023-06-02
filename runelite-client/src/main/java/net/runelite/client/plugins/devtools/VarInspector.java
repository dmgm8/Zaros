/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.HashMultimap
 *  com.google.common.collect.ImmutableMap$Builder
 *  com.google.common.collect.Multimap
 *  com.google.inject.Inject
 *  net.runelite.api.Client
 *  net.runelite.api.IndexDataBase
 *  net.runelite.api.VarClientInt
 *  net.runelite.api.VarClientStr
 *  net.runelite.api.VarPlayer
 *  net.runelite.api.VarbitComposition
 *  net.runelite.api.Varbits
 *  net.runelite.api.events.VarClientIntChanged
 *  net.runelite.api.events.VarClientStrChanged
 *  net.runelite.api.events.VarbitChanged
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package net.runelite.client.plugins.devtools;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multimap;
import com.google.inject.Inject;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.CompoundBorder;
import net.runelite.api.Client;
import net.runelite.api.IndexDataBase;
import net.runelite.api.VarClientInt;
import net.runelite.api.VarClientStr;
import net.runelite.api.VarPlayer;
import net.runelite.api.VarbitComposition;
import net.runelite.api.Varbits;
import net.runelite.api.events.VarClientIntChanged;
import net.runelite.api.events.VarClientStrChanged;
import net.runelite.api.events.VarbitChanged;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.devtools.DevToolsFrame;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.DynamicGridLayout;
import net.runelite.client.ui.FontManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class VarInspector
extends DevToolsFrame {
    private static final Logger log = LoggerFactory.getLogger(VarInspector.class);
    private static final int MAX_LOG_ENTRIES = 10000;
    private static final int VARBITS_ARCHIVE_ID = 14;
    private static final Map<Integer, String> VARBIT_NAMES;
    private static final Map<Integer, String> VARCINT_NAMES;
    private static final Map<Integer, String> VARCSTR_NAMES;
    private final Client client;
    private final ClientThread clientThread;
    private final EventBus eventBus;
    private final JPanel tracker = new JPanel();
    private int lastTick = 0;
    private int[] oldVarps = null;
    private int[] oldVarps2 = null;
    private Multimap<Integer, Integer> varbits;
    private Map<Integer, Object> varcs = null;

    @Inject
    VarInspector(Client client, ClientThread clientThread, EventBus eventBus) {
        this.client = client;
        this.clientThread = clientThread;
        this.eventBus = eventBus;
        this.setTitle("RuneLite Var Inspector");
        this.setLayout(new BorderLayout());
        this.tracker.setLayout(new DynamicGridLayout(0, 1, 0, 3));
        JPanel trackerWrapper = new JPanel();
        trackerWrapper.setLayout(new BorderLayout());
        trackerWrapper.add((Component)this.tracker, "North");
        JScrollPane trackerScroller = new JScrollPane(trackerWrapper);
        trackerScroller.setPreferredSize(new Dimension(400, 400));
        final JScrollBar vertical = trackerScroller.getVerticalScrollBar();
        vertical.addAdjustmentListener(new AdjustmentListener(){
            int lastMaximum = this.actualMax();

            private int actualMax() {
                return vertical.getMaximum() - vertical.getModel().getExtent();
            }

            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                if (vertical.getValue() >= this.lastMaximum) {
                    vertical.setValue(this.actualMax());
                }
                this.lastMaximum = this.actualMax();
            }
        });
        this.add((Component)trackerScroller, "Center");
        JPanel trackerOpts = new JPanel();
        trackerOpts.setLayout(new FlowLayout());
        for (VarType cb : VarType.values()) {
            trackerOpts.add(cb.getCheckBox());
        }
        JButton clearBtn = new JButton("Clear");
        clearBtn.addActionListener(e -> {
            this.tracker.removeAll();
            this.tracker.revalidate();
        });
        trackerOpts.add(clearBtn);
        this.add((Component)trackerOpts, "South");
        this.pack();
    }

    private void addVarLog(VarType type, String name, int old, int neew) {
        this.addVarLog(type, name, Integer.toString(old), Integer.toString(neew));
    }

    private void addVarLog(VarType type, String name, String old, String neew) {
        if (!type.getCheckBox().isSelected()) {
            return;
        }
        int tick = this.client.getTickCount();
        SwingUtilities.invokeLater(() -> {
            if (tick != this.lastTick) {
                this.lastTick = tick;
                JLabel header = new JLabel("Tick " + tick);
                header.setFont(FontManager.getRunescapeSmallFont());
                header.setBorder(new CompoundBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, ColorScheme.LIGHT_GRAY_COLOR), BorderFactory.createEmptyBorder(3, 6, 0, 0)));
                this.tracker.add(header);
            }
            this.tracker.add(new JLabel(String.format("%s %s changed: %s -> %s", type.getName(), name, old, neew)));
            while (this.tracker.getComponentCount() > 10000) {
                this.tracker.remove(0);
            }
            this.tracker.revalidate();
        });
    }

    @Subscribe
    public void onVarbitChanged(VarbitChanged varbitChanged) {
        int index = varbitChanged.getIndex();
        int[] varps = this.client.getVarps();
        Iterator iterator = this.varbits.get((Object)index).iterator();
        while (iterator.hasNext()) {
            int neew;
            int i = (Integer)iterator.next();
            int old = this.client.getVarbitValue(this.oldVarps, i);
            if (old == (neew = this.client.getVarbitValue(varps, i))) continue;
            this.client.setVarbitValue(this.oldVarps2, i, neew);
            String name = VARBIT_NAMES.getOrDefault(i, Integer.toString(i));
            this.addVarLog(VarType.VARBIT, name, old, neew);
        }
        int old = this.oldVarps2[index];
        int neew = varps[index];
        if (old != neew) {
            String name = Integer.toString(index);
            for (VarPlayer varp : VarPlayer.values()) {
                if (varp.getId() != index) continue;
                name = String.format("%s(%d)", varp.name(), index);
                break;
            }
            this.addVarLog(VarType.VARP, name, old, neew);
        }
        System.arraycopy(this.client.getVarps(), 0, this.oldVarps, 0, this.oldVarps.length);
        System.arraycopy(this.client.getVarps(), 0, this.oldVarps2, 0, this.oldVarps2.length);
    }

    @Subscribe
    public void onVarClientIntChanged(VarClientIntChanged e) {
        int idx = e.getIndex();
        int neew = this.client.getVarcMap().getOrDefault(idx, 0);
        int old = (Integer)this.varcs.getOrDefault(idx, 0);
        this.varcs.put(idx, neew);
        if (old != neew) {
            String name = VARCINT_NAMES.getOrDefault(idx, Integer.toString(idx));
            this.addVarLog(VarType.VARCINT, name, old, neew);
        }
    }

    @Subscribe
    public void onVarClientStrChanged(VarClientStrChanged e) {
        int idx = e.getIndex();
        String neew = this.client.getVarcMap().getOrDefault(idx, "");
        String old = (String)this.varcs.getOrDefault(idx, "");
        this.varcs.put(idx, neew);
        if (!Objects.equals(old, neew)) {
            String name = VARCSTR_NAMES.getOrDefault(idx, Integer.toString(idx));
            old = old != null ? "\"" + old + "\"" : "null";
            neew = neew != null ? "\"" + neew + "\"" : "null";
            this.addVarLog(VarType.VARCSTR, name, old, neew);
        }
    }

    @Override
    public void open() {
        if (this.oldVarps == null) {
            this.oldVarps = new int[this.client.getVarps().length];
            this.oldVarps2 = new int[this.client.getVarps().length];
        }
        System.arraycopy(this.client.getVarps(), 0, this.oldVarps, 0, this.oldVarps.length);
        System.arraycopy(this.client.getVarps(), 0, this.oldVarps2, 0, this.oldVarps2.length);
        this.varcs = new HashMap<Integer, Object>(this.client.getVarcMap());
        this.varbits = HashMultimap.create();
        this.clientThread.invoke(() -> {
            int[] varbitIds;
            IndexDataBase indexVarbits = this.client.getIndexConfig();
            for (int id : varbitIds = indexVarbits.getFileIds(14)) {
                VarbitComposition varbit = this.client.getVarbit(id);
                if (varbit == null) continue;
                this.varbits.put((Object)varbit.getIndex(), (Object)id);
            }
        });
        this.eventBus.register(this);
        super.open();
    }

    @Override
    public void close() {
        super.close();
        this.tracker.removeAll();
        this.eventBus.unregister(this);
        this.varcs = null;
        this.varbits = null;
    }

    static {
        ImmutableMap.Builder varbits = new ImmutableMap.Builder();
        ImmutableMap.Builder varcint = new ImmutableMap.Builder();
        ImmutableMap.Builder varcstr = new ImmutableMap.Builder();
        try {
            for (Field f : Varbits.class.getDeclaredFields()) {
                varbits.put((Object)f.getInt(null), (Object)f.getName());
            }
            for (Field f : VarClientInt.class.getDeclaredFields()) {
                varcint.put((Object)f.getInt(null), (Object)f.getName());
            }
            for (Field f : VarClientStr.class.getDeclaredFields()) {
                varcstr.put((Object)f.getInt(null), (Object)f.getName());
            }
        }
        catch (IllegalAccessException ex) {
            log.error("error setting up var names", (Throwable)ex);
        }
        VARBIT_NAMES = varbits.build();
        VARCINT_NAMES = varcint.build();
        VARCSTR_NAMES = varcstr.build();
    }

    private static enum VarType {
        VARBIT("Varbit"),
        VARP("VarPlayer"),
        VARCINT("VarClientInt"),
        VARCSTR("VarClientStr");

        private final String name;
        private final JCheckBox checkBox;

        private VarType(String name) {
            this.name = name;
            this.checkBox = new JCheckBox(name, true);
        }

        public String getName() {
            return this.name;
        }

        public JCheckBox getCheckBox() {
            return this.checkBox;
        }
    }
}

