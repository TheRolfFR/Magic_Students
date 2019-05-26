package Entities;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;
import java.lang.Math;

public class Arrow extends Projectile {
    private int hitBoxSize;

    public Arrow(float x, float y, float maxSpeed, float accelerationRate, String imagePath, Vector2f direction,
                 int hitBoxSize) {
        super(x, y, maxSpeed, accelerationRate, direction, imagePath);

        this.updateSpeed(direction.normalise().scale(this.getAccelerationRate()));
        this.hitBoxSize = hitBoxSize;
    }

    public Shape getBounds() {
        Vector2f arrowHeadPosition = position.copy().add(this.speed.copy().normalise().scale(
                this.getHeight() - hitBoxSize)).add(new Vector2f(-1, -1).scale(hitBoxSize * (float) Math.sqrt(2)));
        return new Rectangle(arrowHeadPosition.x, arrowHeadPosition.y, hitBoxSize, hitBoxSize);
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
