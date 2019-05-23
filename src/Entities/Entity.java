package Entities;

import Main.MainClass;
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
    public float getAccelerationRate() { return this.ACCELERATION_RATE; }
    protected abstract int getWidth();
    protected abstract int getHeight();

    public void updateSpeed(Vector2f acceleration) {
        this.speed.add(acceleration);

        if (this.speed.length() > this.MAX_SPEED) {
            this.speed.normalise().scale(this.MAX_SPEED);
        }
    }

    public void move() {
        this.position.add(this.speed);

        if (this.position.x < 0) {
            this.position.set(0, this.position.y);
        }
        if (this.position.x + this.getWidth() + 1 >= MainClass.WIDTH) {
            this.position.set(MainClass.WIDTH - this.getWidth() - 1, this.position.y);
        }
        if (this.position.y < 0) {
            this.position.set(this.position.x, 0);
        }
        if (this.position.y + this.getHeight() + 1 >= MainClass.HEIGHT) {
            this.position.set(this.position.x, MainClass.HEIGHT - this.getHeight() - 1);
        }
    }

    public abstract Shape getBounds();

    public boolean collides(Entity opponent){
        return this.getBounds().intersects(opponent.getBounds());
    }
}
