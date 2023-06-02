/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Strings
 *  com.google.gson.Gson
 *  javax.annotation.Nullable
 *  javax.inject.Inject
 *  javax.inject.Named
 *  javax.inject.Singleton
 *  net.runelite.api.Client
 *  net.runelite.api.GameState
 *  net.runelite.http.api.RuneLiteAPI
 *  okhttp3.Call
 *  okhttp3.Callback
 *  okhttp3.HttpUrl
 *  okhttp3.MediaType
 *  okhttp3.MultipartBody
 *  okhttp3.MultipartBody$Builder
 *  okhttp3.OkHttpClient
 *  okhttp3.Request
 *  okhttp3.Request$Builder
 *  okhttp3.RequestBody
 *  okhttp3.Response
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package net.runelite.client.util;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.client.Notifier;
import net.runelite.client.RuneLite;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.config.RuneScapeProfileType;
import net.runelite.client.util.ImageUploadStyle;
import net.runelite.client.util.Text;
import net.runelite.client.util.TransferableBufferedImage;
import net.runelite.client.util.WebhookBody;
import net.runelite.http.api.RuneLiteAPI;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class ImageCapture {
    private static final Logger log = LoggerFactory.getLogger(ImageCapture.class);
    private static final DateFormat TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
    private static final HttpUrl IMGUR_IMAGE_UPLOAD_URL = HttpUrl.get((String)"https://api.imgur.com/3/image");
    private static final MediaType JSON = MediaType.get((String)"application/json");
    private final Client client;
    private final Notifier notifier;
    private final OkHttpClient okHttpClient;
    private final Gson gson;
    private final String imgurClientId;
    @Inject
    private ConfigManager configManager;

    @Inject
    private ImageCapture(Client client, Notifier notifier, OkHttpClient okHttpClient, Gson gson, @Named(value="runelite.imgur.client.id") String imgurClientId) {
        this.client = client;
        this.notifier = notifier;
        this.okHttpClient = okHttpClient;
        this.gson = gson;
        this.imgurClientId = imgurClientId;
    }

    public void takeScreenshot(BufferedImage screenshot, String fileName, @Nullable String subDir, boolean notify, ImageUploadStyle imageUploadStyle) {
        File playerFolder;
        if (this.client.getGameState() == GameState.LOGIN_SCREEN) {
            log.info("Login screenshot prevented");
            return;
        }
        if (this.client.getLocalPlayer() != null && this.client.getLocalPlayer().getName() != null) {
            String playerDir = this.client.getLocalPlayer().getName();
            RuneScapeProfileType profileType = RuneScapeProfileType.getCurrent(this.client);
            if (profileType != RuneScapeProfileType.STANDARD) {
                playerDir = playerDir + "-" + Text.titleCase(profileType);
            }
            if (!Strings.isNullOrEmpty((String)subDir)) {
                playerDir = playerDir + File.separator + subDir;
            }
            playerFolder = new File(RuneLite.SCREENSHOT_DIR, playerDir);
        } else {
            playerFolder = RuneLite.SCREENSHOT_DIR;
        }
        playerFolder.mkdirs();
        fileName = fileName + (fileName.isEmpty() ? "" : " ") + ImageCapture.format(new Date());
        try {
            File screenshotFile = new File(playerFolder, fileName + ".png");
            int i = 1;
            while (screenshotFile.exists()) {
                screenshotFile = new File(playerFolder, fileName + String.format("(%d)", i++) + ".png");
            }
            ImageIO.write((RenderedImage)screenshot, "PNG", screenshotFile);
            String webhook = this.configManager.getConfiguration("screenshot", "webhook");
            if (webhook != null && !webhook.isEmpty()) {
                this.uploadScreenshot(screenshotFile, screenshot, notify, true);
            }
            if (imageUploadStyle == ImageUploadStyle.IMGUR) {
                this.uploadScreenshot(screenshotFile, screenshot, notify, false);
            } else if (imageUploadStyle == ImageUploadStyle.CLIPBOARD) {
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                TransferableBufferedImage transferableBufferedImage = new TransferableBufferedImage(screenshot);
                clipboard.setContents(transferableBufferedImage, null);
                if (notify) {
                    this.notifier.notify("A screenshot was saved and inserted into your clipboard!", TrayIcon.MessageType.INFO);
                }
            } else if (notify) {
                this.notifier.notify("A screenshot was saved to " + screenshotFile, TrayIcon.MessageType.INFO);
            }
        }
        catch (IOException ex) {
            log.warn("error writing screenshot", (Throwable)ex);
        }
    }

    public void takeScreenshot(BufferedImage screenshot, String fileName, boolean notify, ImageUploadStyle imageUploadStyle) {
        this.takeScreenshot(screenshot, fileName, null, notify, imageUploadStyle);
    }

    private void uploadScreenshot(File screenshotFile, BufferedImage screenshot, final boolean notify, boolean discord) throws IOException {
        Request request;
        if (discord) {
            byte[] imageBytes;
            try {
                imageBytes = ImageCapture.convertImageToByteArray(screenshot);
            }
            catch (IOException e) {
                log.warn("Error converting image to byte array", (Throwable)e);
                return;
            }
            WebhookBody webhookBody = new WebhookBody();
            webhookBody.setContent(screenshotFile.getName());
            String json = RuneLiteAPI.GSON.toJson((Object)webhookBody);
            request = new Request.Builder().url(this.configManager.getConfiguration("screenshot", "webhook")).addHeader("Authorization", "Client-ID " + this.imgurClientId).post((RequestBody)new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("payload_json", json).addFormDataPart("file", screenshotFile.getName(), RequestBody.create((MediaType)MediaType.parse((String)"image/png"), (byte[])imageBytes)).build()).build();
        } else {
            String json = this.gson.toJson((Object)new ImageUploadRequest(screenshotFile));
            request = new Request.Builder().url(IMGUR_IMAGE_UPLOAD_URL).addHeader("Authorization", "Client-ID " + this.imgurClientId).post(RequestBody.create((MediaType)JSON, (String)json)).build();
        }
        this.okHttpClient.newCall(request).enqueue(new Callback(){

            public void onFailure(Call call, IOException ex) {
                log.warn("error uploading screenshot", (Throwable)ex);
            }

            public void onResponse(Call call, Response response) throws IOException {
                try (InputStream in = response.body().byteStream();){
                    ImageUploadResponse imageUploadResponse = (ImageUploadResponse)ImageCapture.this.gson.fromJson((Reader)new InputStreamReader(in, StandardCharsets.UTF_8), ImageUploadResponse.class);
                    if (imageUploadResponse.isSuccess()) {
                        String link = imageUploadResponse.getData().getLink();
                        StringSelection selection = new StringSelection(link);
                        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                        clipboard.setContents(selection, selection);
                        if (notify) {
                            ImageCapture.this.notifier.notify("A screenshot was uploaded and inserted into your clipboard!", TrayIcon.MessageType.INFO);
                        }
                    }
                }
            }
        });
    }

    private static byte[] convertImageToByteArray(BufferedImage bufferedImage) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write((RenderedImage)bufferedImage, "png", byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static String format(Date date) {
        DateFormat dateFormat = TIME_FORMAT;
        synchronized (dateFormat) {
            return TIME_FORMAT.format(date);
        }
    }

    private static class ImageUploadRequest {
        private final String image;
        private final String type;

        ImageUploadRequest(File imageFile) throws IOException {
            this.image = Base64.getEncoder().encodeToString(Files.readAllBytes(imageFile.toPath()));
            this.type = "base64";
        }

        public String getImage() {
            return this.image;
        }

        public String getType() {
            return this.type;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof ImageUploadRequest)) {
                return false;
            }
            ImageUploadRequest other = (ImageUploadRequest)o;
            if (!other.canEqual(this)) {
                return false;
            }
            String this$image = this.getImage();
            String other$image = other.getImage();
            if (this$image == null ? other$image != null : !this$image.equals(other$image)) {
                return false;
            }
            String this$type = this.getType();
            String other$type = other.getType();
            return !(this$type == null ? other$type != null : !this$type.equals(other$type));
        }

        protected boolean canEqual(Object other) {
            return other instanceof ImageUploadRequest;
        }

        public int hashCode() {
            int PRIME = 59;
            int result = 1;
            String $image = this.getImage();
            result = result * 59 + ($image == null ? 43 : $image.hashCode());
            String $type = this.getType();
            result = result * 59 + ($type == null ? 43 : $type.hashCode());
            return result;
        }

        public String toString() {
            return "ImageCapture.ImageUploadRequest(image=" + this.getImage() + ", type=" + this.getType() + ")";
        }
    }

    private static class ImageUploadResponse {
        private Data data;
        private boolean success;

        public Data getData() {
            return this.data;
        }

        public boolean isSuccess() {
            return this.success;
        }

        public void setData(Data data) {
            this.data = data;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof ImageUploadResponse)) {
                return false;
            }
            ImageUploadResponse other = (ImageUploadResponse)o;
            if (!other.canEqual(this)) {
                return false;
            }
            if (this.isSuccess() != other.isSuccess()) {
                return false;
            }
            Data this$data = this.getData();
            Data other$data = other.getData();
            return !(this$data == null ? other$data != null : !((Object)this$data).equals(other$data));
        }

        protected boolean canEqual(Object other) {
            return other instanceof ImageUploadResponse;
        }

        public int hashCode() {
            int PRIME = 59;
            int result = 1;
            result = result * 59 + (this.isSuccess() ? 79 : 97);
            Data $data = this.getData();
            result = result * 59 + ($data == null ? 43 : ((Object)$data).hashCode());
            return result;
        }

        public String toString() {
            return "ImageCapture.ImageUploadResponse(data=" + this.getData() + ", success=" + this.isSuccess() + ")";
        }

        private static class Data {
            private String link;

            public String getLink() {
                return this.link;
            }

            public void setLink(String link) {
                this.link = link;
            }

            public boolean equals(Object o) {
                if (o == this) {
                    return true;
                }
                if (!(o instanceof Data)) {
                    return false;
                }
                Data other = (Data)o;
                if (!other.canEqual(this)) {
                    return false;
                }
                String this$link = this.getLink();
                String other$link = other.getLink();
                return !(this$link == null ? other$link != null : !this$link.equals(other$link));
            }

            protected boolean canEqual(Object other) {
                return other instanceof Data;
            }

            public int hashCode() {
                int PRIME = 59;
                int result = 1;
                String $link = this.getLink();
                result = result * 59 + ($link == null ? 43 : $link.hashCode());
                return result;
            }

            public String toString() {
                return "ImageCapture.ImageUploadResponse.Data(link=" + this.getLink() + ")";
            }
        }
    }
}

