package Renderer;

import Entities.Entity;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Vector2f;

public class ProjectileRenderer extends SpriteRenderer {
    private Animation animation;
    private double angle;

    public ProjectileRenderer(Entity entity, Image image, Vector2f tileSize, int duration) {
        super(entity);
        this.animation = new Animation(new SpriteSheet(image, (int) tileSize.getX(), (int) tileSize.getY()), duration);
        this.angle = entity.getSpeed().getTheta()- 90;
    }

    public final void setOpacity(float opacity) {
        this.opacity = opacity;
    }
    
    public void render(Graphics g, int x, int y) {
        Image copy = animation.getCurrentFrame().copy();
        copy.rotate((float) this.angle);
        g.drawImage(copy, x, y);
    }
}
