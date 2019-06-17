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
    private static final String FONT_PREPATH = "font/"; // font folder location
    private static final String PIXEL_FONT_PATH = FONT_PREPATH + "FORCED SQUARE.ttf"; // pixel font location
    private static final FontRenderer pixelFont = new FontRenderer(PIXEL_FONT_PATH, 20.f); // static pixel font renderer

    /**
     * Allows to have a pixel font
     * @return the pixel TrueTypeFont font of our game
     */
    public static FontRenderer getPixelFontRenderer() { return pixelFont; }

    /**
     * Slick font object getter
     * @return the pixel font
     */
    public static TrueTypeFont getPixelFont() {
        return pixelFont.getFont();
    }

    private Font originalAwtFont; // orginal axt font that can be derived to get Slick fonts
    private TrueTypeFont font; // the latest font set

    /*
     * This is an optimization because a derivation of an AWT font is very power consuming.
     * I must keep all already loaded fonts in order to get them easily
     */
    private HashMap<Float, TrueTypeFont> fontSizesList;

    /**
     * Font getter
     * @return the font
     */
    public TrueTypeFont getFont() {
        return font;
    }

    /**
     * Default constructor with a default 12pt size
     * @param path the file path to the ttf file
     */
    public FontRenderer(String path) {
        init(path, 12.f); // init the object with a 12pt font
    }

    /**
     * Constructor with default pt size setter
     * @param path the file path to the ttf file
     * @param ptSize the point size of the font
     */
    public FontRenderer(String path, float ptSize) {
        init(path, ptSize); // init the object with a <ptSize>pt font
    }

    /**
     * Private contructor method in order not to rewrite each constructor
     * @param path the path to the font
     * @param ptSize the pt size of the desired Slick font
     */
    private void init(String path, float ptSize) {
        try {
            this.fontSizesList = new HashMap<>(); // create the hash map

            this.originalAwtFont = Font.createFont(Font.TRUETYPE_FONT, ResourceLoader.getResourceAsStream(path)); // try to load th original awt font
            this.setPtSize(ptSize); // sets the size of the font
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Point size setter for the Slick font that can be get with the {@link #getPixelFont()} method
     * @param ptSize the point size desired
     */
    public void setPtSize(float ptSize) {
        if (!this.fontSizesList.containsKey(ptSize)) { // if the size doesn't exists yet
            Font awtFont = this.originalAwtFont.deriveFont(Font.PLAIN, ptSize); // derivate ot
            TrueTypeFont ttfFont = new TrueTypeFont(awtFont, false); // create a new Slick2D font from it

            this.fontSizesList.put(ptSize, ttfFont); // add it to your list

            this.font = ttfFont; // update the last set font
        } else {
            this.font = this.fontSizesList.get(ptSize); // else go get him
        }
    }

    /**
     * Pixel size  setter for the Slick font that can be get with the {@link #getPixelFont()} method
     * @param pxSize the pixel size desired
     */
    public void setPxSize(int pxSize) {
        this.setPtSize(50.f); //  set the pt size to a known value

        int textHeight = this.font.getHeight("ABCDEFGHIJKLMNOPQRSTUVWQYZ"); // calulate the height of such a text

        float scale = ((float) pxSize) / ((float) textHeight); // deduce the scale with your desired px size

        this.setPtSize(50.f*scale); // request for a scaled pt size
    }
}
