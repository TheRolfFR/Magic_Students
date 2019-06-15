package HUD;

import Renderers.FontRenderer;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

/**
 * Button graphic interface
 * @author TheRolf
 */
public class Button {

    private Image image;
    private int height;
    private int width;
    private Shape shape;
    private String text;

    private MouseListener listener;

    private static boolean showDebugRect = false;

    /**
     * Returns whether a debug rectangle will be rendered
     * @return whether a debug rectangle will be rendered
     */
    public static boolean showDebugRect() {
        return showDebugRect;
    }

    /**
     * Says whether a debug rectangle will be rendered
     * @param showDebugRect the state desired
     */
    public static void setShowDebugRect(boolean showDebugRect) {
        Button.showDebugRect = showDebugRect;
    }

    /**
     * Returns the height of the button
     * @return the height of the button
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns the width of the button
     * @return the width of the button
     */
    public int getWidth() {
        return width;
    }

    /**
     * Default constructor
     *
     * @param gc the GameContainer
     * @param text the text of the button
     * @param listener the listener associated
     */
    public Button(GameContainer gc, String text, MouseListener listener) {
        try {
            FontRenderer.getPixelFontRenderer().setPtSize(30.f);
            this.width = FontRenderer.getPixelFont().getWidth(text);
            this.height = FontRenderer.getPixelFont().getHeight(text);

            this.listener = listener;

            this.text = text;

            this.image = new Image(this.width, this.height);
            Graphics imageG = this.image.getGraphics();
            imageG.clear();
            imageG.setFont(FontRenderer.getPixelFont());
            imageG.setColor(Color.white);
            imageG.drawString(text, 0, 0);
            imageG.flush();

            this.shape = new Rectangle(-500, -500, this.width, this.height);
        } catch (SlickException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Moves the button
     * @param x the new x position of the button
     * @param y the new y position of the button
     */
    public void setLocation(int x, int y) {
        this.shape.setLocation(x, y);
    }

    /**
     * In game rendering
     * @param g the graphics to draw on
     */
    public void render(Graphics g) {
        g.drawImage(this.image, this.shape.getX(), this.shape.getY());

        if(showDebugRect()) {
            g.draw(shape);
        }
    }

    /**
     * Mouse click handler for the listener
     * @param button the index of the button (starting at 0)
     * @param x the x position of the mouse when the button was pressed
     * @param y the y position of the mouse when the button was pressed
     * @param clickCount the number of times the button was clicked
     */
    public void mouseClicked(int button, int x, int y, int clickCount) {
        if(shape.contains(x, y)) {
            listener.mouseClicked(button, x, y, clickCount);
        }
    }
}
