package Renderers;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

import java.util.Random;

/**
 * Game grass background renderer
 */
public class BackgroundRenderer {

    private static final String BACKGROUND_PATH = "img/ground.png"; // ground image location
    private static final float BACKGROUND_SCALE = 2f; // backgorund tile scale

    private static final Color GROUND_COLOR = new Color(0x4CAF50); // green ground color

    private static SpriteSheet backgrounds = null; // the ground spritesheet
    private static Image backgroundImage = null; // the background image to display

    private static Vector2f roomDimension = null; // the size of the room (the window size)

    private BackgroundRenderer() {
    }

    /**
     * Charges a new empty image and the ground spritesheet with a certain scale
     * @param gc the game container
     */
    public static void generateBackground(GameContainer gc) {
        if (backgroundImage == null) {
            try {
                roomDimension = new Vector2f(gc.getWidth(), gc.getHeight()); // getting window dimensions

                Image image = new Image(BACKGROUND_PATH, false, Image.FILTER_NEAREST); // try to the ground image with a nearest filter (important)

                int scaledHeight = (int) (image.getHeight() * BACKGROUND_SCALE); // determinating the size of a scaled tile

                image = image.getScaledCopy(BACKGROUND_SCALE); // scaling the image (no alisaing because nearest filter)

                backgrounds = new SpriteSheet(image, scaledHeight, scaledHeight); // creating the spritesheet

                backgroundImage = new Image((int) roomDimension.getX(), (int) roomDimension.getY()); // create an empty image woth the room size

                regenerateBackground(); // generating the background
            } catch (SlickException e) { // else crash
                e.printStackTrace();
                System.exit(1);
            }
        }
    }

    /**
     * Fill the background image with random tiles from the ground spritesheet
     */
    public static void regenerateBackground() {

        try {
            Random randomReference = new Random(); // creation of a random object
            int spriteX; //

            Graphics backgroundImageG = backgroundImage.getGraphics(); // getting the graphics of the image in order to draw on
            backgroundImageG.clear(); // clearing the graphics context

            for (int a = 0; a < roomDimension.getX()/backgrounds.getSprite(0, 0).getHeight()+1; a++) { // looping through the image height
                for (int b = 0; b < roomDimension.getY()/backgrounds.getSprite(0, 0).getHeight()+1; b++) { // looping through the image width
                    if (randomReference.nextInt(4) < 3) { // 3/4 probablity
                        spriteX = 0; // select the first tile index
                    } else {
                        spriteX = 1+ randomReference.nextInt(backgrounds.getHorizontalCount() - 1); // else select something random but not the first tile index
                    }
                    backgroundImageG.drawImage(backgrounds.getSprite(spriteX, 0), a*backgrounds.getSprite(0, 0).getHeight(), b*backgrounds.getSprite(0, 0).getHeight()); // drawing the tile in the image
                }
            }

            backgroundImageG.setDrawMode(Graphics.MODE_COLOR_MULTIPLY); // modifying draw mode to multiply
            backgroundImageG.setColor(GROUND_COLOR); // setting draw color to the static final ground color property
            backgroundImageG.fillRect(0, 0, roomDimension.getX(), roomDimension.getY()); // "coloring" the generated background

            backgroundImageG.flush(); // update of the image
        } catch (SlickException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * In game rendering
     * @param g the graphics to draw on
     */
    public static void renderBackground(Graphics g) {
        if (backgroundImage == null)
            return;

        g.drawImage(backgroundImage, 0, 0); // render of the background at position (0,0)
    }
}
