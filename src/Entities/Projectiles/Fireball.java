package Entities.Projectiles;

import Renderers.ProjectileRenderer;
import org.newdawn.slick.geom.Vector2f;

/**
 * A fireball is a spell that the player can use
 */
public class Fireball extends Projectile implements FireballConstants{

    private static final float SCALE = 1.4f;
    private static int damage = 100;
    private static final int RADIUS = (int) (37/2f*SCALE); //hitbox radius
    private static final String IMG_PATH = "img/fireball/fireball_80x37.png";
    private static final Vector2f TILESIZE = new Vector2f(37, 80).scale(SCALE); //Size of the image
    private static final int FRAME_DURATION = 17*4;

    /**
     * Constructor
     * @param position position of the fireball
     * @param direction direction of the fireball
     */
    public Fireball(Vector2f position, Vector2f direction) {
        super(position.getX(), position.getY(), RADIUS, direction, TILESIZE); //create a projectile
        this.renderer = new ProjectileRenderer(this, IMG_PATH, TILESIZE, FRAME_DURATION);
    }

    /**
     * Getter for the speed
     * @return the norm of the speed of a fireball
     */
    @Override
    public float getMaxSpeed() {
        return FireballConstants.MAX_SPEED;
    }

    /**
     * Getter for the damage
     * @return return the damage of this fireball
     */
    @Override
    public int getDamage() {
        return Fireball.damage;
    }

    /**
     * Increase the damage of future fireballs
     * @param damageBuff the increase in damage
     */
    public static void increaseDamage(int damageBuff) {
        damage = damage + damageBuff;
    }

    /**
     * fade out animation
     */
    @Override
    public void fadeOut() {
    }

    /**
     * Getter for the radius
     * @return the radius of a fireball
     */
    public static int getFireballRadius() {
        return RADIUS;
    }
}
