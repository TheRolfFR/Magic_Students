package Entities;

import static java.lang.Math.sqrt;

public abstract class Entity {
    protected Vector position;
    protected Vector speed;
    private Vector acceleration;

    private float MAX_SPEED;
    private float MAX_COMPONENT;

    public Entity() {
        this.position = new Vector(0, 0);
        this.speed = new Vector(0, 0);
        this.MAX_SPEED = 0;
        this.MAX_COMPONENT = 0;
    }


    public Entity(float x, float y, float max_speed) {
        this.position = new Vector(x, y);
        this.speed = new Vector(0, 0);
        this.MAX_SPEED = max_speed;
        this.MAX_COMPONENT = (float) (max_speed / sqrt(2));
    }
    public Entity(float x, float y, float vx, float vy, float max_speed) {
        this.position = new Vector(x, y);
        this.speed = new Vector(vx, vy);
        this.MAX_SPEED = max_speed;
        this.MAX_COMPONENT = (float) (max_speed / sqrt(2));
    }
    public Entity(Vector position, Vector speed, float max_speed) {
        this.position = position;
        this.speed = speed;
        this.MAX_SPEED = max_speed;
        this.MAX_COMPONENT = (float) (max_speed / sqrt(2));
    }


    public void move() {
        this.speed.computeNorm();
        if (this.speed.getNorm() + this.acceleration.getNorm() >= this.MAX_SPEED) {
            // set speed to max speed
        }
        else {
            this.speed.add(this.acceleration);
        }

        this.position.add(this.speed);
    }
}
