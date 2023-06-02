/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.reflect.TypeToken
 *  javax.inject.Inject
 *  net.runelite.api.Client
 *  net.runelite.api.MenuAction
 *  net.runelite.api.NPC
 *  net.runelite.api.NPCComposition
 *  net.runelite.api.events.GameTick
 *  net.runelite.api.events.MenuEntryAdded
 *  net.runelite.api.events.NpcSpawned
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package net.runelite.client.plugins.spawndumper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.swing.JButton;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.NPC;
import net.runelite.api.NPCComposition;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.NpcSpawned;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.devtools.DevToolsPlugin;
import net.runelite.client.plugins.spawndumper.NPCSpawn;
import net.runelite.client.plugins.spawndumper.SpawnDumperButton;
import net.runelite.client.plugins.spawndumper.SpawnDumperNPCOverlay;
import net.runelite.client.plugins.spawndumper.SpawnDumperOverlay;
import net.runelite.client.plugins.spawndumper.SpawnDumperPanel;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.ImageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(name="Spawn Dumper", forceDisabled=true)
public class SpawnDumperPlugin
extends Plugin {
    private static final Logger log = LoggerFactory.getLogger(SpawnDumperPlugin.class);
    private static final String TAG = "Spawn Info";
    private static final String UNTAG = "Remove Spawn Info";
    @Inject
    private Client client;
    @Inject
    private ClientToolbar clientToolbar;
    @Inject
    private OverlayManager overlayManager;
    @Inject
    private SpawnDumperOverlay spawnDumperOverlay;
    @Inject
    private SpawnDumperNPCOverlay spawnDumperNPCOverlay;
    private SpawnDumperButton enabled;
    private JButton saveSpawns;
    private JButton loadSpawns;
    private JButton clearSpawns;
    private NavigationButton navButton;
    private Map<Integer, NPCSpawn> spawns = new HashMap<Integer, NPCSpawn>();
    private int updatedThisTick = 0;
    private int selectedNPCIndex = -1;

    @Override
    protected void startUp() throws Exception {
        this.enabled = new SpawnDumperButton("Enabled");
        this.saveSpawns = new JButton("Save Spawns");
        this.saveSpawns.addActionListener(e -> {
            try {
                Path spawnsPath = Paths.get("spawns.json", new String[0]);
                Files.write(spawnsPath, new Gson().toJson(this.spawns.values()).getBytes(), new OpenOption[0]);
                int points = this.spawns.values().stream().mapToInt(value -> value.getPoints().size()).sum();
                System.out.println("Saved " + this.spawns.size() + " spawns with " + points + " points to " + spawnsPath + ".");
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        this.loadSpawns = new JButton("Load Spawns");
        this.loadSpawns.addActionListener(e -> {
            try {
                List tempSpawns = (List)new Gson().fromJson((Reader)new InputStreamReader(new FileInputStream("spawns.json")), new TypeToken<List<NPCSpawn>>(){}.getType());
                int points = 0;
                this.spawns.clear();
                for (NPCSpawn spawn : tempSpawns) {
                    points += spawn.getPoints().size();
                    this.spawns.put(spawn.getIndex(), spawn);
                }
                System.out.println("Loaded " + this.spawns.size() + " spawns with " + points + " points.");
            }
            catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
        });
        this.clearSpawns = new JButton("Clear Spawns");
        this.clearSpawns.addActionListener(e -> this.spawns.clear());
        SpawnDumperPanel panel = (SpawnDumperPanel)this.injector.getInstance(SpawnDumperPanel.class);
        BufferedImage icon = ImageUtil.getResourceStreamFromClass(DevToolsPlugin.class, "devtools_icon.png");
        this.overlayManager.add(this.spawnDumperOverlay);
        this.overlayManager.add(this.spawnDumperNPCOverlay);
        this.navButton = NavigationButton.builder().tooltip("Spawn Dumper").icon(icon).priority(1).panel(panel).build();
        this.clientToolbar.addNavigation(this.navButton);
    }

    @Override
    protected void shutDown() throws Exception {
        this.overlayManager.remove(this.spawnDumperOverlay);
        this.overlayManager.remove(this.spawnDumperNPCOverlay);
        this.clientToolbar.removeNavigation(this.navButton);
    }

    @Subscribe
    public void onMenuEntryAdded(MenuEntryAdded event) {
        int type = event.getType();
        if (type >= 2000) {
            type -= 2000;
        }
        if (type == MenuAction.EXAMINE_NPC.getId()) {
            this.client.createMenuEntry(-1).setOption(event.getIdentifier() == this.selectedNPCIndex ? UNTAG : TAG).setTarget(event.getTarget()).setIdentifier(event.getIdentifier()).onClick(c -> {
                this.selectedNPCIndex = this.selectedNPCIndex == c.getIdentifier() ? -1 : c.getIdentifier();
            });
        }
    }

    @Subscribe
    public void onNpcSpawned(NpcSpawned event) {
        if (!this.enabled.isActive()) {
            return;
        }
        NPC npc = event.getNpc();
        NPCComposition def = npc.getComposition();
        NPCSpawn existSpawn = this.spawns.get(npc.getIndex());
        if (existSpawn != null && existSpawn.getNpc() == def.getId()) {
            return;
        }
        NPCSpawn spawn = new NPCSpawn(def.getId(), npc.getIndex());
        spawn.setOrientation(npc.getOrientation());
        spawn.getPoints().add(npc.getWorldLocation());
        this.spawns.put(npc.getIndex(), spawn);
        if (existSpawn != null) {
            log.debug("Replaced " + existSpawn.getNpc() + " with " + spawn.getNpc() + " due to same index but different ids.");
        } else {
            log.debug("Added new NPC to spawns: index=" + npc.getIndex() + ", id=" + def.getId() + ", name=" + def.getName() + "");
        }
    }

    @Subscribe
    public void onGameTick(GameTick event) {
        this.updatedThisTick = 0;
        if (!this.enabled.isActive()) {
            return;
        }
        for (NPC npc : this.client.getNpcs()) {
            NPCSpawn spawn = this.spawns.get(npc.getIndex());
            if (spawn == null) continue;
            if (spawn.getOrientation() != -1 && npc.getOrientation() != spawn.getOrientation()) {
                spawn.setOrientation(-1);
            }
            if (!spawn.getPoints().add(npc.getWorldLocation())) continue;
            ++this.updatedThisTick;
        }
    }

    public SpawnDumperButton getEnabled() {
        return this.enabled;
    }

    public JButton getSaveSpawns() {
        return this.saveSpawns;
    }

    public JButton getLoadSpawns() {
        return this.loadSpawns;
    }

    public JButton getClearSpawns() {
        return this.clearSpawns;
    }

    Map<Integer, NPCSpawn> getSpawns() {
        return this.spawns;
    }

    int getUpdatedThisTick() {
        return this.updatedThisTick;
    }

    int getSelectedNPCIndex() {
        return this.selectedNPCIndex;
    }
}

