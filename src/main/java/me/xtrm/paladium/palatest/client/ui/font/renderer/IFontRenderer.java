package me.xtrm.paladium.palatest.client.ui.font.renderer;

public interface IFontRenderer {
    default String getRendererName() {
        return "Vanilla";
    }

    int getFontHeight();

    int getStringWidth(String text);

    default void drawString(String text, double x, double y, int color, boolean shadow) {
        if (shadow) {
            drawStringWithShadow(text, x, y, color);
        } else {
            drawString(text, x, y, color);
        }
    }

    void drawString(String text, double x, double y, int color);

    void drawStringWithShadow(String text, double x, double y, int color);

}
