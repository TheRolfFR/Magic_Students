package Renderer;

import Entities.Entity;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Vector2f;

public class ItemRenderer extends SpriteRenderer {
    private Animation animation;

    public ItemRenderer(Entity entity, Image image, Vector2f tileSize, int duration) {
        super(entity, tileSize);
        this.animation = new Animation(new SpriteSheet(image, (int) tileSize.getX(), (int) tileSize.getY()), duration);
    }

    public final void setOpacity(float opacity) {
        this.opacity = opacity;
    }

    public void render(Graphics g, int x, int y) {
        Image copy = animation.getCurrentFrame().copy();
        g.drawImage(copy, x, y);
    }
}
