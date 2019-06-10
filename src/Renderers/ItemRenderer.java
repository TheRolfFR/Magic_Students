package Renderers;

import Entities.Entity;
import Main.TimeScale;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Vector2f;

public class ItemRenderer extends SpriteRenderer {
    private Animation animation;
    private Image lastImage;

    public ItemRenderer(Entity entity, Image image, Vector2f tileSize, int duration) {
        super(entity, tileSize);
        this.animation = new Animation(new SpriteSheet(image, (int) tileSize.getX(), (int) tileSize.getY()), duration);
    }

    public final void setOpacity(float opacity) {
        this.opacity = opacity;
    }

    public void render(Graphics g, int x, int y) {
        // if game not paused
        if(TimeScale.getInGameTimeScale().getTimeScale() != 0f) {
            this.lastImage = animation.getCurrentFrame().copy();
        }

        if(lastImage != null) {
            g.drawImage(this.lastImage, x, y);
        }
    }
}
