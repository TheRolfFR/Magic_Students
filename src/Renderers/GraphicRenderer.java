package Renderers;

import Main.TimeScale;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;

public class GraphicRenderer{

    private Animation animation;
    private Vector2f tileSize;

    private Image lastImage;

    public GraphicRenderer(String imgPath, Vector2f tileSize, int frameDuration) {
        this.tileSize = tileSize;
        Image image = null;

        try {
            image = new Image(imgPath, false, Image.FILTER_NEAREST);
        } catch (SlickException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

        float scale = tileSize.getY() / (float) image.getHeight();

        image = image.getScaledCopy(scale);

        this.animation = new Animation(new SpriteSheet(image, (int) tileSize.getX(), (int) tileSize.getY()), frameDuration);
        animation.start();
    }

    public Vector2f getTileSize(){return this.tileSize;}


    public void render(Graphics g, int centerX, int centerY, float angle) {
        // if game not paused
        if (TimeScale.getInGameTimeScale().getTimeScale() != 0f) {
            animation.draw(-10000, -10000);
            this.lastImage = animation.getCurrentFrame().copy();
            this.lastImage.rotate(angle);
        }

        if(this.lastImage != null) {
            g.drawImage(this.lastImage, centerX - this.tileSize.getX()/2, centerY - this.tileSize.getY()/2);
        }
    }
}
