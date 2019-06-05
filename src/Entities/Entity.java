package Entities;

import Main.MainClass;

import Renderer.SpriteRenderer;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

public abstract class Entity {
    protected Vector2f position;
    protected Vector2f speed;
    protected int radius;

    protected Vector2f tileSize;

    private float MAX_SPEED;
    private float ACCELERATION_RATE;

    private static final float SPEED_THRESHOLD = 0.5f;

    protected boolean showDebugRect;

    public void setTileSize(Vector2f tileSize) {
        this.tileSize = tileSize;
    }

    /**
     * Returns hitbox top left corner
     * @return hitbox top left corner
     */
    public Vector2f getPosition() { return this.position; }

    /**
     * Returns hitbox radius
     * @return hitbox radius
     */
    public int getRadius() { return this.radius; }

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
     * Allows to show the debug rect shape
     * @param showDebugRect show or not the shape
     */
    public void setShowDebugRect(boolean showDebugRect) {
        this.showDebugRect = showDebugRect;
    }

    /**
     * Default constructor
     */
    public Entity() {
        this.position = new Vector2f(0, 0);
        this.speed = new Vector2f(0, 0);
        this.radius = 0;
        this.MAX_SPEED = 0;
        this.ACCELERATION_RATE = 0;

        this.showDebugRect = false;
        this.tileSize = new Vector2f(0, 0);
    }

    /**
     * Simple constructor with position, maximum speed and acceleration rate
     * @param x initial x position of the entity
     * @param y initial y position of the entity
     * @param maxSpeed maximum speed of the entity
     * @param accelerationRate acceleration factor of the entity
     * @param radius the hitbox radius
     */
    public Entity(float x, float y, float maxSpeed, float accelerationRate, int radius) {
        this.position = new Vector2f(x, y);
        this.speed = new Vector2f(0, 0);
        this.MAX_SPEED = maxSpeed;
        this.ACCELERATION_RATE = accelerationRate;
        this.radius = radius;

        this.showDebugRect = false;
        this.tileSize = new Vector2f(0, 0);
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
    public abstract void move();

    public Vector2f getCenter(){
        return new Vector2f(position.copy().x+this.tileSize.getX()/2,position.copy().y+this.tileSize.getY()/2);
    }

    /**
     * In game rendering
     * @param g the graphics to draw on
     */
    public void render(Graphics g) {
        if (this.showDebugRect) {
            Color c = g.getColor();

            g.setColor(Color.white);
            Vector2f center = this.getCenter();
            g.drawOval(center.x, center.y, radius*2, radius*2);

            g.setColor(c);
        }
    }

    /**
     * Returns if this entity collides with another one
     * @param other the other entity
     * @return whether it collides with another entity
     */
    public boolean collides(Entity other){
        return (this.getCenter().sub(other.getCenter()).length() < this.radius+other.getRadius());
    }
}
