package Entities.Projectiles;

import Entities.Projectiles.Projectile;
import org.newdawn.slick.geom.Vector2f;

public class MeleeAttack extends Projectile {

    private static final int RADIUS = 15;

    public MeleeAttack(Vector2f position, Vector2f direction) {
        super(position.getX(), position.getY(), 0, 0, RADIUS, direction);
        this.isDead=true;
    }

    public static int getMeleeRadius(){return RADIUS;} //package private

    @Override
    public void fadeOut() {
    }
}

