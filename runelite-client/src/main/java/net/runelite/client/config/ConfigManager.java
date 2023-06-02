/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.annotations.VisibleForTesting
 *  com.google.common.base.Strings
 *  com.google.common.collect.ComparisonChain
 *  com.google.common.hash.Hasher
 *  com.google.common.hash.Hashing
 *  com.google.gson.Gson
 *  javax.annotation.Nonnull
 *  javax.annotation.Nullable
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  lombok.NonNull
 *  net.runelite.api.Client
 *  net.runelite.api.Player
 *  net.runelite.api.coords.WorldPoint
 *  net.runelite.api.events.AccountHashChanged
 *  net.runelite.api.events.PlayerChanged
 *  net.runelite.api.events.UsernameChanged
 *  net.runelite.api.events.WorldChanged
 *  net.runelite.http.api.config.ConfigPatch
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package net.runelite.client.config;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Strings;
import com.google.common.collect.ComparisonChain;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import com.google.gson.Gson;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import lombok.NonNull;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.AccountHashChanged;
import net.runelite.api.events.PlayerChanged;
import net.runelite.api.events.UsernameChanged;
import net.runelite.api.events.WorldChanged;
import net.runelite.client.RuneLite;
import net.runelite.client.account.AccountSession;
import net.runelite.client.config.Alpha;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigClient;
import net.runelite.client.config.ConfigDescriptor;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigInvocationHandler;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigItemDescriptor;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.ConfigSectionDescriptor;
import net.runelite.client.config.Keybind;
import net.runelite.client.config.ModifierlessKeybind;
import net.runelite.client.config.Range;
import net.runelite.client.config.RuneScapeProfile;
import net.runelite.client.config.RuneScapeProfileType;
import net.runelite.client.config.Units;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ClientShutdown;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.events.RuneScapeProfileChanged;
import net.runelite.client.util.ColorUtil;
import net.runelite.http.api.config.ConfigPatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class ConfigManager {
    private static final Logger log = LoggerFactory.getLogger(ConfigManager.class);
    public static final String RSPROFILE_GROUP = "rsprofile";
    private static final String RSPROFILE_DISPLAY_NAME = "displayName";
    private static final String RSPROFILE_TYPE = "type";
    private static final String RSPROFILE_LOGIN_HASH = "loginHash";
    private static final String RSPROFILE_LOGIN_SALT = "loginSalt";
    private static final String RSPROFILE_ACCOUNT_HASH = "accountHash";
    private static final DateFormat TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
    private static final int KEY_SPLITTER_GROUP = 0;
    private static final int KEY_SPLITTER_PROFILE = 1;
    private static final int KEY_SPLITTER_KEY = 2;
    private final File settingsFileInput;
    private final EventBus eventBus;
    private final Gson gson;
    @Nonnull
    private final ConfigClient configClient;
    private final boolean vanilla;
    private AccountSession session;
    private File propertiesFile;
    @Nullable
    private final Client client;
    private final ConfigInvocationHandler handler = new ConfigInvocationHandler(this);
    private final Map<String, String> pendingChanges = new HashMap<String, String>();
    private Properties properties = new Properties();
    @Nullable
    private String rsProfileKey;

    @Inject
    public ConfigManager(@Named(value="config") File config, ScheduledExecutorService scheduledExecutorService, EventBus eventBus, @Nullable Client client, Gson gson, ConfigClient configClient, @Named(value="vanilla") boolean vanilla) {
        this.settingsFileInput = config;
        this.eventBus = eventBus;
        this.client = client;
        this.propertiesFile = this.getPropertiesFile();
        this.gson = gson;
        this.configClient = configClient;
        this.vanilla = vanilla;
        scheduledExecutorService.scheduleWithFixedDelay(this::sendConfig, 30L, 300L, TimeUnit.SECONDS);
    }

    public String getRSProfileKey() {
        return this.rsProfileKey;
    }

    public final void switchSession(AccountSession session) {
        this.sendConfig();
        if (session == null) {
            this.session = null;
            this.configClient.setUuid(null);
        } else {
            this.session = session;
            this.configClient.setUuid(session.getUuid());
        }
        this.propertiesFile = this.getPropertiesFile();
        this.load();
    }

    private File getLocalPropertiesFile() {
        return this.settingsFileInput;
    }

    private File getPropertiesFile() {
        if (this.session == null || this.session.getUsername() == null) {
            return this.getLocalPropertiesFile();
        }
        File profileDir = new File(RuneLite.PROFILES_DIR, this.session.getUsername().toLowerCase());
        return new File(profileDir, RuneLite.DEFAULT_CONFIG_FILE.getName());
    }

    public void load() {
        Map<String, String> configuration;
        if (this.vanilla) {
            return;
        }
        if (this.session == null) {
            this.loadFromFile();
            return;
        }
        try {
            configuration = this.configClient.get();
        }
        catch (IOException ex) {
            log.debug("Unable to load configuration from client, using saved configuration from disk", (Throwable)ex);
            this.loadFromFile();
            return;
        }
        if (configuration == null || configuration.isEmpty()) {
            log.debug("No configuration from client, using saved configuration on disk");
            this.loadFromFile();
            return;
        }
        Properties newProperties = new Properties();
        newProperties.putAll(configuration);
        log.debug("Loading in config from server");
        this.swapProperties(newProperties, false);
        try {
            this.saveToFile(this.propertiesFile);
            log.debug("Updated configuration on disk with the latest version");
        }
        catch (IOException ex) {
            log.warn("Unable to update configuration on disk", (Throwable)ex);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void swapProperties(Properties newProperties, boolean saveToServer) {
        Properties oldProperties;
        HashSet<Object> allKeys = new HashSet<Object>(newProperties.keySet());
        ConfigManager configManager = this;
        synchronized (configManager) {
            this.handler.invalidate();
            oldProperties = this.properties;
            this.properties = newProperties;
        }
        this.updateRSProfile();
        allKeys.addAll(oldProperties.keySet());
        for (Object e : allKeys) {
            String newValue;
            String[] split = ConfigManager.splitKey((String)e);
            if (split == null) continue;
            String groupName = split[0];
            String profile = split[1];
            String key = split[2];
            String oldValue = (String)oldProperties.get(e);
            if (Objects.equals(oldValue, newValue = (String)newProperties.get(e))) continue;
            log.debug("Loading configuration value {}: {}", e, (Object)newValue);
            ConfigChanged configChanged = new ConfigChanged();
            configChanged.setGroup(groupName);
            configChanged.setProfile(profile);
            configChanged.setKey(key);
            configChanged.setOldValue(oldValue);
            configChanged.setNewValue(newValue);
            this.eventBus.post(configChanged);
            if (!saveToServer) continue;
            Map<String, String> map = this.pendingChanges;
            synchronized (map) {
                this.pendingChanges.put((String)e, newValue);
            }
        }
    }

    private void syncPropertiesFromFile(File propertiesFile) {
        Properties properties = new Properties();
        try (FileInputStream in = new FileInputStream(propertiesFile);){
            properties.load(new InputStreamReader((InputStream)in, StandardCharsets.UTF_8));
        }
        catch (Exception e) {
            log.warn("Malformed properties, skipping update");
            return;
        }
        log.debug("Loading in config from disk for upload");
        this.swapProperties(properties, true);
    }

    public Future<Void> importLocal() {
        if (this.session == null) {
            return null;
        }
        File file = new File(this.propertiesFile.getParent(), this.propertiesFile.getName() + "." + TIME_FORMAT.format(new Date()));
        try {
            this.saveToFile(file);
        }
        catch (IOException e) {
            log.warn("Backup failed, skipping import", (Throwable)e);
            return null;
        }
        this.syncPropertiesFromFile(this.getLocalPropertiesFile());
        return this.sendConfig();
    }

    private synchronized void loadFromFile() {
        Properties newProperties = new Properties();
        try (FileInputStream in = new FileInputStream(this.propertiesFile);){
            newProperties.load(new InputStreamReader((InputStream)in, StandardCharsets.UTF_8));
        }
        catch (FileNotFoundException ex) {
            log.debug("Unable to load settings - no such file");
        }
        catch (IOException | IllegalArgumentException ex) {
            log.warn("Unable to load settings", (Throwable)ex);
        }
        log.debug("Loading in config from disk");
        this.swapProperties(newProperties, false);
    }

    private void saveToFile(File propertiesFile) throws IOException {
        if (this.vanilla) {
            return;
        }
        File parent = propertiesFile.getParentFile();
        parent.mkdirs();
        File tempFile = File.createTempFile("runelite", null, parent);
        try (FileOutputStream out = new FileOutputStream(tempFile);
             FileChannel channel = out.getChannel();
             OutputStreamWriter writer = new OutputStreamWriter((OutputStream)out, StandardCharsets.UTF_8);){
            channel.lock();
            this.properties.store(writer, "RuneLite configuration");
            channel.force(true);
        }
        try {
            Files.move(tempFile.toPath(), propertiesFile.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        }
        catch (AtomicMoveNotSupportedException ex) {
            log.debug("atomic move not supported", (Throwable)ex);
            Files.move(tempFile.toPath(), propertiesFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
    }

    public <T extends Config> T getConfig(Class<T> clazz) {
        if (!Modifier.isPublic(clazz.getModifiers())) {
            throw new RuntimeException("Non-public configuration classes can't have default methods invoked");
        }
        Config t = (Config)Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, (InvocationHandler)this.handler);
        return (T)t;
    }

    public List<String> getConfigurationKeys(String prefix) {
        return this.properties.keySet().stream().map(String.class::cast).filter(k -> k.startsWith(prefix)).collect(Collectors.toList());
    }

    public List<String> getRSProfileConfigurationKeys(String group, String profile, String keyPrefix) {
        if (profile == null) {
            return Collections.emptyList();
        }
        assert (profile.startsWith(RSPROFILE_GROUP));
        String prefix = group + "." + profile + "." + keyPrefix;
        return this.properties.keySet().stream().map(String.class::cast).filter(k -> k.startsWith(prefix)).map(k -> ConfigManager.splitKey(k)[2]).collect(Collectors.toList());
    }

    public static String getWholeKey(String groupName, String profile, String key) {
        if (profile == null) {
            return groupName + "." + key;
        }
        return groupName + "." + profile + "." + key;
    }

    public String getConfiguration(String groupName, String key) {
        return this.getConfiguration(groupName, null, key);
    }

    public String getRSProfileConfiguration(String groupName, String key) {
        String rsProfileKey = this.rsProfileKey;
        if (rsProfileKey == null) {
            return null;
        }
        return this.getConfiguration(groupName, rsProfileKey, key);
    }

    public String getConfiguration(String groupName, String profile, String key) {
        return this.properties.getProperty(ConfigManager.getWholeKey(groupName, profile, key));
    }

    public <T> T getConfiguration(String groupName, String key, Type clazz) {
        return this.getConfiguration(groupName, null, key, clazz);
    }

    public <T> T getRSProfileConfiguration(String groupName, String key, Type clazz) {
        String rsProfileKey = this.rsProfileKey;
        if (rsProfileKey == null) {
            return null;
        }
        return this.getConfiguration(groupName, rsProfileKey, key, clazz);
    }

    public <T> T getConfiguration(String groupName, String profile, String key, Type type) {
        String value = this.getConfiguration(groupName, profile, key);
        if (!Strings.isNullOrEmpty((String)value)) {
            try {
                return (T)this.stringToObject(value, type);
            }
            catch (Exception e) {
                log.warn("Unable to unmarshal {} ", (Object)ConfigManager.getWholeKey(groupName, profile, key), (Object)e);
            }
        }
        return null;
    }

    public void setConfiguration(String groupName, String key, String value) {
        this.setConfiguration(groupName, (String)null, key, value);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void setConfiguration(String groupName, String profile, String key, @NonNull String value) {
        String oldValue;
        if (value == null) {
            throw new NullPointerException("value is marked non-null but is null");
        }
        if (Strings.isNullOrEmpty((String)groupName) || Strings.isNullOrEmpty((String)key) || key.indexOf(58) != -1) {
            throw new IllegalArgumentException();
        }
        assert (!key.startsWith("rsprofile."));
        String wholeKey = ConfigManager.getWholeKey(groupName, profile, key);
        Object object = this;
        synchronized (object) {
            oldValue = (String)this.properties.setProperty(wholeKey, value);
        }
        if (Objects.equals(oldValue, value)) {
            return;
        }
        log.debug("Setting configuration value for {} to {}", (Object)wholeKey, (Object)value);
        this.handler.invalidate();
        object = this.pendingChanges;
        synchronized (object) {
            this.pendingChanges.put(wholeKey, value);
        }
        ConfigChanged configChanged = new ConfigChanged();
        configChanged.setGroup(groupName);
        configChanged.setProfile(profile);
        configChanged.setKey(key);
        configChanged.setOldValue(oldValue);
        configChanged.setNewValue(value);
        this.eventBus.post(configChanged);
    }

    public <T> void setConfiguration(String groupName, String profile, String key, T value) {
        this.setConfiguration(groupName, profile, key, this.objectToString(value));
    }

    public <T> void setConfiguration(String groupName, String key, T value) {
        this.setConfiguration(groupName, null, key, value);
    }

    public <T> void setRSProfileConfiguration(String groupName, String key, T value) {
        String rsProfileKey = this.rsProfileKey;
        if (rsProfileKey == null) {
            if (this.client == null) {
                log.warn("trying to use profile without injected client");
                return;
            }
            String displayName = null;
            Player p = this.client.getLocalPlayer();
            if (p == null) {
                log.warn("trying to create profile without display name");
            } else {
                displayName = p.getName();
            }
            RuneScapeProfile prof = this.findRSProfile(this.getRSProfiles(), RuneScapeProfileType.getCurrent(this.client), displayName, true);
            if (prof == null) {
                log.warn("trying to create a profile while not logged in");
                return;
            }
            this.rsProfileKey = rsProfileKey = prof.getKey();
            log.debug("RS profile changed to {}", (Object)rsProfileKey);
            this.eventBus.post(new RuneScapeProfileChanged());
        }
        this.setConfiguration(groupName, rsProfileKey, key, value);
    }

    public void unsetConfiguration(String groupName, String key) {
        this.unsetConfiguration(groupName, null, key);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void unsetConfiguration(String groupName, String profile, String key) {
        String oldValue;
        assert (!key.startsWith("rsprofile."));
        String wholeKey = ConfigManager.getWholeKey(groupName, profile, key);
        Object object = this;
        synchronized (object) {
            oldValue = (String)this.properties.remove(wholeKey);
        }
        if (oldValue == null) {
            return;
        }
        log.debug("Unsetting configuration value for {}", (Object)wholeKey);
        this.handler.invalidate();
        object = this.pendingChanges;
        synchronized (object) {
            this.pendingChanges.put(wholeKey, null);
        }
        ConfigChanged configChanged = new ConfigChanged();
        configChanged.setGroup(groupName);
        configChanged.setProfile(profile);
        configChanged.setKey(key);
        configChanged.setOldValue(oldValue);
        this.eventBus.post(configChanged);
    }

    public void unsetRSProfileConfiguration(String groupName, String key) {
        String rsProfileKey = this.rsProfileKey;
        if (rsProfileKey == null) {
            return;
        }
        this.unsetConfiguration(groupName, rsProfileKey, key);
    }

    public ConfigDescriptor getConfigDescriptor(Config configurationProxy) {
        Class<?> inter = configurationProxy.getClass().getInterfaces()[0];
        ConfigGroup group = inter.getAnnotation(ConfigGroup.class);
        if (group == null) {
            throw new IllegalArgumentException("Not a config group");
        }
        List<ConfigSectionDescriptor> sections = Arrays.stream(inter.getDeclaredFields()).filter(m -> m.isAnnotationPresent(ConfigSection.class) && m.getType() == String.class).map(m -> {
            try {
                return new ConfigSectionDescriptor(String.valueOf(m.get(inter)), m.getDeclaredAnnotation(ConfigSection.class));
            }
            catch (IllegalAccessException e) {
                log.warn("Unable to load section {}::{}", (Object)inter.getSimpleName(), (Object)m.getName());
                return null;
            }
        }).filter(Objects::nonNull).sorted((a, b) -> ComparisonChain.start().compare(a.getSection().position(), b.getSection().position()).compare((Comparable)((Object)a.getSection().name()), (Comparable)((Object)b.getSection().name())).result()).collect(Collectors.toList());
        List<ConfigItemDescriptor> items = Arrays.stream(inter.getMethods()).filter(m -> m.getParameterCount() == 0 && m.isAnnotationPresent(ConfigItem.class)).map(m -> new ConfigItemDescriptor(m.getDeclaredAnnotation(ConfigItem.class), m.getGenericReturnType(), m.getDeclaredAnnotation(Range.class), m.getDeclaredAnnotation(Alpha.class), m.getDeclaredAnnotation(Units.class))).sorted((a, b) -> ComparisonChain.start().compare(a.getItem().position(), b.getItem().position()).compare((Comparable)((Object)a.getItem().name()), (Comparable)((Object)b.getItem().name())).result()).collect(Collectors.toList());
        return new ConfigDescriptor(group, sections, items);
    }

    public void setDefaultConfiguration(Object proxy, boolean override) {
        Class<?> clazz = proxy.getClass().getInterfaces()[0];
        ConfigGroup group = clazz.getAnnotation(ConfigGroup.class);
        if (group == null) {
            return;
        }
        for (Method method : clazz.getDeclaredMethods()) {
            Object defaultValue;
            String current;
            ConfigItem item = method.getAnnotation(ConfigItem.class);
            if (item == null || method.getParameterCount() != 0) continue;
            if (!method.isDefault()) {
                if (!override || (current = this.getConfiguration(group.value(), item.keyName())) == null) continue;
                this.unsetConfiguration(group.value(), item.keyName());
                continue;
            }
            if (!override && (current = this.getConfiguration(group.value(), item.keyName(), method.getGenericReturnType())) != null) continue;
            try {
                defaultValue = ConfigInvocationHandler.callDefaultMethod(proxy, method, null);
            }
            catch (Throwable ex) {
                log.warn(null, ex);
                continue;
            }
            String current2 = this.getConfiguration(group.value(), item.keyName());
            String valueString = this.objectToString(defaultValue);
            if (Objects.equals(current2, valueString) || Strings.isNullOrEmpty((String)current2) && Strings.isNullOrEmpty((String)valueString)) continue;
            log.debug("Setting default configuration value for {}.{} to {}", new Object[]{group.value(), item.keyName(), defaultValue});
            this.setConfiguration(group.value(), item.keyName(), valueString);
        }
    }

    Object stringToObject(String str, Type type) {
        ParameterizedType parameterizedType;
        if (type == Boolean.TYPE || type == Boolean.class) {
            return Boolean.parseBoolean(str);
        }
        if (type == Integer.TYPE || type == Integer.class) {
            return Integer.parseInt(str);
        }
        if (type == Long.TYPE || type == Long.class) {
            return Long.parseLong(str);
        }
        if (type == Double.TYPE || type == Double.class) {
            return Double.parseDouble(str);
        }
        if (type == Color.class) {
            return ColorUtil.fromString(str);
        }
        if (type == Dimension.class) {
            String[] splitStr = str.split("x");
            int width = Integer.parseInt(splitStr[0]);
            int height = Integer.parseInt(splitStr[1]);
            return new Dimension(width, height);
        }
        if (type == Point.class) {
            String[] splitStr = str.split(":");
            int width = Integer.parseInt(splitStr[0]);
            int height = Integer.parseInt(splitStr[1]);
            return new Point(width, height);
        }
        if (type == Rectangle.class) {
            String[] splitStr = str.split(":");
            int x = Integer.parseInt(splitStr[0]);
            int y = Integer.parseInt(splitStr[1]);
            int width = Integer.parseInt(splitStr[2]);
            int height = Integer.parseInt(splitStr[3]);
            return new Rectangle(x, y, width, height);
        }
        if (type instanceof Class && ((Class)type).isEnum()) {
            return Enum.valueOf((Class)type, str);
        }
        if (type == Instant.class) {
            return Instant.parse(str);
        }
        if (type == Keybind.class || type == ModifierlessKeybind.class) {
            String[] splitStr = str.split(":");
            int code = Integer.parseInt(splitStr[0]);
            int mods = Integer.parseInt(splitStr[1]);
            if (type == ModifierlessKeybind.class) {
                return new ModifierlessKeybind(code, mods);
            }
            return new Keybind(code, mods);
        }
        if (type == WorldPoint.class) {
            String[] splitStr = str.split(":");
            int x = Integer.parseInt(splitStr[0]);
            int y = Integer.parseInt(splitStr[1]);
            int plane = Integer.parseInt(splitStr[2]);
            return new WorldPoint(x, y, plane);
        }
        if (type == Duration.class) {
            return Duration.ofMillis(Long.parseLong(str));
        }
        if (type == byte[].class) {
            return Base64.getUrlDecoder().decode(str);
        }
        if (type instanceof ParameterizedType && (parameterizedType = (ParameterizedType)type).getRawType() == Set.class) {
            return this.gson.fromJson(str, (Type)parameterizedType);
        }
        return str;
    }

    @Nullable
    String objectToString(Object object) {
        if (object instanceof Color) {
            return String.valueOf(((Color)object).getRGB());
        }
        if (object instanceof Enum) {
            return ((Enum)object).name();
        }
        if (object instanceof Dimension) {
            Dimension d = (Dimension)object;
            return d.width + "x" + d.height;
        }
        if (object instanceof Point) {
            Point p = (Point)object;
            return p.x + ":" + p.y;
        }
        if (object instanceof Rectangle) {
            Rectangle r = (Rectangle)object;
            return r.x + ":" + r.y + ":" + r.width + ":" + r.height;
        }
        if (object instanceof Instant) {
            return ((Instant)object).toString();
        }
        if (object instanceof Keybind) {
            Keybind k = (Keybind)object;
            return k.getKeyCode() + ":" + k.getModifiers();
        }
        if (object instanceof WorldPoint) {
            WorldPoint wp = (WorldPoint)object;
            return wp.getX() + ":" + wp.getY() + ":" + wp.getPlane();
        }
        if (object instanceof Duration) {
            return Long.toString(((Duration)object).toMillis());
        }
        if (object instanceof byte[]) {
            return Base64.getUrlEncoder().encodeToString((byte[])object);
        }
        if (object instanceof Set) {
            return this.gson.toJson(object, Set.class);
        }
        return object == null ? null : object.toString();
    }

    @Subscribe(priority=-100.0f)
    private void onClientShutdown(ClientShutdown e) {
        CompletableFuture<Void> f = this.sendConfig();
        if (f != null) {
            e.waitFor(f);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Nullable
    private CompletableFuture<Void> sendConfig() {
        CompletableFuture<Void> future = null;
        Map<String, String> map = this.pendingChanges;
        synchronized (map) {
            if (this.pendingChanges.isEmpty()) {
                return null;
            }
            if (this.session != null) {
                ConfigPatch patch = new ConfigPatch();
                for (Map.Entry<String, String> entry : this.pendingChanges.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (value == null) {
                        patch.getUnset().add(key);
                        continue;
                    }
                    patch.getEdit().put(key, value);
                }
                future = this.configClient.patch(patch);
            }
            this.pendingChanges.clear();
        }
        try {
            this.saveToFile(this.propertiesFile);
        }
        catch (IOException ex) {
            log.warn("unable to save configuration file", (Throwable)ex);
        }
        return future;
    }

    public List<RuneScapeProfile> getRSProfiles() {
        String prefix = "rsprofile.rsprofile.";
        HashSet<String> profileKeys = new HashSet<String>();
        for (Object oKey : this.properties.keySet()) {
            String[] split;
            String key2 = (String)oKey;
            if (!key2.startsWith(prefix) || (split = ConfigManager.splitKey(key2)) == null) continue;
            profileKeys.add(split[1]);
        }
        return profileKeys.stream().map(key -> {
            Long accid = (Long)this.getConfiguration(RSPROFILE_GROUP, (String)key, RSPROFILE_ACCOUNT_HASH, Long.TYPE);
            RuneScapeProfile prof = new RuneScapeProfile(this.getConfiguration(RSPROFILE_GROUP, (String)key, RSPROFILE_DISPLAY_NAME), (RuneScapeProfileType)((Object)((Object)this.getConfiguration(RSPROFILE_GROUP, (String)key, RSPROFILE_TYPE, (Type)((Object)RuneScapeProfileType.class)))), (byte[])this.getConfiguration(RSPROFILE_GROUP, (String)key, RSPROFILE_LOGIN_HASH, (Type)((Object)byte[].class)), accid == null ? -1L : accid, (String)key);
            return prof;
        }).collect(Collectors.toList());
    }

    private synchronized RuneScapeProfile findRSProfile(List<RuneScapeProfile> profiles, RuneScapeProfileType type, String displayName, boolean create) {
        byte[] arrby;
        byte[] loginHash;
        String username = this.client.getUsername();
        long accountHash = this.client.getAccountHash();
        if (accountHash == -1L && username == null) {
            return null;
        }
        byte[] salt = null;
        if (username != null) {
            salt = (byte[])this.getConfiguration(RSPROFILE_GROUP, RSPROFILE_LOGIN_SALT, (Type)((Object)byte[].class));
            if (salt == null) {
                salt = new byte[15];
                new SecureRandom().nextBytes(salt);
                log.info("creating new salt as there is no existing one {}", (Object)Base64.getUrlEncoder().encodeToString(salt));
                this.setConfiguration(RSPROFILE_GROUP, RSPROFILE_LOGIN_SALT, salt);
            }
            Hasher h = Hashing.sha512().newHasher();
            h.putBytes(salt);
            h.putString((CharSequence)username.toLowerCase(Locale.US), StandardCharsets.UTF_8);
            loginHash = h.hash().asBytes();
        } else {
            loginHash = null;
        }
        Set matches = Collections.emptySet();
        if (accountHash != -1L) {
            matches = profiles.stream().filter(p -> p.getType() == type && accountHash == p.getAccountHash()).collect(Collectors.toSet());
        }
        if (matches.isEmpty() && loginHash != null) {
            matches = profiles.stream().filter(p -> p.getType() == type && Arrays.equals(loginHash, p.getLoginHash())).collect(Collectors.toSet());
        }
        if (matches.size() > 1) {
            log.warn("multiple matching profiles");
        }
        if (matches.size() >= 1) {
            RuneScapeProfile profile = (RuneScapeProfile)matches.iterator().next();
            if (profile.getAccountHash() == -1L && accountHash != -1L) {
                int upgrades = 0;
                for (RuneScapeProfile p2 : profiles) {
                    if (p2.getAccountHash() != -1L || !Arrays.equals(p2.getLoginHash(), loginHash)) continue;
                    this.setConfiguration(RSPROFILE_GROUP, p2.getKey(), RSPROFILE_ACCOUNT_HASH, accountHash);
                    ++upgrades;
                }
                log.info("Attaching account id to {} profiles", (Object)upgrades);
            }
            return profile;
        }
        if (!create) {
            return null;
        }
        Set keys = profiles.stream().map(RuneScapeProfile::getKey).collect(Collectors.toSet());
        if (accountHash == -1L) {
            arrby = Arrays.copyOf(loginHash, 6);
        } else {
            byte[] arrby2 = new byte[6];
            arrby2[0] = (byte)accountHash;
            arrby2[1] = (byte)(accountHash >> 8);
            arrby2[2] = (byte)(accountHash >> 16);
            arrby2[3] = (byte)(accountHash >> 24);
            arrby2[4] = (byte)(accountHash >> 32);
            arrby = arrby2;
            arrby2[5] = (byte)(accountHash >> 40);
        }
        byte[] key = arrby;
        key[0] = (byte)(key[0] + type.ordinal());
        for (int i = 0; i < 255; ++i) {
            String keyStr = "rsprofile." + Base64.getUrlEncoder().encodeToString(key);
            if (!keys.contains(keyStr)) {
                log.info("creating new profile {} for username {} account hash {} ({}) salt {}", new Object[]{keyStr, username, accountHash, type, salt == null ? "null" : Base64.getUrlEncoder().encodeToString(salt)});
                if (loginHash != null) {
                    this.setConfiguration(RSPROFILE_GROUP, keyStr, RSPROFILE_LOGIN_HASH, loginHash);
                }
                if (accountHash != -1L) {
                    this.setConfiguration(RSPROFILE_GROUP, keyStr, RSPROFILE_ACCOUNT_HASH, accountHash);
                }
                this.setConfiguration(RSPROFILE_GROUP, keyStr, RSPROFILE_TYPE, (Object)((Object)type));
                if (displayName != null) {
                    this.setConfiguration(RSPROFILE_GROUP, keyStr, RSPROFILE_DISPLAY_NAME, displayName);
                }
                return new RuneScapeProfile(displayName, type, loginHash, accountHash, keyStr);
            }
            key[1] = (byte)(key[1] + 1);
        }
        throw new RuntimeException("too many rs profiles");
    }

    private void updateRSProfile() {
        String key;
        if (this.client == null) {
            return;
        }
        List<RuneScapeProfile> profiles = this.getRSProfiles();
        RuneScapeProfile prof = this.findRSProfile(profiles, RuneScapeProfileType.getCurrent(this.client), null, false);
        String string = key = prof == null ? null : prof.getKey();
        if (Objects.equals(key, this.rsProfileKey)) {
            return;
        }
        this.rsProfileKey = key;
        log.debug("RS profile changed to {}", (Object)key);
        this.eventBus.post(new RuneScapeProfileChanged());
    }

    @Subscribe
    private void onUsernameChanged(UsernameChanged ev) {
        this.updateRSProfile();
    }

    @Subscribe
    private void onAccountHashChanged(AccountHashChanged ev) {
        this.updateRSProfile();
    }

    @Subscribe
    private void onWorldChanged(WorldChanged ev) {
        this.updateRSProfile();
    }

    @Subscribe
    private void onPlayerChanged(PlayerChanged ev) {
        if (ev.getPlayer() == this.client.getLocalPlayer()) {
            String name = ev.getPlayer().getName();
            this.setRSProfileConfiguration(RSPROFILE_GROUP, RSPROFILE_DISPLAY_NAME, name);
        }
    }

    @Nullable
    @VisibleForTesting
    static String[] splitKey(String key) {
        int i = key.indexOf(46);
        if (i == -1) {
            return null;
        }
        String group = key.substring(0, i);
        String profile = null;
        if ((key = key.substring(i + 1)).startsWith("rsprofile.")) {
            i = key.indexOf(46, RSPROFILE_GROUP.length() + 2);
            profile = key.substring(0, i);
            key = key.substring(i + 1);
        }
        return new String[]{group, profile, key};
    }
}

