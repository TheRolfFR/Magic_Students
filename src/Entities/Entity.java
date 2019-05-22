package Entities;

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

    abstract boolean can_move();

    public void move() {
        if (this.speed.length() != this.MAX_SPEED) {
            if (this.speed.length() + this.ACCELERATION_RATE > this.MAX_SPEED) {
                this.speed.scale(this.MAX_SPEED / this.speed.length());
            }
            else {
                this.speed.add(this.ACCELERATION_RATE);
            }
        }

        if (this.can_move()) {
            this.position.add(this.speed);
        }
    }
}
