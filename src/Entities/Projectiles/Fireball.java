package Entities.Projectiles;

import Renderers.ProjectileRenderer;
import org.newdawn.slick.geom.Vector2f;

public class Fireball extends Projectile {

    private static final float SCALE = 1.4f;
    private static int damage = 100;
    private static final int RADIUS = (int) (37/2f*SCALE);
    private static final String IMG_PATH = "img/fireball/fireball_80x37.png";
    private static final Vector2f TILESIZE = new Vector2f(37, 80).scale(SCALE);
    private static final int FRAME_DURATION = 17*4;

    public Fireball(Vector2f position, Vector2f direction) {
        super(position.getX(), position.getY(), Snowball.MAX_SPEED, Snowball.ACCELERATION_RATE, RADIUS, direction, TILESIZE);

        this.updateSpeed(direction.normalise().scale(this.getAccelerationRate()));

        this.renderer = new ProjectileRenderer(this, IMG_PATH, TILESIZE, FRAME_DURATION);
    }

    @Override
    public int getDamage() {
        return Fireball.damage;
    }

    public static void increaseDamage(int damageBuff) {
        damage = damage + damageBuff;
    }

    @Override
    public void fadeOut() {
        if (this.renderer != null)
            this.renderer.setOpacity(0);
    }

    public static int getFireballRadius() {
        return RADIUS;
    }
}
