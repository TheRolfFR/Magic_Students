package Entities.Projectiles;

import Renderers.ProjectileRenderer;
import org.newdawn.slick.geom.Vector2f;

public class Arrow extends Projectile implements ArrowConstants{

    private static final int RADIUS = 1;
    private int damage = 15;
    private static final String IMG_PATH = "img/arrow_32x32.png";
    private static final Vector2f TILESIZE = new Vector2f(64, 64);
    private static final int FRAME_DURATION = 10000;

    public Arrow(Vector2f position, Vector2f direction, int damage) {
        super(position.getX(), position.getY(), RADIUS, direction, TILESIZE);
        this.damage *= damage;

        this.renderer = new ProjectileRenderer(this, IMG_PATH, TILESIZE, FRAME_DURATION);
    }

    public static int getArrowRadius() {
        return RADIUS;
    }

    @Override
    public float getMaxSpeed() {
        return ArrowConstants.MAX_SPEED;
    }

    @Override
    public int getDamage() {
        return this.damage;
    }

    @Override
    public void fadeOut() {

    }
}
