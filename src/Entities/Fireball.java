package Entities;

import Main.MainClass;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

public class Fireball extends Projectile {

    public static final int SIZE = 24;

    public Fireball(Vector2f position, Vector2f direction) {
        super(position.getX(), position.getY(), Snowball.MAX_SPEED, Snowball.ACCELERATION_RATE, SIZE, direction);

        try {
            this.setRenderer(new SpriteRenderer(
            this,
            new Vector2f(SIZE, SIZE),
            new Image("img/fireball_16x16.png", false, Image.FILTER_NEAREST).getScaledCopy( ((float) SIZE)/16f),
            new int[] {6},
            1000/6, direction.getTheta()- 90 ));

            this.updateSpeed(direction.normalise().scale(this.getAccelerationRate()));
        } catch (SlickException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }


    @Override
    protected int getWidth() {
        return SIZE;
    }

    @Override
    protected int getHeight() {
        return SIZE;
    }

    @Override
    public void fadeOut() {
        this.getRenderer().setOpacity(0);
    }
}
