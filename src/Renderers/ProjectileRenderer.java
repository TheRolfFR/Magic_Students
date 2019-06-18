package Renderers;

import Entities.Projectiles.Projectile;
import Main.TimeScale;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;

/**
 * projectile renderer for fireballs and arrows
 */
public class ProjectileRenderer extends SpriteRenderer {
    private Animation animation;

    private Image lastImage;

    private double angle;

    /**
     * Default renderer : tries to load the image, crash otherwise
     * @param projectile the projectile related
     * @param imgPath the path of the image
     * @param tileSize the size of the image
     * @param frameDuration the duration in ms of a frame of the animation
     */
    public ProjectileRenderer(Projectile projectile, String imgPath, Vector2f tileSize, int frameDuration) {
        super(projectile, tileSize);

        Image image = null;
        try {
            image = new Image(imgPath, false, Image.FILTER_NEAREST);
        } catch (SlickException e) {
            e.printStackTrace();
            System.exit(1);
        }

        float scale = tileSize.getY() / (float) image.getHeight();

        image = image.getScaledCopy(scale);

        this.animation = new Animation(new SpriteSheet(image, (int) tileSize.getX(), (int) tileSize.getY()), frameDuration);
        this.angle = projectile.getDirection().getTheta() - 90;
        animation.start();
    }

    /**
     * Opacity setter
     * @param opacity the opacity to set
     */
    public final void setOpacity(float opacity) {
        this.opacity = Math.min(1f, Math.max(opacity, 0f));
    }

    /**
     * In game rendering
     * @param g the graphics to draw on
     * @param x the x center position
     * @param y the y center position
     */
    public void render(Graphics g, int x, int y) {
        // if game not paused
        if (TimeScale.getInGameTimeScale().getTimeScale() != 0f) {
            animation.draw(-10000, -10000);
            this.lastImage = animation.getCurrentFrame().copy();
            this.lastImage.rotate((float) this.angle);
        }

        // avoid NullPointerException
        if (this.lastImage != null) {
            g.drawImage(this.lastImage, x, y);
        }
    }
}
