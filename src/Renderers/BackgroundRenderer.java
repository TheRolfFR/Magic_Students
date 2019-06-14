package Renderers;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

import java.util.Random;

public class BackgroundRenderer {

    private static final String BACKGROUND_PATH = "img/ground.png";
    private static final float BACKGROUND_SCALE = 2f;

    private static final Color GROUND_COLOR = new Color(0x4CAF50);

    private static SpriteSheet backgrounds = null;
    private static Image backgroundImage = null;

    private static Vector2f roomDimension = null;


    public static void generateBackground(GameContainer gc) {
        if (backgroundImage == null) {
            try {
                roomDimension = new Vector2f(gc.getWidth(), gc.getHeight());

                Image image = new Image(BACKGROUND_PATH, false, Image.FILTER_NEAREST);

                int scaledHeight = (int) (image.getHeight() * BACKGROUND_SCALE);
                image = image.getScaledCopy(BACKGROUND_SCALE);

                backgrounds = new SpriteSheet(image, scaledHeight, scaledHeight);

                regenerateBackground();
            } catch (SlickException e) {
                e.printStackTrace();
            }
        }
    }

    public static void regenerateBackground() {

        try {
            backgroundImage = new Image((int) roomDimension.getX(), (int) roomDimension.getY());

            Random randomReference = new Random();
            int spriteX;

            Graphics backgroundImageG = backgroundImage.getGraphics();
            for (int a = 0; a < roomDimension.getX()/backgrounds.getSprite(0, 0).getHeight()+1; a++) {
                for (int b = 0; b < roomDimension.getY()/backgrounds.getSprite(0, 0).getHeight()+1; b++) {
                    if (randomReference.nextInt(4) < 3) {
                        spriteX = 0;
                    } else {
                        spriteX = 1+ randomReference.nextInt(backgrounds.getHorizontalCount() - 1);
                    }
                    backgroundImageG.drawImage(backgrounds.getSprite(spriteX, 0), a*backgrounds.getSprite(0, 0).getHeight(), b*backgrounds.getSprite(0, 0).getHeight());
                }
            }

            backgroundImageG.setDrawMode(Graphics.MODE_COLOR_MULTIPLY);
            backgroundImageG.setColor(GROUND_COLOR);
            backgroundImageG.fillRect(0, 0, roomDimension.getX(), roomDimension.getY());

            backgroundImageG.flush();
        } catch (SlickException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void renderBackground(Graphics g) {
        if (backgroundImage == null)
            return;

        g.drawImage(backgroundImage, 0, 0);
    }

    public static boolean inRoomLimits(Shape shape) {
        return (shape.getX() != 0 && Math.round(shape.getX()+shape.getWidth()) != roomDimension.getX() && shape.getY() != 0 && Math.round(shape.getY() + shape.getHeight()) != roomDimension.getY());
    }
}
