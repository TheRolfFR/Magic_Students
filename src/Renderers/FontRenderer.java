package Renderers;

import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.util.ResourceLoader;

import java.awt.*;
import java.io.IOException;

/**
 * Simple TrueTypeFont renderer
 * @author TheRolf
 */
public class FontRenderer {
    private static final String FONT_PREPATH = "font/";
    private static final String PIXEL_FONT_PATH = FONT_PREPATH + "ThaleahFat.ttf";
    private static final FontRenderer pixelFont = new FontRenderer(PIXEL_FONT_PATH, 30.f);

    /**
     * Allows to have a pixel font
     * @return the pixel TrueTypeFont font of our game
     */
    public static TrueTypeFont getPixelFont() {
        return pixelFont.getFont();
    }

    private Font orginalAwtFont;
    private TrueTypeFont font;

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

    public FontRenderer(String path, float size) {
        init(path, size);
    }

    private void init(String path, float size) {
        try {
            this.orginalAwtFont = Font.createFont(Font.TRUETYPE_FONT, ResourceLoader.getResourceAsStream(path));
            this.setPtSize(size);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void setPtSize(float ptSize) {
        Font f = this.orginalAwtFont.deriveFont(Font.PLAIN, ptSize);
        this.font = new TrueTypeFont(f, false);
    }
}
