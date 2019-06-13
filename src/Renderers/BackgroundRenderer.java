package Renderers;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

import java.util.Random;

public class BackgroundRenderer {

    private static final Color GROUND_COLOR = new Color(0x4CAF50);

    private static Image backgroundImage = null;

    private static Vector2f roomDimension = null;

    public static void generateBackground(String path, GameContainer gc) {
        if (backgroundImage == null) {
            try {
                float scale = 2;

                Image image = new Image(path, false, Image.FILTER_NEAREST);

                int scaledHeight = image.getHeight() * (int) scale;

                image = image.getScaledCopy(2);

                SpriteSheet backgrounds = new SpriteSheet(image, scaledHeight, scaledHeight);

                backgroundImage = new Image(gc.getWidth(), gc.getHeight());
                Random randomReference = new Random();
                int spriteX;

                Graphics backgroundImageG = backgroundImage.getGraphics();
                for (int a = 0; a < gc.getWidth()/scaledHeight+1; a++) {
                    for (int b = 0; b < gc.getHeight()/scaledHeight+1; b++) {
                        if (randomReference.nextInt(4) < 3) {
                            spriteX = 0;
                        } else {
                            spriteX = 1+ randomReference.nextInt(backgrounds.getHorizontalCount() - 1);
                        }
                        backgroundImageG.drawImage(backgrounds.getSprite(spriteX, 0), a*scaledHeight, b*scaledHeight);
                    }
                }

                backgroundImageG.setDrawMode(Graphics.MODE_COLOR_MULTIPLY);
                backgroundImageG.setColor(GROUND_COLOR);
                backgroundImageG.fillRect(0, 0, gc.getWidth(), gc.getHeight());

                backgroundImageG.flush();

                roomDimension = new Vector2f(gc.getWidth(), gc.getHeight());
            } catch (SlickException e) {
                e.printStackTrace();
            }
        }
    }

    public static void renderBackground(Graphics g, int x, int y) {
        if (backgroundImage == null)
            return;

        g.drawImage(backgroundImage, x, y);
    }

    public static boolean inRoomLimits(Shape shape) {
        return (shape.getX() != 0 && Math.round(shape.getX()+shape.getWidth()) != roomDimension.getX() && shape.getY() != 0 && Math.round(shape.getY() + shape.getHeight()) != roomDimension.getY());
    }
}
