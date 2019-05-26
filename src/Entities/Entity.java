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

    public Entity() {
        this.position = new Vector2f(0, 0);
        this.speed = new Vector2f(0, 0);
        this.MAX_SPEED = 0;
        this.ACCELERATION_RATE = 0;

        this.showDebugRect = false;
        this.renderer = null;
    }

    public Entity(float x, float y, float maxSpeed, float accelerationRate) {
        this.position = new Vector2f(x, y);
        this.speed = new Vector2f(0, 0);
        this.MAX_SPEED = maxSpeed;
        this.ACCELERATION_RATE = accelerationRate;

        this.showDebugRect = false;
        this.renderer = null;
    }
    public Entity(float x, float y, float vx, float vy, float maxSpeed, float accelerationRate) {
        this.position = new Vector2f(x, y);
        this.speed = new Vector2f(vx, vy);
        this.MAX_SPEED = maxSpeed;
        this.ACCELERATION_RATE = accelerationRate;

        this.showDebugRect = false;
        this.renderer = null;
    }
    public Entity(Vector2f position, Vector2f speed, float maxSpeed, float accelerationRate) {
        this.position = position;
        this.speed = speed;
        this.MAX_SPEED = maxSpeed;
        this.ACCELERATION_RATE = accelerationRate;

        this.showDebugRect = false;
        this.renderer = null;
    }

    public Vector2f getPosition() { return this.position; }
    public Vector2f getSpeed() { return this.speed; }
    public float getAccelerationRate() { return this.ACCELERATION_RATE; }
    protected abstract int getWidth();
    protected abstract int getHeight();

    void updateSpeed(Vector2f acceleration) {
        this.speed.add(acceleration);

        if (this.speed.length() > this.MAX_SPEED) {
            this.speed.normalise().scale(this.MAX_SPEED);
        }

        if (this.speed.getX() > -SPEED_THRESHOLD  && this.speed.getX() < SPEED_THRESHOLD) {
            this.speed.set(0, this.speed.getY());
        }

        if (this.speed.getY() > -SPEED_THRESHOLD && this.speed.getY() < SPEED_THRESHOLD) {
            this.speed.set(this.speed.getX(), 0);
        }
    }

    public void move() {
        this.position.add(this.speed);

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

    public abstract Shape getBounds();

    public void setShowDebugRect(boolean showDebugRect) {
        this.showDebugRect = showDebugRect;
    }

    public void setRenderer(SpriteRenderer renderer) {
        this.renderer = renderer;
    }

    public SpriteRenderer getRenderer() {
        return renderer;
    }

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

    public boolean collides(Entity opponent){
        return this.getBounds().intersects(opponent.getBounds());
    }

}
