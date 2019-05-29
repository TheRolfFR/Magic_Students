package Entities;

import Main.MainClass;
import org.newdawn.slick.geom.Vector2f;

public class Arrow extends Projectile {

    public Arrow(float x, float y, float maxSpeed, float accelerationRate, String imagePath, Vector2f direction,
                 int hitBoxSize, int radius) {
        super(x, y, maxSpeed, accelerationRate, direction, imagePath, radius);

        this.updateSpeed(direction.normalise().scale(this.getAccelerationRate()));
        this.radius = hitBoxSize;
    }


    @Override
    protected int getWidth() {
        return this.image.getWidth();
    }

    @Override
    protected int getHeight() {
        return this.image.getHeight();
    }

    @Override
    public void fadeOut() {

    }
}
