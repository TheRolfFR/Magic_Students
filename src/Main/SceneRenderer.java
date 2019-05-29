package Main;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

import java.util.Random;

public class SceneRenderer {
    private static Image backgroundImage = null;

    private static Vector2f roomDimension = null;

    static void generateBackground(String path, GameContainer gc) {
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

                for (int a = 0; a < gc.getWidth()/scaledHeight; a++) {
                    for (int b = 0; b < gc.getHeight()/scaledHeight; b++) {
                        if (randomReference.nextInt(4) < 3) {
                            spriteX = 0;
                        } else {
                            spriteX = 1+ randomReference.nextInt(backgrounds.getHorizontalCount() - 1);
                        }
                        backgroundImage.getGraphics().drawImage(backgrounds.getSprite(spriteX, 0), a*scaledHeight, b*scaledHeight);
                    }
                }

                backgroundImage.getGraphics().flush();

                roomDimension = new Vector2f(gc.getWidth(), gc.getHeight());
            } catch (SlickException e) {
                e.printStackTrace();
            }
        }
    }

    static void renderBackground(Graphics g, int x, int y) {
        if (backgroundImage == null)
            return;

        g.drawImage(backgroundImage, x, y);
    }

    public static boolean inRoomLimits(Shape shape) {
        return shape.getX() != 0 && Math.round(shape.getX()+shape.getWidth()) != roomDimension.getX() && shape.getY() != 0 && Math.round(shape.getY() + shape.getHeight()) != roomDimension.getY();
    }
}
