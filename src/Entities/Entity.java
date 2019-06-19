package Entities;

import Main.GameStats;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

/**
 * The mother class of all our elements in game
 */
public abstract class Entity {

    private Vector2f position; //Coordonates of our object
    private Vector2f speed; //Speed of our object
    private int radius; //Radius of the hitbox

    private Vector2f tileSize; //Size of the image

    /**
     * Default constructor
     */
    public Entity() {
        this.position = new Vector2f(0, 0);
        this.speed = new Vector2f(0, 0);
        this.radius = 0;
        this.tileSize = new Vector2f(0, 0);
    }

    /**
     * Complex constructor
     * @param x initial x position of the entity
     * @param y initial y position of the entity
     * @param width the witdh of the image
     * @param height the height of the image
     * @param radius the radius of the hitbox
     */
    public Entity(float x, float y, int width, int height, int radius) {
        this.position = new Vector2f(x, y);
        this.speed = new Vector2f(0, 0);
        this.radius = radius;
        this.tileSize = new Vector2f(width, height);
    }

    /**
     * Complex constructor
     * @param x initial x position of the entity
     * @param y initial y position of the entity
     * @param radius the radius of the hitbox
     */
    public Entity(float x, float y, int radius) {
        this.position = new Vector2f(x, y);
        this.speed = new Vector2f(0, 0);
        this.radius = radius;
        this.tileSize = new Vector2f(radius*2, radius*2);
    }

    /**
     * Setter for the size of the image
     * @return size of the image
     */
    public Vector2f getTileSize() {
        return tileSize.copy();
    }

    /**
     * Setter for the size of the image
     * @param tileSize new size of the image
     */
    public void setTileSize(Vector2f tileSize) {
        this.tileSize = tileSize;
    }

    /**
     * Setter for the position
     * @param position new position of the entity
     */
    protected void setCenter(Vector2f position) {
        this.position = position;
    }

    /**
     * Getter for the position of the entity
     * @return postion of the entity
     */
    public Vector2f getCenter() {
        return this.position.copy();
    }

    /**
     * Setter for the speed of the entity
     * @param speed new speed of the entity
     */
    protected void setSpeed(Vector2f speed) {
        this.speed.set(speed);
    }

    /**
     * Setter for the speed of the entity
     * @param x first coordonate of the new speed
     * @param y second coordonate of the new speed
     */
    protected void setSpeed(float x, float y) {
        this.speed.set(x,y);
    }

    /**
     * Getter for the hitbox radius
     * @return hitbox radius
     */
    public int getRadius() { return this.radius; }

    /**
     * Getter for the entity speed
     * @return entity speed
     */
    public Vector2f getSpeed() { return this.speed.copy(); }

    /**
     * Getter for the hitbox
     * @return a Shape that reprensents the hitbox
     */
    public Shape getBounds() {
        return new Circle(getCenter().x, getCenter().y, getRadius()); //a circle with the same postion as the entity and the hitbox radius as radius
    }

    /**
     * In game rendering
     * @param g the graphics to draw on
     */
    public void render(Graphics g) {
        if (GameStats.getInstance().isShowDebugRect()) {
            g.draw(this.getBounds());
        }
    }

    /**
     * Returns if this entity collides with another one
     * @param other the other entity
     * @return whether it collides with another entity
     */
    public boolean collidesWith(Entity other) {
        if (other != this) { //If the two entities are not the same
            return (this.getCenter().sub(other.getCenter()).length() < this.radius + other.getRadius()); //return true if their distance is below the sum of their radius
        }
        else { //If they are the same entity
            return false; //an entity can't collides with itself
        }
    }
}
