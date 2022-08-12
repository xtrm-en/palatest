package me.xtrm.paladium.palatest.client.ui.font;

import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import me.xtrm.paladium.palatest.Constants;
import me.xtrm.paladium.palatest.client.ui.font.renderer.GlyphAtlas;
import me.xtrm.paladium.palatest.client.ui.font.renderer.IFontRenderer;
import me.xtrm.paladium.palatest.client.ui.font.renderer.SynapticFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import java.awt.*;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.*;

/**
 * Shared loader and manager for Font/FontRenderers.
 *
 * @author xtrm
 * @since 4.0.692
 */
@Log4j2(topic = "FontManager")
public enum FontManager {
    INSTANCE;

    private final List<FontData> loadedFonts = new ArrayList<>();
    private final Map<FontData, IFontRenderer> fontRenderers = new HashMap<>();
    private IFontRenderer minecraftRenderer;

    public void initialize() {
        this.loadedFonts.addAll(
            Arrays.asList(
                new Gson().fromJson(
                    new InputStreamReader(
                        Objects.requireNonNull(
                            getClass().getResourceAsStream(
                                "/assets/"
                                    + Constants.MODID
                                    + "/fonts/font_list.json"
                            )
                        )
                    ), FontData[].class
                )
            )
        );

        this.loadedFonts.forEach(f -> {
            IFontRenderer fontRenderer = generateFontRenderer(f);
            this.fontRenderers.put(f, fontRenderer);
        });
    }

    private IFontRenderer generateFontRenderer(FontData font) {
        log.info("Generating FontRenderer... (" + font.getFontName() + " @ " + font.getBaseSize() + "px)");

        char[] charset = new char[256];
        for (int i = 0; i < charset.length; i++) {
            charset[i] = (char) i;
        }

        InputStream is = getClass().getResourceAsStream(
            "/assets/"
                + Constants.MODID
                + "/fonts/"
                + font.getFontName().toLowerCase()
                + "/data.ttf"
        );
        assert is != null;

        Font baseFont;
        try {
            baseFont = Font.createFont(0, is);
        } catch (Throwable t) {
            throw new RuntimeException("Couldn't load font " + font.getFontName() + "!");
        }

        Font normal = baseFont.deriveFont(Font.PLAIN, font.getBaseSize());
        Font bold = baseFont.deriveFont(Font.BOLD, font.getBaseSize());
        Font italic = baseFont.deriveFont(Font.ITALIC, font.getBaseSize());
        Font boldItalic = baseFont.deriveFont(Font.BOLD | Font.ITALIC, font.getBaseSize());

        GlyphAtlas page = new GlyphAtlas(normal, true, true);
        page.generateGlyphAtlas(charset);
        page.setupTexture();
        GlyphAtlas page2 = new GlyphAtlas(bold, true, true);
        page2.generateGlyphAtlas(charset);
        page2.setupTexture();
        GlyphAtlas page3 = new GlyphAtlas(italic, true, true);
        page3.generateGlyphAtlas(charset);
        page3.setupTexture();
        GlyphAtlas page4 = new GlyphAtlas(boldItalic, true, true);
        page4.generateGlyphAtlas(charset);
        page4.setupTexture();

        return new SynapticFontRenderer(page, page2, page3, page4);
    }

    public IFontRenderer getFontRenderer(String name) {
        if (name == null || name.isEmpty()) return null;

        if (loadedFonts.isEmpty() || name.equalsIgnoreCase("minecraft")) {
            if (this.minecraftRenderer == null) {
                this.minecraftRenderer = generateMinecraftRenderer();
            }
            if (!name.equalsIgnoreCase("minecraft")) {
                log.warn("Couldn't generate font \"{}\", fallback to default...", name);
            }
            return minecraftRenderer;
        }

        String fontName = name;
        int sizeModifier = -1;

        int index = name.indexOf('/');
        if (index != -1) {
            fontName = name.substring(0, index);
            try {
                sizeModifier = Integer.parseInt(name.substring(index + 1));
            } catch (Throwable ignored) {
                log.warn(
                    "Error while parsing size modifier in string declaration: \"{}\"",
                    name
                );
            }
        }

        return getFontRenderer(fontName, sizeModifier);
    }

    public IFontRenderer getFontRenderer(String fontName, int size) {
        FontData data = new FontData(fontName, size);
        if (size == -1) {
            data = this.loadedFonts
                .stream()
                .filter(d -> d.getFontName().equalsIgnoreCase(fontName))
                .findFirst()
                .orElse(data);
        }
        return getFontRenderer(data);
    }

    private IFontRenderer getFontRenderer(FontData fontData) {
        return this.fontRenderers.computeIfAbsent(fontData, this::generateFontRenderer);
    }

    private IFontRenderer generateMinecraftRenderer() {
        return new IFontRenderer() {
            private final FontRenderer fr =
                Minecraft.getMinecraft().fontRendererObj;

            @Override
            public int getFontHeight() {
                return fr.FONT_HEIGHT;
            }

            @Override
            public int getStringWidth(String text) {
                return fr.getStringWidth(text);
            }

            @Override
            public void drawString(String text, double x, double y, int color) {
                fr.drawString(text, (int) x, (int) y, color);
            }

            @Override
            public void drawStringWithShadow(String text, double x, double y, int color) {
                fr.drawStringWithShadow(text, (int) x, (int) y, color);
            }
        };
    }
}
