package Renderers;

import Entities.Projectiles.Projectile;
import Main.TimeScale;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;

public class ProjectileRenderer extends SpriteRenderer {
    private Animation animation;

    private Image lastImage;

    private double angle;

    public ProjectileRenderer(Projectile projectile, String imgPath, Vector2f tileSize, int frameDuration) {
        super(projectile, tileSize);

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
        this.angle = projectile.getDirection().getTheta() - 90;
        animation.start();
    }

    public final void setOpacity(float opacity) {
        this.opacity = opacity;
    }

    public void render(Graphics g, int x, int y) {
        // if game not paused
        if (TimeScale.getInGameTimeScale().getTimeScale() != 0f) {
            animation.draw(-10000, -10000);
            this.lastImage = animation.getCurrentFrame().copy();
            this.lastImage.rotate((float) this.angle);
        }

        if(this.lastImage != null) {
            g.drawImage(this.lastImage, x, y);
        }
    }
}
