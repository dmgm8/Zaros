/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nonnull
 *  javax.annotation.Nullable
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package net.runelite.client.util;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Insets;
import java.awt.SecondaryLoop;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Enumeration;
import java.util.function.BiConsumer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.LookAndFeel;
import javax.swing.PopupFactory;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.FontUIResource;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.ui.components.CustomScrollBarUI;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.MacOSPopupFactory;
import net.runelite.client.util.OSType;
import net.runelite.client.util.OSXUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SwingUtil {
    private static final Logger log = LoggerFactory.getLogger(SwingUtil.class);

    public static void setupDefaults() {
        ToolTipManager.sharedInstance().setLightWeightPopupEnabled(false);
        ToolTipManager.sharedInstance().setInitialDelay(300);
        JPopupMenu.setDefaultLightWeightPopupEnabled(false);
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("MenuItem.foreground", Color.WHITE);
        UIManager.put("Panel.background", ColorScheme.DARK_GRAY_COLOR);
        UIManager.put("ScrollBarUI", CustomScrollBarUI.class.getName());
        UIManager.put("TextField.selectionBackground", ColorScheme.BRAND_ORANGE_TRANSPARENT);
        UIManager.put("TextField.selectionForeground", Color.WHITE);
        UIManager.put("FormattedTextField.selectionBackground", ColorScheme.BRAND_ORANGE_TRANSPARENT);
        UIManager.put("FormattedTextField.selectionForeground", Color.WHITE);
        UIManager.put("TextArea.selectionBackground", ColorScheme.BRAND_ORANGE_TRANSPARENT);
        UIManager.put("TextArea.selectionForeground", Color.WHITE);
        System.setProperty("jgoodies.popupDropShadowEnabled", "false");
        System.setProperty("sun.awt.noerasebackground", "true");
    }

    public static void setTheme(@Nonnull LookAndFeel laf) {
        try {
            UIManager.setLookAndFeel(laf);
            if (OSType.getOSType() == OSType.MacOS) {
                PopupFactory.setSharedInstance(new MacOSPopupFactory());
            }
        }
        catch (UnsupportedLookAndFeelException ex) {
            log.warn("Unable to set look and feel", (Throwable)ex);
        }
    }

    public static void setFont(@Nonnull Font font) {
        FontUIResource f = new FontUIResource(font);
        Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (!(value instanceof FontUIResource)) continue;
            UIManager.put(key, f);
        }
    }

    @Nullable
    public static TrayIcon createTrayIcon(@Nonnull Image icon, @Nonnull String title, final @Nonnull Frame frame) {
        if (!SystemTray.isSupported()) {
            return null;
        }
        SystemTray systemTray = SystemTray.getSystemTray();
        TrayIcon trayIcon = new TrayIcon(icon, title);
        trayIcon.setImageAutoSize(true);
        try {
            systemTray.add(trayIcon);
        }
        catch (AWTException ex) {
            log.debug("Unable to add system tray icon", (Throwable)ex);
            return trayIcon;
        }
        trayIcon.addMouseListener(new MouseAdapter(){

            @Override
            public void mouseClicked(MouseEvent e) {
                if (OSType.getOSType() == OSType.MacOS && !frame.isFocused()) {
                    frame.setVisible(false);
                    OSXUtil.requestForeground();
                }
                frame.setVisible(true);
                frame.setState(0);
            }
        });
        return trayIcon;
    }

    public static JButton createSwingButton(@Nonnull NavigationButton navigationButton, int iconSize, @Nullable BiConsumer<NavigationButton, JButton> specialCallback) {
        BufferedImage scaledImage = iconSize > 0 ? ImageUtil.resizeImage(navigationButton.getIcon(), iconSize, iconSize) : navigationButton.getIcon();
        JButton button = new JButton();
        button.setSize(scaledImage.getWidth(), scaledImage.getHeight());
        button.setToolTipText(navigationButton.getTooltip());
        button.setIcon(new ImageIcon(scaledImage));
        button.putClientProperty("substancelaf.internal.FlatLook", Boolean.TRUE);
        button.setFocusable(false);
        button.addActionListener(e -> {
            if (specialCallback != null) {
                specialCallback.accept(navigationButton, button);
            }
            if (navigationButton.getOnClick() != null) {
                navigationButton.getOnClick().run();
            }
        });
        if (navigationButton.getPopup() != null) {
            JPopupMenu popupMenu = new JPopupMenu();
            navigationButton.getPopup().forEach((name, callback) -> {
                JMenuItem menuItem = new JMenuItem((String)name);
                menuItem.addActionListener(e -> callback.run());
                popupMenu.add(menuItem);
            });
            button.setComponentPopupMenu(popupMenu);
        }
        navigationButton.setOnSelect(button::doClick);
        return button;
    }

    public static void removeButtonDecorations(AbstractButton button) {
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setOpaque(false);
    }

    public static void addModalTooltip(AbstractButton button, String on, String off) {
        button.setToolTipText(button.isSelected() ? on : off);
        button.addItemListener(l -> button.setToolTipText(button.isSelected() ? on : off));
    }

    public static void fastRemoveAll(Container c) {
        assert (SwingUtilities.isEventDispatchThread());
        c.invalidate();
        for (int i = 0; i < c.getComponentCount(); ++i) {
            Component ic = c.getComponent(i);
            if (ic instanceof Container) {
                SwingUtil.fastRemoveAll((Container)ic);
            }
            SwingUtil.pumpPendingEvents();
            ic.removeNotify();
        }
        c.removeAll();
    }

    public static void pumpPendingEvents() {
        EventQueue eq = Toolkit.getDefaultToolkit().getSystemEventQueue();
        if (eq.peekEvent() != null) {
            SecondaryLoop l = eq.createSecondaryLoop();
            SwingUtilities.invokeLater(l::exit);
            l.enter();
        }
    }
}

