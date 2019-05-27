package Entities;

import Main.MainClass;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

public abstract class Entity {
    protected Vector2f position;
    protected Vector2f speed;

    private float MAX_SPEED;
    private float ACCELERATION_RATE;

    private static final float SPEED_THRESHOLD = 0.5f;

    protected boolean showDebugRect;
    private SpriteRenderer renderer;

    /**
     * Returns hitbox top left corner
     * @return hitbox top left corner
     */
    public Vector2f getPosition() { return this.position; }

    /**
     * Returns entity speed
     * @return entity speed
     */
    public Vector2f getSpeed() { return this.speed; }

    /**
     * Returns acceleration factor
     * @return acceleration factor
     */
    public float getAccelerationRate() { return this.ACCELERATION_RATE; }

    /**
     * Returns the hitbox width of the entity
     * @return the hitbox width of the entity
     */
    protected abstract int getWidth();

    /**
     * Returns the hitbox height
     * @return the hitbox height
     */
    protected abstract int getHeight();

    /**
     * Returns the associated hitbox shape
     * @return the associated hitbox shape
     */
    public abstract Shape getBounds();

    /**
     * Allows to show the debug rect shape
     * @param showDebugRect show or not the shape
     */
    public void setShowDebugRect(boolean showDebugRect) {
        this.showDebugRect = showDebugRect;
    }

    /**
     * Assigns a renderer to the entity
     * @param renderer renderer to apply
     */
    public void setRenderer(SpriteRenderer renderer) {
        this.renderer = renderer;
    }

    /**
     * Returns the entity sprite renderer
     * @return the entity sprite renderer
     */
    public SpriteRenderer getRenderer() {
        return renderer;
    }

    /**
     * Default constructor
     */
    public Entity() {
        this.position = new Vector2f(0, 0);
        this.speed = new Vector2f(0, 0);
        this.MAX_SPEED = 0;
        this.ACCELERATION_RATE = 0;

        this.showDebugRect = false;
        this.renderer = null;
    }

    /**
     * Simple constructor with position, maximum speed and acceleration rate
     * @param x initial x position of the entity
     * @param y initial y position of the entity
     * @param maxSpeed maximum speed of the entity
     * @param accelerationRate acceleration factor of the entity
     */
    public Entity(float x, float y, float maxSpeed, float accelerationRate) {
        this.position = new Vector2f(x, y);
        this.speed = new Vector2f(0, 0);
        this.MAX_SPEED = maxSpeed;
        this.ACCELERATION_RATE = accelerationRate;

        this.showDebugRect = false;
        this.renderer = null;
    }

    /**
     * Simple constructor with position, initkal speed, maximum speed and acceleration rate
     * @param x initial x position of the entity
     * @param y initial y position of the entity
     * @param vx initial x speed ot the entity
     * @param vy initial y speed of the entity
     * @param maxSpeed maximum speed of the entity
     * @param accelerationRate acceleration factor of the entity
     */
    public Entity(float x, float y, float vx, float vy, float maxSpeed, float accelerationRate) {
        this.position = new Vector2f(x, y);
        this.speed = new Vector2f(vx, vy);
        this.MAX_SPEED = maxSpeed;
        this.ACCELERATION_RATE = accelerationRate;

        this.showDebugRect = false;
        this.renderer = null;
    }

    /**
     * Simple constructor with position, initial speed, maximum speed and acceleration rate
     * @param position initial position of the entity
     * @param speed intial speed of the entity
     * @param maxSpeed maximum speed of the entity
     * @param accelerationRate acceleration factor of the entity
     */
    public Entity(Vector2f position, Vector2f speed, float maxSpeed, float accelerationRate) {
        this.position = position;
        this.speed = speed;
        this.MAX_SPEED = maxSpeed;
        this.ACCELERATION_RATE = accelerationRate;

        this.showDebugRect = false;
        this.renderer = null;
    }

    /**
     * Updates speed with an acceleration
     * @param acceleration the given acceleration
     */
    void updateSpeed(Vector2f acceleration) {
        this.speed.add(acceleration);

        if (this.speed.length() > this.MAX_SPEED) {
            this.speed.normalise().scale(this.MAX_SPEED * MainClass.getInGameTimeScale().getTimeScale());
        }

        if (this.speed.getX() > -SPEED_THRESHOLD  && this.speed.getX() < SPEED_THRESHOLD) {
            this.speed.set(0, this.speed.getY());
        }

        if (this.speed.getY() > -SPEED_THRESHOLD && this.speed.getY() < SPEED_THRESHOLD) {
            this.speed.set(this.speed.getX(), 0);
        }
    }

    /**
     * Moves the entity
     */
    public void move() {
        this.position.add(this.speed.scale(MainClass.getInGameTimeScale().getTimeScale()));

        if (this.position.x < 0) {
            this.position.set(0, this.position.y);
        }
        if (this.position.x + this.getWidth() >= MainClass.WIDTH) {
            this.position.set(MainClass.WIDTH - this.getWidth(), this.position.y);
        }
        if (this.position.y < 0) {
            this.position.set(this.position.x, 0);
        }
        if (this.position.y + this.getHeight() >= MainClass.HEIGHT) {
            this.position.set(this.position.x, MainClass.HEIGHT - this.getHeight());
        }
    }

    /**
     * In game rendering
     * @param g the graphics to draw on
     */
    public void render(Graphics g) {
        if (this.renderer != null) {
            this.renderer.render();
        }

        if (this.showDebugRect) {
            Color c = g.getColor();

            g.setColor(Color.white);
            g.drawRect(Math.round(this.getPosition().x), Math.round(this.getPosition().y),
                    this.getWidth(), this.getHeight());

            g.setColor(c);
        }
    }

    /**
     * Returns if this entity collides with another one
     * @param other the other entity
     * @return whether it collides with another entity
     */
    public boolean collides(Entity other){
        return this.getBounds().intersects(other.getBounds());
    }
}
