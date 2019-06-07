package Entities;

import Main.MainClass;
import Renderer.LivingBeingRenderer;
import Renderer.SpriteView;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;

import static Main.MainClass.MAX_FPS;
import static Main.MainClass.getInGameTimeScale;

public class Player extends LivingBeing implements KeyListener, MouseListener{

    private boolean keyUp;
    private boolean keyDown;
    private boolean keyLeft;
    private boolean keyRight;

    private int framesLeftBeforeAttack=0;
    private Vector2f attackDirection = new Vector2f(0,0);

    /**
     * Single contructor
     *
     * @param gc game container
     * @param x initial x position of the player
     * @param y initial y position of the player
     */
    public Player(GameContainer gc, float x, float y) {
        super(x, y,450 / MAX_FPS, 135 / MAX_FPS, 100, 5, 50);

        this.keyUp = false;
        this.keyDown = false;
        this.keyLeft = false;
        this.keyRight = false;

        gc.getInput().addKeyListener(this);
        gc.getInput().addMouseListener(this);

        String prepath = "img/wizard/";
        int duration = 50;

        Color capeColor = new Color(0x0094ff);

        this.tileSize = new Vector2f(96, 96);
        this.renderer = new LivingBeingRenderer(this, this.tileSize, capeColor);
        this.renderer.setTopIdleView(new SpriteView(prepath + "topIdle.png", this.tileSize, duration, Color.red));
        this.renderer.setBottomIdleView(new SpriteView(prepath + "bottomIdle.png", this.tileSize, duration, Color.red));
        this.renderer.setLeftIdleView(new SpriteView(prepath + "leftIdle.png", this.tileSize, duration, Color.red));
        this.renderer.setRightIdleView(new SpriteView(prepath + "rightIdle.png", this.tileSize, duration, Color.red));

        this.renderer.setTopView(new SpriteView(prepath + "top.png", this.tileSize, duration, Color.red));
        this.renderer.setBottomView(new SpriteView(prepath + "bottom.png", this.tileSize, duration, Color.red));
        this.renderer.setLeftView(new SpriteView(prepath + "left.png", this.tileSize, duration, Color.red));
        this.renderer.setRightView(new SpriteView(prepath + "right.png", this.tileSize, duration, Color.red));
    }

    /**
     * Do an attack
     */
    private void doAttack() {
        Vector2f direction = new Vector2f( Math.round(MainClass.getInput().getMouseX()), Math.round(MainClass.getInput().getMouseY() )).sub(this.getCenter());
        Ranged.allyProjectiles.add(new Snowball(direction.copy().normalise().scale(this.getRadius()).add(new Vector2f(this.getCenter().x - Snowball.getSnowballRadius(), this.getCenter().y - Snowball.getSnowballRadius())), direction)); //décalage car bord haut gauche
    }

    /**
     * In game calculations
     */
    public void update() {
        if(isAttacking()){
            if(isAttackReady()){
                doMeleeAttack();
            }
            else{
                prepareAttack();
            }
        }
        else if (this.keyUp || this.keyDown || this.keyLeft || this.keyRight) {
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
        else{
            this.updateSpeed(this.getSpeed().negate().scale(0.2f));
        }
        this.move();


    }

    private Boolean isAttacking(){return !this.attackDirection.equals(new Vector2f(0,0));}
    private Boolean isAttackReady(){return this.framesLeftBeforeAttack==0;}

    private void prepareAttack(){this.framesLeftBeforeAttack -= 1;}

    private void doMeleeAttack(){
        Ranged.allyProjectiles.add(new MeleeAttack(this.getCenter().add(this.getCenter().sub(this.attackDirection).normalise().scale(-this.getRadius())).add(new Vector2f(-MeleeAttack.getMeleeRadius(), -MeleeAttack.getMeleeRadius())), this.attackDirection));
        this.attackDirection.set(0,0);
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

        for(Projectile p : Ranged.allyProjectiles) {
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
    public void mouseWheelMoved(int change) {doAttack();

    }

    @Override
    public void mouseClicked(int button, int x, int y, int clickCount) {
    }

    @Override
    public void mousePressed(int button, int x, int y) {
        if (getInGameTimeScale().getTimeScale() != 0f) {
            //this.doAttack();
            if(!isAttacking()){
                this.attackDirection = new Vector2f(x,y).sub(this.getCenter()).normalise().scale(this.getRadius()).add(this.getCenter());
                this.speed.set(0,0);
                this.framesLeftBeforeAttack=60;
            }
        }
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
