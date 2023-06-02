/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap$Builder
 */
package net.runelite.client.plugins.emojis;

import com.google.common.collect.ImmutableMap;
import java.awt.image.BufferedImage;
import java.util.Map;
import net.runelite.client.util.ImageUtil;

enum Emoji {
    SLIGHT_SMILE(":)"),
    JOY("=')"),
    COWBOY("3:)"),
    BLUSH("^_^"),
    SMILE(":D"),
    GRINNING("=D"),
    WINK(";)"),
    STUCK_OUT_TONGUE_CLOSED_EYES("X-P"),
    STUCK_OUT_TONGUE(":P"),
    YUM("=P~"),
    HUGGING("<gt>:D<lt>"),
    TRIUMPH(":<gt>"),
    THINKING(":-?"),
    CONFUSED(":/"),
    NEUTRAL_FACE("=|"),
    EXPRESSIONLESS(":|"),
    UNAMUSED(":-|"),
    SLIGHT_FROWN(":("),
    FROWNING2("=("),
    CRY(":'("),
    SOB(":_("),
    FLUSHED(":$"),
    ZIPPER_MOUTH(":-#"),
    PERSEVERE("<gt>_<lt>"),
    SUNGLASSES("8-)"),
    INNOCENT("O:)"),
    SMILING_IMP("<gt>:)"),
    RAGE("<gt>:("),
    HUSHED(":-O"),
    OPEN_MOUTH(":O"),
    SCREAM(":-@"),
    SEE_NO_EVIL("X_X"),
    DANCER("\\:D/"),
    OK_HAND("(Ok)"),
    THUMBSUP("(Y)"),
    THUMBSDOWN("(N)"),
    HEARTS("<lt>3"),
    BROKEN_HEART("<lt>/3"),
    ZZZ("Zzz"),
    FISH("<lt><gt><lt>"),
    CAT(":3"),
    DOG("=3"),
    CRAB("V(;,;)V"),
    FORK_AND_KNIFE("--E"),
    COOKING("--(o)"),
    PARTY_POPPER("@@@"),
    EYES("O.O"),
    SWEAT(";;"),
    PILE_OF_POO("~@~"),
    FIRE("(/\\)"),
    ALIEN("(@.@)"),
    EGGPLANT("8=D"),
    WAVE("(^_^)/"),
    HEART_EYES("(*.*)"),
    FACEPALM("M-)"),
    PENSIVE("V_V"),
    ACORN("<lt>D~"),
    GORILLA(":G"),
    PLEADING("(n_n)"),
    XD("Xd"),
    SPOON("--o"),
    WEARY_FACE("Dx"),
    ROCKETSHIP("<gt>==<gt>"),
    CLOWN(":O)"),
    COW("3:O"),
    NOTDUCK(":notduck"),
    FEELSOK(":feelsok"),
    POG(":pog"),
    LUL(":lul"),
    MONKAW(":monka");

    private static final Map<String, Emoji> emojiMap;
    private final String trigger;

    private Emoji(String trigger) {
        this.trigger = trigger;
    }

    BufferedImage loadImage() {
        return ImageUtil.loadImageResource(((Object)((Object)this)).getClass(), this.name().toLowerCase() + ".png");
    }

    static Emoji getEmoji(String trigger) {
        return emojiMap.get(trigger);
    }

    static {
        ImmutableMap.Builder builder = new ImmutableMap.Builder();
        for (Emoji emoji : Emoji.values()) {
            builder.put((Object)emoji.trigger, (Object)emoji);
        }
        emojiMap = builder.build();
    }
}

