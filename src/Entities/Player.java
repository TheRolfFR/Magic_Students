package Entities;

import Entities.Attacks.MeleeAttack;
import Entities.Attacks.RangedAttack;

import Main.MainClass;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

import java.util.LinkedList;

public class Player extends LivingBeing implements MeleeAttack, RangedAttack, KeyListener {
    protected int width;
    protected int height;

    private boolean keyUp;
    private boolean keyDown;
    private boolean keyLeft;
    private boolean keyRight;

    private LinkedList<Projectile> playerProjectiles;

    @Override
    public int getWidth() { return this.width; }
    @Override
    public int getHeight() { return this.height; }

    public Player(float x, float y, int width, int height, float maxSpeed, float accelerationRate) {
        super(x, y, maxSpeed, accelerationRate, 100, 10);
        this.width = width;
        this.height = height;

        this.keyUp = false;
        this.keyDown = false;
        this.keyLeft = false;
        this.keyRight = false;

        this.playerProjectiles = new LinkedList<Projectile>();
    }

    private void doAttack() {
        Vector2f direction = new Vector2f(MainClass.getInput().getMouseX(), MainClass.getInput().getMouseY()).sub(this.getPosition());
        this.playerProjectiles.add(new Snowball(this.position.getX(), this.position.getY(), direction));
    }

    @Override
    public Shape getBounds() {
        return new Rectangle(position.x, position.y, width, height);
    }

    public void update() {
        if (this.keyUp || this.keyDown || this.keyLeft || this.keyRight) {
            if (this.keyUp) {
                this.updateSpeed(new Vector2f(0, -1).scale(this.getAccelerationRate()));
            }
            if (this.keyDown) {
                this.updateSpeed(new Vector2f(0, 1).scale(this.getAccelerationRate()));
            }
            if (this.keyLeft) {
                this.updateSpeed(new Vector2f(-1, 0).scale(this.getAccelerationRate()));
            }
            if (this.keyRight) {
                this.updateSpeed(new Vector2f(1, 0).scale(this.getAccelerationRate()));
            }
        }
        else {
            this.updateSpeed(this.getSpeed().negate().scale(0.2f));
        }
        this.move();

        Projectile p;
        for(int i = 0; i < playerProjectiles.size(); i++) {
            p = playerProjectiles.get(i);
            p.update();

            if(p.isFadeOut()) {
                playerProjectiles.remove(i);
            }
        }
    }

    public void render(Graphics g) {
        super.render(g);

        for(Projectile p : playerProjectiles) {
            p.render(g);
        }
    }

    public void keyPressed(int key, char c) {
        switch (key) {
            case Input.KEY_UP:
            case Input.KEY_Z:
                this.keyUp = true;
                break;
            case Input.KEY_DOWN:
            case Input.KEY_S:
                this.keyDown = true;
                break;
            case Input.KEY_LEFT:
            case Input.KEY_Q:
                this.keyLeft = true;
                break;
            case Input.KEY_RIGHT:
            case Input.KEY_D:
                this.keyRight = true;
                break;
            case Input.KEY_L:
                this.doAttack();
                break;
        }
    }

    public void keyReleased(int key, char c) {
        switch (key) {
            case Input.KEY_UP:
            case Input.KEY_Z:
                this.keyUp = false;
                break;
            case Input.KEY_DOWN:
            case Input.KEY_S:
                this.keyDown = false;
                break;
            case Input.KEY_LEFT:
            case Input.KEY_Q:
                this.keyLeft = false;
                break;
            case Input.KEY_RIGHT:
            case Input.KEY_D:
                this.keyRight = false;
                break;
        }
    }

    @Override public void setInput(Input input) {}
    @Override public boolean isAcceptingInput() { return false; }
    @Override public void inputEnded() {}
    @Override public void inputStarted() {}
}
