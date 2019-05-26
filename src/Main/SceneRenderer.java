package Main;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

import java.util.Random;

public class SceneRenderer {
    private static Image backgroundImage = null;

    private static boolean roomShapeShowed = false;
    private static Rectangle roomShape = null;

    public static boolean isRoomShapeShowed() {
        return roomShapeShowed;
    }

    public static void setRoomShapeShowed(boolean roomShapeShowed) {
        SceneRenderer.roomShapeShowed = roomShapeShowed;
    }

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

                roomShape = new Rectangle(50,50,gc.getWidth()-100, gc.getHeight()-100);
            } catch (SlickException e) {
                e.printStackTrace();
            }
        }
    }

    static void renderBackground(Graphics g, int x, int y) {
        if (backgroundImage == null)
            return;

        g.drawImage(backgroundImage, x, y);

        if(isRoomShapeShowed()) {
            Color c = g.getColor();
            g.setColor(Color.green);
            g.draw(roomShape);
            g.setColor(c);
        }
    }

    public static boolean inRoomLimits(Shape shape) {
        System.out.println(roomShape.getWidth());
        return shape.contains(roomShape);
    }
}
