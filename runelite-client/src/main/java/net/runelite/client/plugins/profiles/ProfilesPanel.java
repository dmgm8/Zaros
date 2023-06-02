/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  javax.inject.Inject
 *  javax.inject.Singleton
 *  net.runelite.api.Client
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package net.runelite.client.plugins.profiles;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import javax.annotation.Nullable;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import net.runelite.api.Client;
import net.runelite.client.plugins.profiles.ProfilePanel;
import net.runelite.client.plugins.profiles.ProfilesConfig;
import net.runelite.client.plugins.profiles.ProfilesPlugin;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.DynamicGridLayout;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.PluginPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
class ProfilesPanel
extends PluginPanel {
    private static final Logger log = LoggerFactory.getLogger(ProfilesPanel.class);
    private static final int iterations = 100000;
    private static final String UNLOCK_PASSWORD = "Encryption Password";
    private static final String ACCOUNT_USERNAME = "Account Username";
    private static final String ACCOUNT_LABEL = "Account Label";
    private static final String PASSWORD_LABEL = "Account Password";
    private static final String HELP = "To add and load accounts, first enter a password into the Encryption Password field then press %s. <br /><br /> You can now add as many accounts as you would like. <br /><br /> The next time you restart Zaros, enter your encryption password and click load accounts to see the accounts you entered.";
    @Inject
    @Nullable
    private Client client;
    @Inject
    private ProfilesConfig profilesConfig;
    @Inject
    ProfilesPlugin profilesPlugin;
    private final JPasswordField txtDecryptPassword = new JPasswordField("Encryption Password");
    private final JTextField txtAccountLabel = new JTextField("Account Label");
    private final JPasswordField txtAccountLogin = new JPasswordField("Account Username");
    private final JPasswordField txtPasswordLogin = new JPasswordField("Account Password");
    private final JPanel profilesPanel = new JPanel();
    private final JPanel accountPanel = new JPanel();
    private final JPanel loginPanel = new JPanel();

    ProfilesPanel() {
    }

    void init() {
        String LOAD_ACCOUNTS = this.profilesConfig.salt().length() == 0 ? "Save" : "Unlock";
        this.setLayout(new BorderLayout(0, 10));
        this.setBackground(ColorScheme.DARK_GRAY_COLOR);
        this.setBorder(new EmptyBorder(10, 10, 10, 10));
        Font smallFont = FontManager.getRunescapeSmallFont();
        JPanel helpPanel = new JPanel();
        helpPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        helpPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        helpPanel.setLayout(new DynamicGridLayout(1, 1));
        JLabel helpLabel = new JLabel(ProfilesPanel.htmlLabel(String.format(HELP, this.profilesConfig.salt().length() == 0 ? "save" : "unlock")));
        helpLabel.setFont(smallFont);
        helpPanel.add(helpLabel);
        this.loginPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        this.loginPanel.setBorder(new EmptyBorder(10, 10, 10, 3));
        this.loginPanel.setLayout(new DynamicGridLayout(0, 1, 0, 5));
        this.txtDecryptPassword.setEchoChar('\u0000');
        this.txtDecryptPassword.setForeground(ColorScheme.LIGHT_GRAY_COLOR);
        this.txtDecryptPassword.setToolTipText(UNLOCK_PASSWORD);
        this.txtDecryptPassword.addActionListener(e -> this.decryptAccounts());
        this.txtDecryptPassword.addFocusListener(new FocusListener(){

            @Override
            public void focusGained(FocusEvent e) {
                if (String.valueOf(ProfilesPanel.this.txtDecryptPassword.getPassword()).equals(ProfilesPanel.UNLOCK_PASSWORD)) {
                    ProfilesPanel.this.txtDecryptPassword.setText("");
                    ProfilesPanel.this.txtDecryptPassword.setEchoChar('*');
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (ProfilesPanel.this.txtDecryptPassword.getPassword().length == 0) {
                    ProfilesPanel.this.txtDecryptPassword.setText(ProfilesPanel.UNLOCK_PASSWORD);
                    ProfilesPanel.this.txtDecryptPassword.setEchoChar('\u0000');
                }
            }
        });
        JButton btnLoadAccounts = new JButton(LOAD_ACCOUNTS);
        btnLoadAccounts.setToolTipText(LOAD_ACCOUNTS);
        btnLoadAccounts.addMouseListener(new MouseListener(){

            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                ProfilesPanel.this.decryptAccounts();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        this.loginPanel.add(this.txtDecryptPassword);
        this.loginPanel.add(btnLoadAccounts);
        this.accountPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        this.accountPanel.setBorder(new EmptyBorder(10, 10, 10, 3));
        this.accountPanel.setLayout(new DynamicGridLayout(0, 1, 0, 5));
        this.txtAccountLabel.setForeground(ColorScheme.LIGHT_GRAY_COLOR);
        this.txtAccountLabel.addFocusListener(new FocusListener(){

            @Override
            public void focusGained(FocusEvent e) {
                if (ProfilesPanel.this.txtAccountLabel.getText().equals(ProfilesPanel.ACCOUNT_LABEL)) {
                    ProfilesPanel.this.txtAccountLabel.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (ProfilesPanel.this.txtAccountLabel.getText().isEmpty()) {
                    ProfilesPanel.this.txtAccountLabel.setText(ProfilesPanel.ACCOUNT_LABEL);
                }
            }
        });
        this.txtAccountLogin.setEchoChar('\u0000');
        this.txtAccountLogin.setForeground(ColorScheme.LIGHT_GRAY_COLOR);
        this.txtAccountLogin.addFocusListener(new FocusListener(){

            @Override
            public void focusGained(FocusEvent e) {
                if (ProfilesPanel.ACCOUNT_USERNAME.equals(String.valueOf(ProfilesPanel.this.txtAccountLogin.getPassword()))) {
                    ProfilesPanel.this.txtAccountLogin.setText("");
                    if (ProfilesPanel.this.profilesPlugin.isStreamerMode()) {
                        ProfilesPanel.this.txtAccountLogin.setEchoChar('*');
                    }
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (ProfilesPanel.this.txtAccountLogin.getPassword().length == 0) {
                    ProfilesPanel.this.txtAccountLogin.setText(ProfilesPanel.ACCOUNT_USERNAME);
                    ProfilesPanel.this.txtAccountLogin.setEchoChar('\u0000');
                }
            }
        });
        this.txtPasswordLogin.setEchoChar('\u0000');
        this.txtPasswordLogin.setForeground(ColorScheme.LIGHT_GRAY_COLOR);
        this.txtPasswordLogin.setToolTipText(PASSWORD_LABEL);
        this.txtPasswordLogin.addFocusListener(new FocusListener(){

            @Override
            public void focusGained(FocusEvent e) {
                if (ProfilesPanel.PASSWORD_LABEL.equals(String.valueOf(ProfilesPanel.this.txtPasswordLogin.getPassword()))) {
                    ProfilesPanel.this.txtPasswordLogin.setText("");
                    ProfilesPanel.this.txtPasswordLogin.setEchoChar('*');
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (ProfilesPanel.this.txtPasswordLogin.getPassword().length == 0) {
                    ProfilesPanel.this.txtPasswordLogin.setText(ProfilesPanel.PASSWORD_LABEL);
                    ProfilesPanel.this.txtPasswordLogin.setEchoChar('\u0000');
                }
            }
        });
        final JButton btnAddAccount = new JButton("Add Account");
        btnAddAccount.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        btnAddAccount.addActionListener(e -> {
            String labelText = String.valueOf(this.txtAccountLabel.getText());
            String loginText = String.valueOf(this.txtAccountLogin.getPassword());
            String passwordText = String.valueOf(this.txtPasswordLogin.getPassword());
            if (labelText.equals(ACCOUNT_LABEL) || loginText.equals(ACCOUNT_USERNAME)) {
                return;
            }
            String data = this.profilesPlugin.isRememberPassword() && this.txtPasswordLogin.getPassword() != null ? labelText + ":" + loginText + ":" + passwordText : labelText + ":" + loginText;
            try {
                if (!this.addProfile(data)) {
                    return;
                }
                this.redrawProfiles();
            }
            catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException ex) {
                log.error(e.toString());
            }
            this.txtAccountLabel.setText(ACCOUNT_LABEL);
            this.txtAccountLogin.setText(ACCOUNT_USERNAME);
            this.txtAccountLogin.setEchoChar('\u0000');
            this.txtPasswordLogin.setText(PASSWORD_LABEL);
            this.txtPasswordLogin.setEchoChar('\u0000');
        });
        this.txtAccountLogin.addKeyListener(new KeyAdapter(){

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 10) {
                    btnAddAccount.doClick();
                    btnAddAccount.requestFocus();
                }
            }
        });
        this.txtAccountLogin.addMouseListener(new MouseListener(){

            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        this.accountPanel.add(this.txtAccountLabel);
        this.accountPanel.add(this.txtAccountLogin);
        if (this.profilesPlugin.isRememberPassword()) {
            this.accountPanel.add(this.txtPasswordLogin);
        }
        this.accountPanel.add(btnAddAccount);
        this.add((Component)helpPanel, "North");
        this.add((Component)this.loginPanel, "Center");
    }

    private void decryptAccounts() {
        if (this.txtDecryptPassword.getPassword().length == 0 || String.valueOf(this.txtDecryptPassword.getPassword()).equals(UNLOCK_PASSWORD)) {
            ProfilesPanel.showErrorMessage("Unable to load data", "Please enter a password!");
            return;
        }
        boolean error = false;
        try {
            this.redrawProfiles();
        }
        catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException ex) {
            error = true;
            ProfilesPanel.showErrorMessage("Unable to load data", "Incorrect password!");
            this.txtDecryptPassword.setText("");
        }
        if (error) {
            return;
        }
        this.remove(this.loginPanel);
        this.add((Component)this.accountPanel, "Center");
        this.profilesPanel.setLayout(new DynamicGridLayout(0, 1, 0, 3));
        this.add((Component)this.profilesPanel, "South");
    }

    void redrawProfiles() throws InvalidKeySpecException, NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchPaddingException {
        this.profilesPanel.removeAll();
        this.addAccounts(this.getProfileData());
        this.revalidate();
        this.repaint();
    }

    private void addAccount(String data) {
        ProfilePanel profile = new ProfilePanel(this.client, data, this.profilesPlugin, this);
        this.profilesPanel.add(profile);
        this.revalidate();
        this.repaint();
    }

    private void addAccounts(String data) {
        if (!(data = data.trim()).contains(":")) {
            return;
        }
        Arrays.stream(data.split("\\n")).forEach(this::addAccount);
    }

    private boolean addProfile(String data) throws InvalidKeySpecException, NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchPaddingException {
        return this.setProfileData(this.getProfileData() + data + "\n");
    }

    void removeProfile(String data) throws InvalidKeySpecException, NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchPaddingException {
        this.setProfileData(this.getProfileData().replaceAll(data + "\\n", ""));
        this.revalidate();
        this.repaint();
    }

    private void setSalt(byte[] bytes) {
        this.profilesConfig.salt(this.base64Encode(bytes));
    }

    private byte[] getSalt() {
        if (this.profilesConfig.salt().length() == 0) {
            return new byte[0];
        }
        return this.base64Decode(this.profilesConfig.salt());
    }

    private SecretKey getAesKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        if (this.getSalt().length == 0) {
            byte[] b = new byte[16];
            SecureRandom.getInstanceStrong().nextBytes(b);
            this.setSalt(b);
        }
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        PBEKeySpec spec = new PBEKeySpec(this.txtDecryptPassword.getPassword(), this.getSalt(), 100000, 128);
        return factory.generateSecret(spec);
    }

    private String getProfileData() throws InvalidKeySpecException, NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchPaddingException {
        String tmp = this.profilesConfig.profilesData();
        if (tmp.startsWith("\u00ac")) {
            tmp = tmp.substring(1);
            return ProfilesPanel.decryptText(this.base64Decode(tmp), this.getAesKey());
        }
        return tmp;
    }

    private boolean setProfileData(String data) throws InvalidKeySpecException, NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchPaddingException {
        if (this.txtDecryptPassword.getPassword().length == 0 || String.valueOf(this.txtDecryptPassword.getPassword()).equals(UNLOCK_PASSWORD)) {
            ProfilesPanel.showErrorMessage("Unable to save data", "Please enter a password!");
            return false;
        }
        byte[] enc = ProfilesPanel.encryptText(data, this.getAesKey());
        if (enc.length == 0) {
            return false;
        }
        String s = "\u00ac" + this.base64Encode(enc);
        this.profilesConfig.profilesData(s);
        return true;
    }

    private byte[] base64Decode(String data) {
        return Base64.getDecoder().decode(data);
    }

    private String base64Encode(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    private static byte[] encryptText(String text, SecretKey aesKey) throws NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchPaddingException {
        Cipher cipher = Cipher.getInstance("AES");
        SecretKeySpec newKey = new SecretKeySpec(aesKey.getEncoded(), "AES");
        cipher.init(1, newKey);
        return cipher.doFinal(text.getBytes());
    }

    private static String decryptText(byte[] enc, SecretKey aesKey) throws NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchPaddingException {
        Cipher cipher = Cipher.getInstance("AES");
        SecretKeySpec newKey = new SecretKeySpec(aesKey.getEncoded(), "AES");
        cipher.init(2, newKey);
        return new String(cipher.doFinal(enc));
    }

    private static void showErrorMessage(String title, String text) {
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, text, title, 0));
    }

    private static String htmlLabel(String text) {
        return "<html><body><span style = 'color:white'>" + text + "</span></body></html>";
    }
}

