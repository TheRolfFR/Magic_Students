package Entities;

import Main.MainClass;
import Main.SceneRenderer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

public class Snowball extends Projectile {

    private static final float MAX_SPEED = 250/ MainClass.MAX_FPS;
    private static final float ACCELERATION_RATE = 135/ MainClass.MAX_FPS;
    private static final String IMAGE_PATH = "img/snowball.png";

    public Snowball(float x, float y, Vector2f direction) {
        super(x, y, MAX_SPEED, ACCELERATION_RATE, direction, IMAGE_PATH);

        this.updateSpeed(direction.normalise().scale(this.getAccelerationRate()));
    }

    public Snowball(float x, float y, float maxSpeed, float accelerationRate, String imagePath, Vector2f direction) {
        super(x, y, maxSpeed, accelerationRate, direction, imagePath);

        this.updateSpeed(direction.normalise().scale(this.getAccelerationRate()));
    }

    @Override
    public Shape getBounds(){
        return new Circle(position.x + this.getWidth()/2, position.y + this.getHeight()/2, this.getWidth()/2);
    }

    @Override
    protected int getWidth() {
        return this.image.getWidth();
    }

    @Override
    protected int getHeight() {
        return this.image.getHeight();
    }

    public void render(Graphics g) {
        super.render(g);
        if(showDebugRect) {
            g.draw(this.getBounds());
        }
    }

    @Override
    public void fadeOut() {
        this.image.setAlpha(opacity);
    }
}
