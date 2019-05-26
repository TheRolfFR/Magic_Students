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

    /**
     * Returns the associated hitbox shape
     * @return the associated hitbox shape
     */
    @Override
    public Shape getBounds(){
        return new Circle(position.x + this.getWidth()/2, position.y + this.getHeight()/2, this.getWidth()/2);
    }

    /**
     * Returns snowball width
     * @return snowball width
     */
    @Override
    protected int getWidth() {
        return this.image.getWidth();
    }

    /**
     * Returns snowball height
     * @return snowball height
     */
    @Override
    protected int getHeight() {
        return this.image.getHeight();
    }

    /**
     * In game rendering
     * @param g the grapgics to draw on
     */
    public void render(Graphics g) {
        super.render(g);
        if(showDebugRect) {
            g.draw(this.getBounds());
        }
    }

    /**
     * Constructor made super simple
     * @param position the initial vector position
     * @param direction the direction vector
     */
    public Snowball(Vector2f position, Vector2f direction) {
        super(position.getX(), position.getY(), MAX_SPEED, ACCELERATION_RATE, direction, IMAGE_PATH);

        this.updateSpeed(direction.normalise().scale(this.getAccelerationRate()));
    }

    /**
     * Constructor made simple
     * @param x the initial x position of the snowball
     * @param y the initial x position of the snowball
     * @param direction the direction of the snowball
     */
    public Snowball(float x, float y, Vector2f direction) {
        super(x, y, MAX_SPEED, ACCELERATION_RATE, direction, IMAGE_PATH);

        this.updateSpeed(direction.normalise().scale(this.getAccelerationRate()));
    }

    /**
     * Complex constructor
     * @param x initial x position of the entity
     * @param y initial y position of the entity
     * @param maxSpeed maximum speed of the entity
     * @param accelerationRate acceleration factor of the entity
     * @param imagePath the image ref to the the snowball image
     * @param direction the direction vector
     */
    public Snowball(float x, float y, float maxSpeed, float accelerationRate, String imagePath, Vector2f direction) {
        super(x, y, maxSpeed, accelerationRate, direction, imagePath);

        this.updateSpeed(direction.normalise().scale(this.getAccelerationRate()));
    }

    /**
     * Fade out operation for the snowball
     */
    @Override
    public void fadeOut() {
        this.image.setAlpha(opacity);
    }
}
