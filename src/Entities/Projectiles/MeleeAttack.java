package Entities.Projectiles;

import org.newdawn.slick.geom.Vector2f;

/**
 * MeleeAttack is a one frame projectile that representes a melee attack from the player
 */
public class MeleeAttack extends Projectile {
    private static final int RADIUS = 35;
    private static int damage = 25;

    /**
     * Constructor
     * @param position position of the attack
     */
    public MeleeAttack(Vector2f position) {
        super(position.getX(), position.getY(), RADIUS, new Vector2f(0, 0), new Vector2f(0, 0)); //Create a projectile
        this.isDead=true; //create as dead allows this to last for one frame
    }

    /**
     * Increase the damage of futures attack
     * @param damageBuff the increase in damage
     */
    public static void increaseDamage(int damageBuff) {
        damage = damage + damageBuff;
    }

    /**
     * Getter for the speed
     * @return the norm of the speed of the projectile
     */
    @Override
    public float getMaxSpeed() {
        return 0;
    }

    /**
     * Getter for the damage
     * @return the damage of the projectile
     */
    @Override
    public int getDamage() {
        return MeleeAttack.damage;
    }

    /**
     * fade out animation
     */
    @Override
    public void fadeOut() {
    }
}

