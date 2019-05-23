package Entities;

import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

public abstract class Entity {
    protected Vector2f position;
    protected Vector2f speed;

    private float MAX_SPEED;
    private float ACCELERATION_RATE;

    public Entity() {
        this.position = new Vector2f(0, 0);
        this.speed = new Vector2f(0, 0);
        this.MAX_SPEED = 0;
        this.ACCELERATION_RATE = 0;
    }

    public Entity(float x, float y, float maxSpeed, float accelerationRate) {
        this.position = new Vector2f(x, y);
        this.speed = new Vector2f(0, 0);
        this.MAX_SPEED = maxSpeed;
        this.ACCELERATION_RATE = accelerationRate;
    }
    public Entity(float x, float y, float vx, float vy, float maxSpeed, float accelerationRate) {
        this.position = new Vector2f(x, y);
        this.speed = new Vector2f(vx, vy);
        this.MAX_SPEED = maxSpeed;
        this.ACCELERATION_RATE = accelerationRate;
    }
    public Entity(Vector2f position, Vector2f speed, float maxSpeed, float accelerationRate) {
        this.position = position;
        this.speed = speed;
        this.MAX_SPEED = maxSpeed;
        this.ACCELERATION_RATE = accelerationRate;
    }

    public Vector2f getPosition() { return this.position; }
    public Vector2f getSpeed() { return this.speed; }

    abstract boolean canMove();

    public void updateSpeed(Vector2f acceleration) {
        acceleration.scale(this.ACCELERATION_RATE);

        // a changer avec newSpeed = this.speed.add(acceleration); car la méthode add renvoie un vecteur qui est la somme, voir doc @Xwaler
        Vector2f newSpeed = this.speed.copy();
        newSpeed.add(acceleration);

        if (newSpeed.length() <= this.MAX_SPEED) {
            this.speed = newSpeed;
        }
        else {
            this.speed = newSpeed.normalise().scale(this.MAX_SPEED);
        }
    }

    public void move() {
        if (this.canMove()) {
            this.position.add(this.speed);
        }
    }

    public abstract Shape getBounds();

    public boolean collides(Entity opponent){
        return this.getBounds().intersects(opponent.getBounds());
    }

}
