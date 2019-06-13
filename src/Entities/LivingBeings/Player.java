package Entities.LivingBeings;

import Entities.Projectiles.Fireball;
import Entities.Projectiles.MeleeAttack;
import Entities.LivingBeings.Monsters.Ranged.Ranged;
import Main.MainClass;
import Renderers.LivingBeingRenderer;
import Renderers.PlayerMarkerRenderer;
import Renderers.SpriteView;
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

    private int framesLeftBeforeEnablingMovement = 0;
    private int framesLeftWhileDashing=0;

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
        super(x, y,450 / MAX_FPS, 135 / MAX_FPS, 100, 1, (int) (0.4*45));

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

        String visions[] = {"top", "left", "right", "bottom"};
        String activities[] = {"Move", "Idle"};

        String fileName;
        for(String vision : visions) {
            for(String activity : activities) {
                // Determine filename
                fileName = vision;
                if(!activity.equals("Move"))
                    fileName += activity;

                // setting the view
                this.renderer.addView(vision + activity, new SpriteView(prepath + fileName + ".png", this.getTileSize(), duration, Color.red));
            }
        }

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
        updateCooldown();
        if (!isDashing()){
            if (this.keySpace && isDashReady()){
                startDash();
            }
            else {
                if (isAbleToMove()){
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
                    else{
                        this.updateSpeed(this.getSpeed().negate().scale(0.2f));
                    }
                }
            }
        }
        this.move();
    }

    private boolean isAbleToMove(){
        return framesLeftBeforeEnablingMovement == 0;
    }

    private void updateCooldown() {
        if (!isDashReady()){
            dashCD = dashCD - 1;
        }
        if (!isCastingUp()){
            spellCD = spellCD - 1;
        }
        if (!isAbleToMove()){
            framesLeftBeforeEnablingMovement = framesLeftBeforeEnablingMovement - 1;
        }
        if (isDashing()){
            framesLeftWhileDashing = framesLeftWhileDashing - 1;
        }
    }


    private void startMeleeAttack(int mouseX, int mouseY){
        this.meleeAttackDirection = new Vector2f(mouseX,mouseY).sub(this.getCenter()).normalise().scale(this.getRadius()).add(this.getCenter());
        this.framesLeftBeforeEnablingMovement = 6;
        this.setSpeed(new Vector2f(0,0));
        doMeleeAttack();
    }

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
        Ranged.allyProjectiles.add(new Fireball(this.rangedAttackDirection.copy().normalise().scale(this.getRadius()).add(new Vector2f(this.getCenter().x - Fireball.getFireballRadius(), this.getCenter().y - Fireball.getFireballRadius())), this.rangedAttackDirection.copy())); //décalage car bord haut gauche
        this.rangedAttackDirection.set(0,0);
    }

    private void startDash(){
        if(this.getSpeed()!=null){
            framesLeftWhileDashing = 12;
            this.setSpeed(this.getSpeed().copy().normalise().scale(MAX_SPEED*2.5f));
            dashCD = 18;
        }
    }

    public boolean isDashing(){return this.framesLeftWhileDashing != 0;}

    private boolean isDashReady(){return dashCD == 0;}

    private void startRangedAttack(){
        this.rangedAttackDirection = new Vector2f(MainClass.getInput().getMouseX(), MainClass.getInput().getMouseY()).sub(this.getCenter());
        spellCD = 30;
        framesLeftBeforeEnablingMovement = 6;
        this.setSpeed(new Vector2f(0,0));
        doRangedAttack();
    }

    private boolean isCastingUp(){return this.spellCD == 0;}

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
    public void mouseWheelMoved(int change) {}

    @Override
    public void mouseClicked(int button, int x, int y, int clickCount) {}

    @Override
    public void mousePressed(int button, int x, int y) {
        if (!MainClass.isGamePaused() && !isDashing()) {
            switch (button) {
                case 0:
                    startMeleeAttack(x, y);
                    break;
                case 1:
                    startRangedAttack();
                    break;
            }
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
