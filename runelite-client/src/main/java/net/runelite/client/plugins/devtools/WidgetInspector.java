/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.inject.Inject
 *  com.google.inject.Provider
 *  com.google.inject.Singleton
 *  net.runelite.api.Client
 *  net.runelite.api.MenuAction
 *  net.runelite.api.MenuEntry
 *  net.runelite.api.events.MenuEntryAdded
 *  net.runelite.api.events.MenuOptionClicked
 *  net.runelite.api.widgets.Widget
 *  net.runelite.api.widgets.WidgetInfo
 *  net.runelite.api.widgets.WidgetItem
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package net.runelite.client.plugins.devtools;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.Collection;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Stream;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.devtools.DevToolsConfig;
import net.runelite.client.plugins.devtools.DevToolsFrame;
import net.runelite.client.plugins.devtools.WidgetInfoTableModel;
import net.runelite.client.plugins.devtools.WidgetInspectorOverlay;
import net.runelite.client.plugins.devtools.WidgetItemNode;
import net.runelite.client.plugins.devtools.WidgetTreeNode;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.ColorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
class WidgetInspector
extends DevToolsFrame {
    private static final Logger log = LoggerFactory.getLogger(WidgetInspector.class);
    private static final Map<Integer, WidgetInfo> widgetIdMap = new HashMap<Integer, WidgetInfo>();
    static final Color SELECTED_WIDGET_COLOR = Color.CYAN;
    private static final float SELECTED_WIDGET_HUE;
    private final Client client;
    private final ClientThread clientThread;
    private final DevToolsConfig config;
    private final Provider<WidgetInspectorOverlay> overlay;
    private final OverlayManager overlayManager;
    private final JTree widgetTree;
    private final WidgetInfoTableModel infoTableModel;
    private final JCheckBox alwaysOnTop;
    private final JCheckBox hideHidden;
    private DefaultMutableTreeNode root;
    private Widget selectedWidget;
    private int selectedItem;
    private Widget picker = null;
    private boolean pickerSelected = false;

    @Inject
    private WidgetInspector(Client client, ClientThread clientThread, WidgetInfoTableModel infoTableModel, DevToolsConfig config, EventBus eventBus, Provider<WidgetInspectorOverlay> overlay, OverlayManager overlayManager) {
        this.client = client;
        this.clientThread = clientThread;
        this.infoTableModel = infoTableModel;
        this.config = config;
        this.overlay = overlay;
        this.overlayManager = overlayManager;
        eventBus.register(this);
        this.setTitle("RuneLite Widget Inspector");
        this.setLayout(new BorderLayout());
        this.widgetTree = new JTree(new DefaultMutableTreeNode());
        this.widgetTree.setRootVisible(false);
        this.widgetTree.setShowsRootHandles(true);
        this.widgetTree.getSelectionModel().addTreeSelectionListener(e -> {
            Object selected = this.widgetTree.getLastSelectedPathComponent();
            if (selected instanceof WidgetTreeNode) {
                WidgetTreeNode node = (WidgetTreeNode)selected;
                Widget widget = node.getWidget();
                this.setSelectedWidget(widget, -1, false);
            } else if (selected instanceof WidgetItemNode) {
                WidgetItemNode node = (WidgetItemNode)selected;
                this.setSelectedWidget(node.getWidgetItem().getWidget(), node.getWidgetItem().getIndex(), false);
            }
        });
        JScrollPane treeScrollPane = new JScrollPane(this.widgetTree);
        treeScrollPane.setPreferredSize(new Dimension(200, 400));
        JTable widgetInfo = new JTable(infoTableModel);
        JScrollPane infoScrollPane = new JScrollPane(widgetInfo);
        infoScrollPane.setPreferredSize(new Dimension(400, 400));
        JPanel bottomPanel = new JPanel();
        this.add((Component)bottomPanel, "South");
        JButton refreshWidgetsBtn = new JButton("Refresh");
        refreshWidgetsBtn.addActionListener(e -> this.refreshWidgets());
        bottomPanel.add(refreshWidgetsBtn);
        this.alwaysOnTop = new JCheckBox("Always on top");
        this.alwaysOnTop.addItemListener(ev -> config.inspectorAlwaysOnTop(this.alwaysOnTop.isSelected()));
        this.onConfigChanged(null);
        bottomPanel.add(this.alwaysOnTop);
        this.hideHidden = new JCheckBox("Hide hidden");
        this.hideHidden.setSelected(true);
        this.hideHidden.addItemListener(ev -> this.refreshWidgets());
        bottomPanel.add(this.hideHidden);
        JButton revalidateWidget = new JButton("Revalidate");
        revalidateWidget.addActionListener(ev -> clientThread.invokeLater(() -> {
            if (this.selectedWidget == null) {
                return;
            }
            this.selectedWidget.revalidate();
        }));
        bottomPanel.add(revalidateWidget);
        JSplitPane split = new JSplitPane(1, treeScrollPane, infoScrollPane);
        this.add((Component)split, "Center");
        this.pack();
    }

    @Subscribe
    private void onConfigChanged(ConfigChanged ev) {
        boolean onTop = this.config.inspectorAlwaysOnTop();
        this.setAlwaysOnTop(onTop);
        this.alwaysOnTop.setSelected(onTop);
    }

    private void refreshWidgets() {
        this.clientThread.invokeLater(() -> {
            Widget[] rootWidgets = this.client.getWidgetRoots();
            this.root = new DefaultMutableTreeNode();
            Widget wasSelectedWidget = this.selectedWidget;
            int wasSelectedItem = this.selectedItem;
            this.selectedWidget = null;
            this.selectedItem = -1;
            for (Widget widget : rootWidgets) {
                DefaultMutableTreeNode childNode = this.addWidget("R", widget);
                if (childNode == null) continue;
                this.root.add(childNode);
            }
            SwingUtilities.invokeLater(() -> {
                this.widgetTree.setModel(new DefaultTreeModel(this.root));
                this.setSelectedWidget(wasSelectedWidget, wasSelectedItem, true);
            });
        });
    }

    private DefaultMutableTreeNode addWidget(String type, Widget widget) {
        Collection items;
        DefaultMutableTreeNode childNode;
        if (widget == null || this.hideHidden.isSelected() && widget.isHidden()) {
            return null;
        }
        WidgetTreeNode node = new WidgetTreeNode(type, widget);
        Widget[] childComponents = widget.getDynamicChildren();
        if (childComponents != null) {
            for (Widget component : childComponents) {
                childNode = this.addWidget("D", component);
                if (childNode == null) continue;
                node.add(childNode);
            }
        }
        if ((childComponents = widget.getStaticChildren()) != null) {
            for (Widget component : childComponents) {
                childNode = this.addWidget("S", component);
                if (childNode == null) continue;
                node.add(childNode);
            }
        }
        if ((childComponents = widget.getNestedChildren()) != null) {
            for (Widget component : childComponents) {
                childNode = this.addWidget("N", component);
                if (childNode == null) continue;
                node.add(childNode);
            }
        }
        if ((items = widget.getWidgetItems()) != null) {
            for (WidgetItem item : items) {
                if (item == null) continue;
                node.add(new WidgetItemNode(item));
            }
        }
        return node;
    }

    private void setSelectedWidget(Widget widget, int item, boolean updateTree) {
        this.infoTableModel.setWidget(widget);
        if (this.selectedWidget == widget && this.selectedItem == item) {
            return;
        }
        this.selectedWidget = widget;
        this.selectedItem = item;
        if (this.root == null || !updateTree) {
            return;
        }
        this.clientThread.invoke(() -> {
            DefaultMutableTreeNode inner;
            Stack<Widget> treePath = new Stack<Widget>();
            for (Widget w = widget; w != null; w = w.getParent()) {
                treePath.push(w);
            }
            DefaultMutableTreeNode node = this.root;
            block1: while (!treePath.empty()) {
                Widget w = (Widget)treePath.pop();
                Enumeration<TreeNode> it = node.children();
                while (it.hasMoreElements()) {
                    inner = (WidgetTreeNode)it.nextElement();
                    if (((WidgetTreeNode)inner).getWidget().getId() != w.getId() || ((WidgetTreeNode)inner).getWidget().getIndex() != w.getIndex()) continue;
                    node = inner;
                    continue block1;
                }
            }
            if (this.selectedItem != -1) {
                Enumeration<TreeNode> it = node.children();
                while (it.hasMoreElements()) {
                    TreeNode wiw = it.nextElement();
                    if (!(wiw instanceof WidgetItemNode) || ((WidgetItemNode)(inner = (WidgetItemNode)wiw)).getWidgetItem().getIndex() != this.selectedItem) continue;
                    node = inner;
                    break;
                }
            }
            DefaultMutableTreeNode fnode = node;
            SwingUtilities.invokeLater(() -> {
                this.widgetTree.getSelectionModel().clearSelection();
                this.widgetTree.getSelectionModel().addSelectionPath(new TreePath(fnode.getPath()));
            });
        });
    }

    static WidgetInfo getWidgetInfo(int packedId) {
        if (widgetIdMap.isEmpty()) {
            WidgetInfo[] widgets;
            for (WidgetInfo w : widgets = WidgetInfo.values()) {
                widgetIdMap.put(w.getPackedId(), w);
            }
        }
        return widgetIdMap.get(packedId);
    }

    @Override
    public void open() {
        super.open();
        this.overlayManager.add((Overlay)this.overlay.get());
        this.clientThread.invokeLater(this::addPickerWidget);
    }

    @Override
    public void close() {
        this.overlayManager.remove((Overlay)this.overlay.get());
        this.clientThread.invokeLater(this::removePickerWidget);
        this.setSelectedWidget(null, -1, false);
        super.close();
    }

    private void removePickerWidget() {
        if (this.picker == null) {
            return;
        }
        Widget parent = this.picker.getParent();
        if (parent == null) {
            return;
        }
        Widget[] children = parent.getChildren();
        if (children == null || children.length <= this.picker.getIndex() || children[this.picker.getIndex()] != this.picker) {
            return;
        }
        children[this.picker.getIndex()] = null;
    }

    private void addPickerWidget() {
        this.removePickerWidget();
        int x = 10;
        int y = 2;
        Widget parent = this.client.getWidget(WidgetInfo.MINIMAP_ORBS);
        if (parent == null) {
            Widget[] roots = this.client.getWidgetRoots();
            parent = Stream.of(roots).filter(w -> w.getType() == 0 && w.getContentType() == 0 && !w.isSelfHidden()).sorted(Comparator.comparingInt(w -> w.getRelativeX() + w.getRelativeY()).reversed().thenComparingInt(Widget::getId).reversed()).findFirst().get();
            x = 4;
            y = 4;
        }
        this.picker = parent.createChild(-1, 5);
        log.info("Picker is {}.{} [{}]", new Object[]{WidgetInfo.TO_GROUP((int)this.picker.getId()), WidgetInfo.TO_CHILD((int)this.picker.getId()), this.picker.getIndex()});
        this.picker.setSpriteId(1653);
        this.picker.setOriginalWidth(15);
        this.picker.setOriginalHeight(17);
        this.picker.setOriginalX(x);
        this.picker.setOriginalY(y);
        this.picker.revalidate();
        this.picker.setTargetVerb("Select");
        this.picker.setName("Pick");
        this.picker.setClickMask(98304);
        this.picker.setNoClickThrough(true);
        this.picker.setOnTargetEnterListener(new Object[]{ev -> {
            this.pickerSelected = true;
            this.picker.setOpacity(30);
            this.client.setAllWidgetsAreOpTargetable(true);
        }});
        this.picker.setOnTargetLeaveListener(new Object[]{ev -> this.onPickerDeselect()});
    }

    private void onPickerDeselect() {
        this.client.setAllWidgetsAreOpTargetable(false);
        this.picker.setOpacity(0);
        this.pickerSelected = false;
    }

    @Subscribe
    private void onMenuOptionClicked(MenuOptionClicked ev) {
        if (!this.pickerSelected) {
            return;
        }
        this.onPickerDeselect();
        this.client.setSpellSelected(false);
        ev.consume();
        Object target = this.getWidgetOrWidgetItemForMenuOption(ev.getMenuAction(), ev.getParam0(), ev.getParam1());
        if (target == null) {
            return;
        }
        if (target instanceof WidgetItem) {
            WidgetItem iw = (WidgetItem)target;
            this.setSelectedWidget(iw.getWidget(), iw.getIndex(), true);
        } else {
            this.setSelectedWidget((Widget)target, -1, true);
        }
    }

    @Subscribe
    private void onMenuEntryAdded(MenuEntryAdded event) {
        if (!this.pickerSelected) {
            return;
        }
        MenuEntry[] menuEntries = this.client.getMenuEntries();
        for (int i = 0; i < menuEntries.length; ++i) {
            MenuEntry entry = menuEntries[i];
            if (entry.getType() != MenuAction.WIDGET_USE_ON_ITEM && entry.getType() != MenuAction.WIDGET_TARGET_ON_WIDGET) continue;
            String name = WidgetInfo.TO_GROUP((int)entry.getParam1()) + "." + WidgetInfo.TO_CHILD((int)entry.getParam1());
            if (entry.getParam0() != -1) {
                name = name + " [" + entry.getParam0() + "]";
            }
            Color color = this.colorForWidget(i, menuEntries.length);
            entry.setTarget(ColorUtil.wrapWithColorTag(name, color));
        }
    }

    Color colorForWidget(int index, int length) {
        float h = SELECTED_WIDGET_HUE + 0.1f + 0.8f / (float)length * (float)index;
        return Color.getHSBColor(h, 1.0f, 1.0f);
    }

    Object getWidgetOrWidgetItemForMenuOption(MenuAction type, int param0, int param1) {
        if (type == MenuAction.WIDGET_TARGET_ON_WIDGET) {
            Widget w = this.client.getWidget(param1);
            if (param0 != -1) {
                w = w.getChild(param0);
            }
            return w;
        }
        if (type == MenuAction.WIDGET_USE_ON_ITEM) {
            Widget w = this.client.getWidget(param1);
            return w.getWidgetItem(param0);
        }
        return null;
    }

    public static String getWidgetIdentifier(Widget widget) {
        WidgetInfo info;
        int id = widget.getId();
        String str = WidgetInfo.TO_GROUP((int)id) + "." + WidgetInfo.TO_CHILD((int)id);
        if (widget.getIndex() != -1) {
            str = str + "[" + widget.getIndex() + "]";
        }
        if ((info = WidgetInspector.getWidgetInfo(id)) != null) {
            str = str + " " + info.name();
        }
        return str;
    }

    public Widget getSelectedWidget() {
        return this.selectedWidget;
    }

    public int getSelectedItem() {
        return this.selectedItem;
    }

    public boolean isPickerSelected() {
        return this.pickerSelected;
    }

    static {
        float[] hsb = new float[3];
        Color.RGBtoHSB(SELECTED_WIDGET_COLOR.getRed(), SELECTED_WIDGET_COLOR.getGreen(), SELECTED_WIDGET_COLOR.getBlue(), hsb);
        SELECTED_WIDGET_HUE = hsb[0];
    }
}

