package Entities.Projectiles;

import Renderers.ProjectileRenderer;
import org.newdawn.slick.geom.Vector2f;

/**
 * An arrow is the projectile shoot by the bowman
 */
public class Arrow extends Projectile implements ArrowConstants{

    private static final int RADIUS = 1; //hitbow radius
    private int damage = 15;
    private static final String IMG_PATH = "img/arrow_32x32.png";
    private static final Vector2f TILESIZE = new Vector2f(64, 64); //size of the image
    private static final int FRAME_DURATION = 10000;

    /**
     * Constructor
     * @param position the postition of the arrow
     * @param direction the direction of the arrow
     * @param damage the scale of damage
     */
    public Arrow(Vector2f position, Vector2f direction, int damage) {
        super(position.getX(), position.getY(), RADIUS, direction, TILESIZE); //create a projectile
        this.damage *= damage; //Apply the difficulty to the damage

        this.renderer = new ProjectileRenderer(this, IMG_PATH, TILESIZE, FRAME_DURATION);
    }

    /**
     * Getter for the speed
     * @return the norm of the speed of an arrow
     */
    @Override
    public float getMaxSpeed() {
        return MAX_SPEED;
    }

    /**
     * Getter for the damage
     * @return the damage of this arrow
     */
    @Override
    public int getDamage() {
        return this.damage;
    }

    /**
     * fade out animation
     */
    @Override
    public void fadeOut() {

    }
}
