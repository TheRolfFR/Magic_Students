package Entities;

import Entities.Attacks.MeleeAttack;
import Entities.Attacks.RangedAttack;

import Main.MainClass;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;

import java.util.ArrayList;

public class Player extends LivingBeing implements MeleeAttack, RangedAttack, KeyListener, MouseListener{

    protected int width;
    protected int height;

    private boolean keyUp;
    private boolean keyDown;
    private boolean keyLeft;
    private boolean keyRight;

    public static ArrayList<Projectile> playerProjectiles;

    @Override
    public int getWidth() { return this.width; }
    @Override
    public int getHeight() { return this.height; }

    /**
     * Single contructor
     *
     * @param gc game container
     * @param x initial x position of the player
     * @param y initial y position of the player
     * @param width hitbox width of the player
     * @param height hitbox height of the player
     * @param maxSpeed max speed of the player
     * @param accelerationRate max acceleration of the player
     */
    public Player(GameContainer gc, float x, float y, int width, int height, float maxSpeed, float accelerationRate, int radius) {
        super(x, y, maxSpeed, accelerationRate, 100, 10, radius);
        this.width = width;
        this.height = height;

        this.keyUp = false;
        this.keyDown = false;
        this.keyLeft = false;
        this.keyRight = false;

        this.playerProjectiles = new ArrayList<>();
        gc.getInput().addKeyListener(this);
        gc.getInput().addMouseListener(this);
    }

    /**
     * Do an attack
     */
    private void doAttack() {
        Vector2f direction = new Vector2f( MainClass.getInput().getMouseX(), MainClass.getInput().getMouseY() ).sub( this.getPosition() );
        this.playerProjectiles.add(new Snowball(this.getPosition(), direction));
    }

    /**
     * In game calculations
     */
    public void update(int i) {
        if (this.keyUp || this.keyDown || this.keyLeft || this.keyRight) {
            if (this.keyUp) {
                this.updateSpeed(new Vector2f(0, -1).scale(this.getAccelerationRate()), i);
            }
            if (this.keyDown) {
                this.updateSpeed(new Vector2f(0, 1).scale(this.getAccelerationRate()), i);
            }
            if (this.keyLeft) {
                this.updateSpeed(new Vector2f(-1, 0).scale(this.getAccelerationRate()), i);
            }
            if (this.keyRight) {
                this.updateSpeed(new Vector2f(1, 0).scale(this.getAccelerationRate()), i);
            }
        }
        else {
            this.updateSpeed(this.getSpeed().negate().scale(0.2f), i);
        }
        this.move();

        Projectile p;
        for (int j = 0; j < playerProjectiles.size(); j++) {
            p = playerProjectiles.get(j);
            p.update(i);

            if (p.isFadeOut()) {
                playerProjectiles.remove(j);
                j--;
            }
        }
    }

    public void checkCollidesProjectile(LivingBeing opponent){
        for(int i=0; i<this.playerProjectiles.size(); i++){
            if(this.playerProjectiles.get(i).collides(opponent)){
                this.playerProjectiles.get(i).collidingAction(opponent);
                this.playerProjectiles.remove(this.playerProjectiles.get(i));
            }
        }
    }

    /**
     * In game rendering
     * @param g the graphics to draw on
     */
    public void render(Graphics g) {
        Vector2f facedDirection = new Vector2f(0,0);
        if(this.keyDown) {
            facedDirection.y = 1;
        } else if(this.keyUp) {
            facedDirection.y = -1;
        }
        if(this.keyRight) {
            facedDirection.x = 1;
        } else if(this.keyLeft) {
            facedDirection.x = -1;
        }

        super.render(g, facedDirection);

        for(Projectile p : playerProjectiles) {
            p.render(g);
        }
    }

    /**
     * KeyListener interface key down implementation
     * @param key integer value of the key
     * @param c char associated to the int value
     */
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
        }
    }


    /**
     * KeyListener interface key up implementation
     * @param key integer value of the key
     * @param c char associated to the int value
     */
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

    /**
     * key listener interface implementation (empty)
     * @param input the input
     */
    @Override public void setInput(Input input) {}

    /**
     * key listener interface implementation (empty)
     * @return false
     */
    @Override public boolean isAcceptingInput() { return true; }

    /**
     * key listener interface implementation (empty)
     */
    @Override public void inputEnded() {}

    /**
     * key listener interface implementation (empty)
     */
    @Override public void inputStarted() {}


    @Override
    public void mouseWheelMoved(int change) {

    }

    @Override
    public void mouseClicked(int button, int x, int y, int clickCount) {
    }

    @Override
    public void mousePressed(int button, int x, int y) {
        this.doAttack();
    }

    @Override
    public void mouseReleased(int button, int x, int y) {
    }

    @Override
    public void mouseMoved(int oldx, int oldy, int newx, int newy) {

    }

    @Override
    public void mouseDragged(int oldx, int oldy, int newx, int newy) {

    }
}
