/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  okhttp3.HttpUrl
 *  okhttp3.OkHttpClient
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package net.runelite.client.rs;

import java.applet.Applet;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Collection;
import java.util.function.Supplier;
import javax.swing.SwingUtilities;
import net.runelite.client.RuneLiteProperties;
import net.runelite.client.rs.ClientConfigLoader;
import net.runelite.client.rs.ClientUpdateCheckMode;
import net.runelite.client.rs.LocalClientConfigLoader;
import net.runelite.client.rs.RSAppletStub;
import net.runelite.client.rs.RSConfig;
import net.runelite.client.rs.RemoteClientConfigLoader;
import net.runelite.client.rs.WorldSupplier;
import net.runelite.client.ui.FatalErrorDialog;
import net.runelite.client.ui.SplashScreen;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientLoader
implements Supplier<Applet> {
    private static final Logger log = LoggerFactory.getLogger(ClientLoader.class);
    private static final int NUM_ATTEMPTS = 6;
    private final OkHttpClient okHttpClient;
    private final ClientConfigLoader clientConfigLoader;
    private ClientUpdateCheckMode updateCheckMode;
    private final WorldSupplier worldSupplier;
    private Object client;

    public ClientLoader(OkHttpClient okHttpClient, ClientUpdateCheckMode updateCheckMode, boolean localConfig) {
        this.okHttpClient = okHttpClient;
        this.clientConfigLoader = localConfig ? new LocalClientConfigLoader() : new RemoteClientConfigLoader(okHttpClient, HttpUrl.parse((String)RuneLiteProperties.getJavConfig()));
        this.updateCheckMode = updateCheckMode;
        this.worldSupplier = new WorldSupplier(okHttpClient);
    }

    @Override
    public synchronized Applet get() {
        if (this.client == null) {
            this.client = this.doLoad();
        }
        if (this.client instanceof Throwable) {
            throw new RuntimeException((Throwable)this.client);
        }
        return (Applet)this.client;
    }

    private Object doLoad() {
        if (this.updateCheckMode == ClientUpdateCheckMode.NONE) {
            return null;
        }
        try {
            RSConfig config;
            SplashScreen.stage(0.0, null, "Fetching applet viewer config");
            int attempt = 0;
            while (true) {
                try {
                    config = this.clientConfigLoader.fetch();
                }
                catch (IOException e) {
                    log.info("Failed to get jav_config from host \"{}\" ({})", (Object)RuneLiteProperties.getJavConfig(), (Object)e.getMessage());
                    if (attempt >= 6) {
                        throw e;
                    }
                    ++attempt;
                    continue;
                }
                break;
            }
            SplashScreen.stage(0.465, "Starting", "Starting Zaros");
            Applet rs = null;
            if (this.updateCheckMode == ClientUpdateCheckMode.RUNELITE) {
                Class<?> clientClass = ClientLoader.class.getClassLoader().loadClass(config.getInitialClass());
                rs = ClientLoader.loadFromClass(config, clientClass);
            } else if (this.updateCheckMode == ClientUpdateCheckMode.VANILLA) {
                String codebase = config.getCodeBase();
                String initialJar = config.getInitialJar();
                String initialClass = config.getInitialClass();
                URL url = new URL(codebase + initialJar);
                URLClassLoader classloader = new URLClassLoader(new URL[]{url}, null);
                Class<?> clientClass = classloader.loadClass(initialClass);
                rs = ClientLoader.loadFromClass(config, clientClass);
            }
            SplashScreen.stage(0.5, null, "Starting core classes");
            return rs;
        }
        catch (IOException | ClassNotFoundException | IllegalAccessException | InstantiationException | SecurityException e) {
            if (e instanceof ClassNotFoundException) {
                log.error("Unable to load client - class not found. This means you are not running RuneLite with Maven as the client patch is not in your classpath.");
            }
            log.error("Error loading RS!", (Throwable)e);
            SwingUtilities.invokeLater(() -> FatalErrorDialog.showNetErrorWindow("loading the client", e));
            return null;
        }
    }

    private static Applet loadFromClass(RSConfig config, Class<?> clientClass) throws IllegalAccessException, InstantiationException {
        Applet rs = (Applet)clientClass.newInstance();
        rs.setStub(new RSAppletStub(config, null));
        return rs;
    }

    private static Certificate[] getJagexCertificateChain() throws CertificateException {
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        Collection<? extends Certificate> certificates = certificateFactory.generateCertificates(ClientLoader.class.getResourceAsStream("jagex.crt"));
        return certificates.toArray(new Certificate[certificates.size()]);
    }
}

