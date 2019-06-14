package Renderers;

import Entities.Entity;
import Main.TimeScale;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;

public class GraphicRenderer extends SpriteRenderer{

    private Animation animation;

    private Image lastImage;

    public GraphicRenderer(Entity entity, String imgPath, Vector2f tileSize, int frameDuration) {
        super(entity, tileSize);
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


    public void render(Graphics g, int x, int y) {
        // if game not paused
        if (TimeScale.getInGameTimeScale().getTimeScale() != 0f) {
            animation.draw(-10000, -10000);
            this.lastImage = animation.getCurrentFrame().copy();
        }

        if(this.lastImage != null) {
            g.drawImage(this.lastImage, x - super.tileSize.getX()/2, y - super.tileSize.getY()/2);
        }
    }
}
