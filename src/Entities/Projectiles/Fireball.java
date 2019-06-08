package Entities.Projectiles;

import Renderer.ProjectileRenderer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

public class Fireball extends Projectile {

    private static final int SIZE = 16;

    public Fireball(Vector2f position, Vector2f direction) {
        super(position.getX(), position.getY(), Snowball.MAX_SPEED, Snowball.ACCELERATION_RATE, SIZE, direction);

        this.updateSpeed(direction.normalise().scale(this.getAccelerationRate()));
        try {
            this.renderer = new ProjectileRenderer(this, new Image("img/fireball_16x16.png", false, Image.FILTER_NEAREST).getScaledCopy(2),new Vector2f(SIZE*2, SIZE*2), 1000/12);
        } catch (SlickException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public void fadeOut() {
        if(this.renderer != null)
            this.renderer.setOpacity(0);
    }

    public static int getFireballRadius(){return SIZE;}
}
