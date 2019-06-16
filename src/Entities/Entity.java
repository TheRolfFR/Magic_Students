package Entities;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

public abstract class Entity {
    private Vector2f position;
    private Vector2f speed;
    private int radius;

    private Vector2f tileSize;

    protected boolean showDebugRect;



    public Vector2f getTileSize() {
        return tileSize.copy();
    }

    public void setTileSize(Vector2f tileSize) {
        this.tileSize = tileSize;
    }

    protected void setCenter(Vector2f position) {
        this.position = position;
    }

    public Vector2f getCenter() {
        return this.position.copy();
    }

    protected void setSpeed(Vector2f speed) {
        this.speed.set(speed);
    }

    protected void setSpeed(float x, float y) {
        this.speed.set(x,y);
    }

    /**
     * Returns hitbox radius
     * @return hitbox radius
     */
    public int getRadius() { return this.radius; }

    /**
     * Returns entity speed
     * @return entity speed
     */
    public Vector2f getSpeed() { return this.speed.copy(); }

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
        this.showDebugRect = false;
        this.tileSize = new Vector2f(0, 0);
    }

    public Entity(float x, float y, int width, int height, int radius) {
        this.position = new Vector2f(x, y);
        this.speed = new Vector2f(0, 0);
        this.radius = radius;
        this.showDebugRect = false;
        this.tileSize = new Vector2f(width, height);
    }

    public Entity(float x, float y, int radius) {
        this.position = new Vector2f(x, y);
        this.speed = new Vector2f(0, 0);
        this.radius = radius;
        this.showDebugRect = false;
        this.tileSize = new Vector2f(radius*2, radius*2);
    }

    /**
     * Moves the entity
     */
    public abstract void move();

    public Shape getBounds() {
        return new Circle(getCenter().x, getCenter().y, getRadius());
    }

    /**
     * In game rendering
     * @param g the graphics to draw on
     */
    public void render(Graphics g) {
        if (this.showDebugRect) {
            g.draw(this.getBounds());
        }
    }

    /**
     * Returns if this entity collides with another one
     * @param other the other entity
     * @return whether it collides with another entity
     */
    public boolean collidesWith(Entity other) {
        if (other != this) {
            return (this.getCenter().sub(other.getCenter()).length() < this.radius + other.getRadius());
        }
        else {
            return false;
        }
    }
}
