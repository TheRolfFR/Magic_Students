package Entities.LivingBeings;

import Entities.Projectiles.MeleeAttack;
import Entities.Projectiles.Snowball;
import Entities.LivingBeings.monsters.Ranged.Ranged;
import Main.MainClass;
import Main.TimeScale;
import Renderer.LivingBeingRenderer;
import Renderer.PlayerMarkerRenderer;
import Renderer.SpriteView;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;

import static Main.MainClass.MAX_FPS;

public class Player extends LivingBeing implements KeyListener, MouseListener{

    private boolean keyUp;
    private boolean keyDown;
    private boolean keyLeft;
    private boolean keyRight;
    private boolean keySpace;

    private double angleFaced;

    private PlayerMarkerRenderer playerMarkerRenderer;

    private int framesLeftBeforeMeleeAttack=0;
    private int framesLeftBeforeRangedAttack=0;
    private int framesLeftAfterDash=0;

    private int dashCD = 0;
    private int spellCD = 0;

    private Vector2f meleeAttackDirection = new Vector2f(0,0);
    private Vector2f rangedAttackDirection = new Vector2f(0,0);



    /**
     * Single contructor
     *
     * @param gc game container
     * @param x initial x position of the player
     * @param y initial y position of the player
     */
    public Player(GameContainer gc, float x, float y) {
        super(x, y,450 / MAX_FPS, 135 / MAX_FPS, 100, 5, (int) (0.4*45));

        this.keyUp = false;
        this.keyDown = false;
        this.keyLeft = false;
        this.keyRight = false;
        this.keySpace = false;

        gc.getInput().addKeyListener(this);
        gc.getInput().addMouseListener(this);

        String prepath = "img/wizard/";
        int duration = 50;

        Color capeColor = new Color(0x0094ff);

        this.setTileSize(new Vector2f(96, 96));
        this.renderer = new LivingBeingRenderer(this, this.getTileSize(), capeColor);
        this.renderer.setTopIdleView(new SpriteView(prepath + "topIdle.png", this.getTileSize(), duration, Color.red));
        this.renderer.setBottomIdleView(new SpriteView(prepath + "bottomIdle.png", this.getTileSize(), duration, Color.red));
        this.renderer.setLeftIdleView(new SpriteView(prepath + "leftIdle.png", this.getTileSize(), duration, Color.red));
        this.renderer.setRightIdleView(new SpriteView(prepath + "rightIdle.png", this.getTileSize(), duration, Color.red));

        this.renderer.setTopView(new SpriteView(prepath + "top.png", this.getTileSize(), duration, Color.red));
        this.renderer.setBottomView(new SpriteView(prepath + "bottom.png", this.getTileSize(), duration, Color.red));
        this.renderer.setLeftView(new SpriteView(prepath + "left.png", this.getTileSize(), duration, Color.red));
        this.renderer.setRightView(new SpriteView(prepath + "right.png", this.getTileSize(), duration, Color.red));

        this.playerMarkerRenderer = new PlayerMarkerRenderer(this, 2);

        this.setAngleFaced(gc.getInput().getMouseX(), gc.getInput().getMouseY());
    }

    @Override
    public void setShowDebugRect(boolean showDebugRect) {
        super.setShowDebugRect(showDebugRect);
    }

    public void setShowPlayerMarkerDebugRect(boolean showPlayerMarkerDebugRect) {
        this.playerMarkerRenderer.setShowDebugRect(showPlayerMarkerDebugRect);
    }

    private void setAngleFaced(int x, int y) {
        this.angleFaced = new Vector2f(x, y).sub(this.getPosition()).getTheta() + 90.0;
    }

    /**
     * In game calculations
     */
    public void update() {
        if(willDoSomething()){
            this.updateSpeed(this.getSpeed().negate().scale(0.2f));
        }
        else if(this.keySpace && isDashReady()){
            if(!isDashing()){
                startDash(MainClass.getInput().getMouseX(), MainClass.getInput().getMouseY());
            }
            else{
                this.framesLeftAfterDash-=1;
            }
        }
        else if(isDashing()){
            this.framesLeftAfterDash-=1;
        }
        else if(isCasting()){
            if(isSpellReady()){
                doRangedAttack();
            }
            else{
                this.framesLeftBeforeRangedAttack-=1;
            }
        }
        else if(isAttacking()){
            if(isAttackReady()){
                doMeleeAttack();
            }
            else{
                this.framesLeftBeforeMeleeAttack -= 1;
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

        if(!isDashReady() && !isDashing()){
            dashCD-=1;
        }
        if(!isCastingUp() && !isCasting()){
            spellCD-=1;
        }
    }



    private void startMeleeAttack(int mouseX, int mouseY){
        this.meleeAttackDirection = new Vector2f(mouseX,mouseY).sub(this.getCenter()).normalise().scale(this.getRadius()).add(this.getCenter());
        this.framesLeftBeforeMeleeAttack=5;
    }

    private Boolean isAttacking(){return !this.meleeAttackDirection.equals(new Vector2f(0,0));}

    private Boolean isAttackReady(){return this.framesLeftBeforeMeleeAttack==0;}

    private Boolean willDoSomething(){return (isAttacking() || isCasting()) && !this.getSpeed().equals(new Vector2f(0,0));}

    /**
     * do a melee attack
     */
    private void doMeleeAttack(){
        Ranged.allyProjectiles.add(new MeleeAttack(this.getCenter().add(this.getCenter().sub(this.meleeAttackDirection).normalise().scale(-this.getRadius())).add(new Vector2f(-MeleeAttack.getMeleeRadius(), -MeleeAttack.getMeleeRadius())), this.meleeAttackDirection));
        this.meleeAttackDirection.set(0,0);
    }

    /**
     * Do a ranged attack
     */
    private void doRangedAttack() {
        Ranged.allyProjectiles.add(new Snowball(this.rangedAttackDirection.copy().normalise().scale(this.getRadius()).add(new Vector2f(this.getCenter().x - Snowball.getSnowballRadius(), this.getCenter().y - Snowball.getSnowballRadius())), this.rangedAttackDirection)); //décalage car bord haut gauche
        this.rangedAttackDirection.set(0,0);
    }

    private void startDash(int mouseX, int mouseY){
        framesLeftAfterDash = 23;
        this.setSpeed(new Vector2f(mouseX,mouseY).sub(this.getCenter()).normalise().scale(MAX_SPEED*1.42f));
        dashCD = 15;
    }

    public boolean isDashing(){return this.framesLeftAfterDash!=0;}

    private boolean isDashReady(){return dashCD == 0;}

    private void startRangedAttack(){
        this.rangedAttackDirection = new Vector2f( Math.round(MainClass.getInput().getMouseX()), Math.round(MainClass.getInput().getMouseY() )).sub(this.getCenter());
        spellCD = 30;
        framesLeftBeforeRangedAttack = 25;
    }
    private boolean isSpellReady(){return this.framesLeftBeforeRangedAttack == 0;}

    private boolean isCasting(){return !this.rangedAttackDirection.equals(new Vector2f(0,0));}

    private boolean isCastingUp(){return this.spellCD==0;}

    @Override
    public void takeDamage(int damage) {
        if(!isDashing()){
            super.takeDamage(damage);
            //this.currentHealthPoints = Math.max(0, this.getCurrentHealthPoints() - round(damage / this.getArmorPoints()));
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

        this.playerMarkerRenderer.Render(g, angleFaced);

        super.render(g, facedDirection);
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
            case Input.KEY_SPACE:
                this.keySpace=true;
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
            case Input.KEY_SPACE:
                this.keySpace = false;
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
        if (TimeScale.getInGameTimeScale().getTimeScale() != 0f && !isCasting() && !isDashing() && isCastingUp()) {
            startRangedAttack();
        }
    }

    @Override
    public void mouseClicked(int button, int x, int y, int clickCount) {
    }

    @Override
    public void mousePressed(int button, int x, int y) {
        if (TimeScale.getInGameTimeScale().getTimeScale() != 0f && !isAttacking() && !isDashing()) {
                startMeleeAttack(x,y);
        }
    }

    @Override
    public void mouseReleased(int button, int x, int y) {
    }

    @Override
    public void mouseMoved(int oldx, int oldy, int newx, int newy) {
        this.setAngleFaced(newx, newy);
    }

    @Override
    public void mouseDragged(int oldx, int oldy, int newx, int newy) {

    }
}
