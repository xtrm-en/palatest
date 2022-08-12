package me.xtrm.paladium.palatest.client.ui.font.renderer;

import net.minecraft.client.renderer.Tessellator;

import java.awt.*;
import java.util.Locale;
import java.util.Random;

import static org.lwjgl.opengl.GL11.*;

public class SynapticFontRenderer implements IFontRenderer {
    private static final Random RANDOM = new Random();

    @Override
    public String getRendererName() {
        return "SynapticRenderer";
    }

    /**
     * Current X coordinate at which to draw the next character.
     */
    private double posX;
    /**
     * Current Y coordinate at which to draw the next character.
     */
    private double posY;
    /**
     * Array of RGB triplets defining the 16 standard chat colors followed by
     * 16 darker version of the same colors for drop shadows.
     */
    private final int[] colorCode = new int[32];
    /**
     * Used to specify new red value for the current color.
     */
    private float red;
    /**
     * Used to specify new blue value for the current color.
     */
    private float blue;
    /**
     * Used to specify new green value for the current color.
     */
    private float green;
    /**
     * Used to speify new alpha value for the current color.
     */
    private float alpha;

    /**
     * Set if the "k" style (random) is active in currently rendering string
     */
    private boolean randomStyle;
    /**
     * Set if the "l" style (bold) is active in currently rendering string
     */
    private boolean boldStyle;
    /**
     * Set if the "o" style (italic) is active in currently rendering string
     */
    private boolean italicStyle;
    /**
     * Set if the "n" style (underlined) is active in currently rendering string
     */
    private boolean underlineStyle;
    /**
     * Set if the "m" style (strikethrough) is active in currently rendering string
     */
    private boolean strikethroughStyle;

    private final GlyphAtlas regularGlyphAtlas;
    private final GlyphAtlas boldGlyphAtlas;
    private final GlyphAtlas italicGlyphAtlas;
    private final GlyphAtlas boldItalicGlyphAtlas;

    public SynapticFontRenderer(GlyphAtlas regularGlyphAtlas, GlyphAtlas boldGlyphAtlas, GlyphAtlas italicGlyphAtlas, GlyphAtlas boldItalicGlyphAtlas) {
        this.regularGlyphAtlas = regularGlyphAtlas;
        this.boldGlyphAtlas = boldGlyphAtlas;
        this.italicGlyphAtlas = italicGlyphAtlas;
        this.boldItalicGlyphAtlas = boldItalicGlyphAtlas;

        for (int i = 0; i < 32; ++i) {
            int j = (i >> 3 & 1) * 85;
            int k = (i >> 2 & 1) * 170 + j;
            int l = (i >> 1 & 1) * 170 + j;
            int i1 = (i & 1) * 170 + j;

            if (i == 6)
                k += 85;
            if (i >= 16) {
                k /= 4;
                l /= 4;
                i1 /= 4;
            }

            this.colorCode[i] = (k & 255) << 16 | (l & 255) << 8 | i1 & 255;
        }
    }

    public static SynapticFontRenderer create(String fontName, int size, boolean bold, boolean italic, boolean boldItalic) {
        char[] chars = new char[256];

        for (int i = 0; i < chars.length; i++) {
            chars[i] = (char) i;
        }

        GlyphAtlas regularAtlas;

        regularAtlas = new GlyphAtlas(new Font(fontName, Font.PLAIN, size), true, true);

        regularAtlas.generateGlyphAtlas(chars);
        regularAtlas.setupTexture();

        GlyphAtlas boldAtlas = regularAtlas;
        GlyphAtlas italicAtlas = regularAtlas;
        GlyphAtlas boldItalicAtlas = regularAtlas;

        if (bold) {
            boldAtlas = new GlyphAtlas(new Font(fontName, Font.BOLD, size), true, true);

            boldAtlas.generateGlyphAtlas(chars);
            boldAtlas.setupTexture();
        }

        if (italic) {
            italicAtlas = new GlyphAtlas(new Font(fontName, Font.ITALIC, size), true, true);

            italicAtlas.generateGlyphAtlas(chars);
            italicAtlas.setupTexture();
        }

        if (boldItalic) {
            boldItalicAtlas = new GlyphAtlas(new Font(fontName, Font.BOLD | Font.ITALIC, size), true, true);

            boldItalicAtlas.generateGlyphAtlas(chars);
            boldItalicAtlas.setupTexture();
        }

        return new SynapticFontRenderer(regularAtlas, boldAtlas, italicAtlas, boldItalicAtlas);
    }

    /**
     * Conveniance method
     */
    public void drawString(String text, double x, double y, int color){
        drawString(text, x, y, color, false);
    }

    /**
     * Conveniance method
     */
    public void drawStringWithShadow(String text, double x, double y, int color){
        drawString(text, x, y, color, true);
    }

    /**
     * Draws the specified string.
     */
    public void drawString(String text, double x, double y, int color, boolean dropShadow) {
        this.resetStyles();

        if (dropShadow) {
            this.renderString(text, x + 1, y + 1, color, true);
        }
        this.renderString(text, x, y, color, false);
    }

    /**
     * Render single line string by setting GL color, current (posX,posY), and calling renderStringAtPos()
     */
    private void renderString(String text, double x, double y, int color, boolean dropShadow) {
        if (text != null) {
            if ((color & -67108864) == 0) {
                color |= -16777216;
            }

            if (dropShadow) {
                color = (color & 16579836) >> 2 | color & -16777216;
            }

            this.red = (float) (color >> 16 & 255) / 255.0F;
            this.blue = (float) (color >> 8 & 255) / 255.0F;
            this.green = (float) (color & 255) / 255.0F;
            this.alpha = (float) (color >> 24 & 255) / 255.0F;
            glColor4f(this.red, this.blue, this.green, this.alpha);
            this.posX = x * 2;
            this.posY = y * 2;
            this.renderStringAtPos(text, dropShadow);
        }
    }

    /**
     * Render a single line string at the current (posX, posY) and update posX
     */
    private void renderStringAtPos(String text, boolean shadow) {
        GlyphAtlas glyphAtlas = getCurrentAtlas();

        glPushMatrix();

        glScaled(0.5, 0.5, 0.5);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_TEXTURE_2D);
        //GL11.glEnable(GL_ALPHA);

        glyphAtlas.bindTexture();

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        for (int i = 0; i < text.length(); ++i) {
            char c0 = text.charAt(i);

            if (c0 == 167 && i + 1 < text.length()) {
                int i1 = "0123456789abcdefklmnor".indexOf(text.toLowerCase(Locale.ENGLISH).charAt(i + 1));

                if (i1 < 16) {
                    this.randomStyle = false;
                    this.boldStyle = false;
                    this.strikethroughStyle = false;
                    this.underlineStyle = false;
                    this.italicStyle = false;

                    if (i1 < 0)
                        i1 = 15;
                    if (shadow)
                        i1 += 16;
                    int j1 = this.colorCode[i1];

                    glColor4f((float) (j1 >> 16) / 255.0F, (float) (j1 >> 8 & 255) / 255.0F, (float) (j1 & 255) / 255.0F, this.alpha);
                } else if (i1 == 16) {
                    this.randomStyle = true;
                } else if (i1 == 17) {
                    this.boldStyle = true;
                } else if (i1 == 18) {
                    this.strikethroughStyle = true;
                } else if (i1 == 19) {
                    this.underlineStyle = true;
                } else if (i1 == 20) {
                    this.italicStyle = true;
                } else {
                    this.randomStyle = false;
                    this.boldStyle = false;
                    this.strikethroughStyle = false;
                    this.underlineStyle = false;
                    this.italicStyle = false;

                    glColor4f(this.red, this.blue, this.green, this.alpha);
                }

                ++i;
            } else {
                glyphAtlas = getCurrentAtlas();
                glyphAtlas.bindTexture();

                float f = glyphAtlas.drawChar(c0, posX, posY);
                doDraw(f, glyphAtlas);
            }
        }

        glyphAtlas.unbindTexture();

        glPopMatrix();
    }

    private void doDraw(float f, GlyphAtlas glyphAtlas) {
        if (this.strikethroughStyle) {
            Tessellator tessellator = Tessellator.instance;
            glDisable(GL_TEXTURE_2D);
            tessellator.startDrawingQuads();
            tessellator.addVertex(this.posX, this.posY + (float) (glyphAtlas.getMaxFontHeight() / 2), 0.0D);
            tessellator.addVertex(this.posX + f, this.posY + (float) (glyphAtlas.getMaxFontHeight() / 2), 0.0D);
            tessellator.addVertex(this.posX + f, this.posY + (float) (glyphAtlas.getMaxFontHeight() / 2) - 1.0F, 0.0D);
            tessellator.addVertex(this.posX, this.posY + (float) (glyphAtlas.getMaxFontHeight() / 2) - 1.0F, 0.0D);
            tessellator.draw();
            glEnable(GL_TEXTURE_2D);
        }

        if (this.underlineStyle) {
            Tessellator tessellator1 = Tessellator.instance;
            glDisable(GL_TEXTURE_2D);
            tessellator1.startDrawingQuads();
            int l = this.underlineStyle ? -1 : 0;
            tessellator1.addVertex(this.posX + (float) l, this.posY + (float) glyphAtlas.getMaxFontHeight(), 0.0D);
            tessellator1.addVertex(this.posX + f, this.posY + (float) glyphAtlas.getMaxFontHeight(), 0.0D);
            tessellator1.addVertex(this.posX + f, this.posY + (float) glyphAtlas.getMaxFontHeight() - 1.0F, 0.0D);
            tessellator1.addVertex(this.posX + (float) l, this.posY + (float) glyphAtlas.getMaxFontHeight() - 1.0F, 0.0D);
            tessellator1.draw();
            glEnable(GL_TEXTURE_2D);
        }

        this.posX += f;
    }


    private GlyphAtlas getCurrentAtlas() {
        if (boldStyle && italicStyle)
            return boldItalicGlyphAtlas;
        else if (boldStyle)
            return boldGlyphAtlas;
        else if (italicStyle)
            return italicGlyphAtlas;
        else
            return regularGlyphAtlas;
    }

    /**
     * Reset all style flag fields in the class to false; called at the start of string rendering
     */
    private void resetStyles() {
        this.randomStyle = false;
        this.boldStyle = false;
        this.italicStyle = false;
        this.underlineStyle = false;
        this.strikethroughStyle = false;
    }

    public int getFontHeight() {
        return regularGlyphAtlas.getMaxFontHeight() / 2;
    }

    public int getStringWidth(String text) {
        if (text == null) {
            return 0;
        }
        int width = 0;

        int size = text.length();

        boolean isSearchingForColors = false;

        for (int i = 0; i < size; i++) {
            char character = text.charAt(i);
            GlyphAtlas atlas = getCurrentAtlas();

            if (character == '§'){
                isSearchingForColors = true;
                continue; // don't add this to the width
            }
            if(isSearchingForColors){ // the current char should be about colorcoding
                isSearchingForColors = false;
                int colorIndex = "0123456789abcdefklmnor".indexOf(character);
                if(colorIndex == -1){ // this is no color char, what the fuck (btw its back at white again, pay attention to that fontrenderer)
                    continue;
                }
                if (colorIndex < 16) {
                    boldStyle = false;
                    italicStyle = false;
                } else if (colorIndex == 17) {
                    boldStyle = true;
                } else if (colorIndex == 20) {
                    italicStyle = true;
                } else if (colorIndex == 21) {
                    boldStyle = false;
                    italicStyle = false;
                }
                continue;
            }

            width += atlas.getWidth(character) - 8;
        }

        return width / 2;
    }

    /**
     * Trims a string to fit a specified Width.
     *
     * @param text  The text to trim
     * @param width The desired width
     *
     * @return the trimmed text
     */
    public String trimStringToWidth(String text, int width) {
        return this.trimStringToWidth(text, width, false);
    }

    /**
     * Trims a string to a specified width,
     * and will reverse it if {@code reverse} is set.
     *
     * @param text      The text to trim
     * @param maxWidth  The desired width
     * @param reverse   Whether to reverse the string after trimming.
     *
     * @return the trimmed text
     */
    public String trimStringToWidth(String text, int maxWidth, boolean reverse) {
        StringBuilder stringbuilder = new StringBuilder();

        boolean on = false;

        int j = reverse ? text.length() - 1 : 0;
        int k = reverse ? -1 : 1;
        int width = 0;

        GlyphAtlas atlas;

        for (int i = j; i >= 0 && i < text.length() && i < maxWidth; i += k) {
            char character = text.charAt(i);

            if (character == '§')
                on = true;
            else if (on && character >= '0' && character <= 'r') {
                int colorIndex = "0123456789abcdefklmnor".indexOf(character);
                if (colorIndex < 16) {
                    boldStyle = false;
                    italicStyle = false;
                } else if (colorIndex == 17) {
                    boldStyle = true;
                } else if (colorIndex == 20) {
                    italicStyle = true;
                } else if (colorIndex == 21) {
                    boldStyle = false;
                    italicStyle = false;
                }
                i++;
                on = false;
            } else {
                if (on) i--;

                character = text.charAt(i);

                atlas = getCurrentAtlas();

                width += (atlas.getWidth(character) - 8) / 2;
            }

            if (i > width) {
                break;
            }

            if (reverse) {
                stringbuilder.insert(0, character);
            } else {
                stringbuilder.append(character);
            }
        }

        return stringbuilder.toString();
    }
}
