package Entities;

import org.newdawn.slick.geom.Vector2f;

public class Fireball extends Projectile {

    public static final int SIZE = 24;

    public Fireball(Vector2f position, Vector2f direction) {
        super(position.getX(), position.getY(), Snowball.MAX_SPEED, Snowball.ACCELERATION_RATE, SIZE, direction);
    }


    protected int getWidth() {
        return SIZE;
    }

    protected int getHeight() {
        return SIZE;
    }

    @Override
    public void fadeOut() {
    }
}
