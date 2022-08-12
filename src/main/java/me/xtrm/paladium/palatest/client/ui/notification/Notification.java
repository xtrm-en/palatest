package me.xtrm.paladium.palatest.client.ui.notification;

import lombok.Data;
import me.xtrm.paladium.palatest.client.ui.animation.AnimationTrack;
import me.xtrm.paladium.palatest.client.ui.animation.Animations;
import me.xtrm.paladium.palatest.common.ui.notification.NotificationType;
import me.xtrm.paladium.palatest.client.ui.font.FontManager;
import me.xtrm.paladium.palatest.client.ui.font.renderer.IFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.opengl.GL11;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

public @Data class Notification {

    private static final Minecraft mc = Minecraft.getMinecraft();

    private static final IFontRenderer titleFont = FontManager.INSTANCE.getFontRenderer("product-sans", 21);
    private static final IFontRenderer font = FontManager.INSTANCE.getFontRenderer("product-sans");
    static final int MARGIN_X = 10;
    static final int MARGIN_Y = 15;

    private final NotificationType type;
    private final String title;
    private final String content;

    private final AnimationTrack slideInAnimation =
        new AnimationTrack(Animations.OUT_QUINT, 20, 0);
    private final AnimationTrack slideOutAnimation;

    public Notification(NotificationType notificationType, String title, String content, int delay) {
        this.type = notificationType;
        this.title = title;
        this.content = content;

        this.slideOutAnimation = new AnimationTrack(Animations.OUT_QUINT, 25, delay * 20);
    }

    public void render(int yOffset) {
        slideInAnimation.update();
        slideOutAnimation.update();

        ScaledResolution resolution =
            new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);

        int w = getWidth();
        int h = getHeight();

        double animPercentage = (slideInAnimation.getValue(false) - slideOutAnimation.getValue(false));

        int xPosition = (int) ((resolution.getScaledWidth() + 5) - ((w + MARGIN_X + 5) * animPercentage));
        int yPosition = resolution.getScaledHeight() - (MARGIN_Y + h + yOffset);

        int color = ((int) (0xCC * animPercentage)) << 24;
        Gui.drawRect(xPosition, yPosition, xPosition + w, yPosition + h, color);

        int iconSize = (int) (h * .7);
        int offset = (h - iconSize) / 2;
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glColor4d(1, 1, 1, animPercentage);
        mc.getTextureManager().bindTexture(this.type.getIconLocation());

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        Gui.drawScaledCustomSizeModalRect(xPosition + offset, yPosition + offset, 0, 0, 128, 128, iconSize, iconSize, 128, 128);
        GL11.glPopMatrix();

        int fontColor = ((int) Math.max(0x55, 0xFF * animPercentage) << 24) + 0xFFFFFF;

        titleFont.drawStringWithShadow(EnumChatFormatting.BOLD + title, xPosition + iconSize + (offset * 2), yPosition + 3, fontColor);
        List<String> content = getContent();
        AtomicInteger ai = new AtomicInteger(0);
        content.forEach(l -> font.drawStringWithShadow(l, xPosition + iconSize + (offset * 2), yPosition + titleFont.getFontHeight() + (font.getFontHeight() * ai.getAndIncrement()) + 3, fontColor));

        float timerPercentage = (float) this.slideOutAnimation.getDelayFrame() / (float) this.slideOutAnimation.getTotalDelayFrames();
        int timerWidth = (int) (w * timerPercentage);
        int mask = color + 0xFFFFFF;

        Gui.drawRect(xPosition, yPosition + h - 1, xPosition + timerWidth, yPosition + h, this.type.getColor().getRGB() & mask);
    }

    private int getWidth() {
        List<String> content = getContent();

        int width = titleFont.getStringWidth(title);
        for (String line : content)
            if (font.getStringWidth(line) > width)
                width = font.getStringWidth(line);
        return 30 + (11 * content.size()) + width;
    }

    int getHeight() {
        return 21 + (getContent().size() * font.getFontHeight());
    }

    private List<String> getContent() {
        return Arrays.asList(content.split(Pattern.quote("\n")));
    }

    public boolean isFinished() {
        return slideOutAnimation.isFinished();
    }
}
