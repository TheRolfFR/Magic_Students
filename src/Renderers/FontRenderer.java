package Renderers;

import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.util.ResourceLoader;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;

/**
 * Simple TrueTypeFont renderer
 * @author TheRolf
 */
public class FontRenderer {
    private static final String FONT_PREPATH = "font/";
    private static final String PIXEL_FONT_PATH = FONT_PREPATH + "Pixel Square Bold10.ttf";
    private static final FontRenderer pixelFont = new FontRenderer(PIXEL_FONT_PATH, 20.f);

    /**
     * Allows to have a pixel font
     * @return the pixel TrueTypeFont font of our game
     */
    public static FontRenderer getPixelFontRenderer() { return pixelFont; }
    public static TrueTypeFont getPixelFont() {
        return pixelFont.getFont();
    }

    private Font originalAwtFont;
    private TrueTypeFont font;

    private HashMap<Float, TrueTypeFont> fontSizesList;

    /**
     * Font getter
     * @return the TrueTypeFont font
     */
    public TrueTypeFont getFont() {
        return font;
    }

    /**
     * Default constructor
     * @param path the file path to the ttf file
     */
    public FontRenderer(String path) {
        init(path,12.f);
    }

    /**
     * Complex constructor
     * @param path the file path to the ttf file
     * @param size the point size of the font
     */
    public FontRenderer(String path, float size) {
        init(path, size);
    }

    private void init(String path, float size) {
        try {
            this.fontSizesList = new HashMap<>();

            this.originalAwtFont = Font.createFont(Font.TRUETYPE_FONT, ResourceLoader.getResourceAsStream(path));
            this.setPtSize(size);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void setPtSize(float ptSize) {
        if(!this.fontSizesList.containsKey(ptSize)) {
            Font awtFont = this.originalAwtFont.deriveFont(Font.PLAIN, ptSize);
            TrueTypeFont ttfFont = new TrueTypeFont(awtFont, false);

            this.fontSizesList.put(ptSize, ttfFont);

            this.font = ttfFont;
        } else {
            this.font = this.fontSizesList.get(ptSize);
        }
    }

    public void setPxSize(int pxSize) {
        this.setPtSize(50.f);

        int textHeight = this.font.getHeight("ABCDEFGHIJKLMNOPQRSTUVWQYZ");

        float scale = ((float) pxSize) / ((float) textHeight);

        this.setPtSize(50.f*scale);
    }
}
