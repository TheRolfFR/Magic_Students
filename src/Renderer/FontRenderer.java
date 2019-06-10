package Renderer;

import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.util.ResourceLoader;

import java.awt.*;
import java.io.IOException;

public class FontRenderer {
    private static final String FONT_PREPATH = "font/";
    private static final String PIXEL_FONT_PATH = FONT_PREPATH + "ThaleahFat.ttf";
    private static final FontRenderer pixelFont = new FontRenderer(PIXEL_FONT_PATH);

    public static TrueTypeFont getPixelFont() {
        return pixelFont.getFont();
    }

    private TrueTypeFont font;

    public TrueTypeFont getFont() {
        return font;
    }

    public FontRenderer(String path) {
        try {
            Font UIFont1 = Font.createFont(Font.TRUETYPE_FONT, ResourceLoader.getResourceAsStream(path));
            UIFont1 = UIFont1.deriveFont(java.awt.Font.PLAIN, 30.f);
            this.font = new TrueTypeFont(UIFont1, false);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
