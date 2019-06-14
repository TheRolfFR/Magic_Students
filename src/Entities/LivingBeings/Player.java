package Entities.LivingBeings;

import Entities.Projectiles.Fireball;
import Entities.Projectiles.MeleeAttack;
import Entities.LivingBeings.Monsters.Ranged.Ranged;
import Main.MainClass;
import Main.TimeScale;
import Renderers.*;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;

import static Main.MainClass.MAX_FPS;

public class Player extends LivingBeing implements KeyListener, MouseListener, PlayerConstants{

    private boolean keyUp;
    private boolean keyDown;
    private boolean keyLeft;
    private boolean keyRight;
    private boolean keySpace;

    private double angleFaced;

    private PlayerMarkerRenderer playerMarkerRenderer;
    private GraphicRenderer attackRenderer;

    private boolean isAttackRendered = false;

    private float timeLeftBeforeEnablingMovement = 0;
    private float timeLeftWhileDashing = 0;

    private float dashCooldown = 0;
    private float spellCooldown = 0;

    private float timeLeftWhileAttacking = 0;


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
        this.attackRenderer = new GraphicRenderer(this, prepath + "animationAttack.png",this.getTileSize(), Math.round(1000*4/MainClass.getNumberOfFramePerSecond()));

        String[] activities = {"Move", "Idle", "Dash", "Attack", "Cast"};

        String fileName;
        for(String vision : LivingBeingRenderer.ACCEPTED_VISION_DIRECTIONS) {
            for(String activity : activities) {
                // Determine filename
                fileName = vision;
                if (!activity.equals("Move"))
                    if (activity.equals("Cast")) {
                        fileName = activity;
                    }
                    else {
                        fileName += activity;
                    }

                // setting the view
                this.renderer.addView(vision + activity, new SpriteView(prepath + fileName + ".png", this.getTileSize(), duration, Color.red));
            }
        }

        this.playerMarkerRenderer = new PlayerMarkerRenderer(this, 2);
        this.setTileSize(new Vector2f(96, 96));

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
        this.updateCountdown();
        if (!this.isDashing()){
            if (this.keySpace && this.isDashReady()){
                this.startDash();
            }
            else {
                if (this.isAbleToMove()){
                    if (this.keyUp || this.keyDown || this.keyLeft || this.keyRight) {
                        super.renderer.setLastActivity("Move");
                        if (this.keyUp) {
                            super.updateSpeed(new Vector2f(0, -1).scale(super.getAccelerationRate()));
                        }
                        if (this.keyDown) {
                            super.updateSpeed(new Vector2f(0, 1).scale(super.getAccelerationRate()));
                        }
                        if (this.keyLeft) {
                            super.updateSpeed(new Vector2f(-1, 0).scale(super.getAccelerationRate()));
                        }
                        if (this.keyRight) {
                            super.updateSpeed(new Vector2f(1, 0).scale(super.getAccelerationRate()));
                        }
                    }
                    else {
                        if (super.getSpeed().equals(new Vector2f(0, 0))) {
                            super.renderer.setLastActivity("Idle");
                        } else {
                            super.updateSpeed(super.getSpeed().negate().scale(0.2f));
                        }
                    }
                }
            }
        }
        super.move();
    }

    private boolean isAbleToMove(){
        return this.timeLeftBeforeEnablingMovement <= 0;
    }

    private void updateCountdown() {
        if (this.isAttacking()){
            this.timeLeftWhileAttacking = this.timeLeftWhileAttacking - TimeScale.getInGameTimeScale().getDeltaTime();
        }
        if (!this.isDashReady()){
            this.dashCooldown = this.dashCooldown - TimeScale.getInGameTimeScale().getDeltaTime();
        }
        if (!this.isSpellReady()){
            this.spellCooldown = this.spellCooldown - TimeScale.getInGameTimeScale().getDeltaTime();
        }
        if (!this.isAbleToMove()){
            this.timeLeftBeforeEnablingMovement = this.timeLeftBeforeEnablingMovement - TimeScale.getInGameTimeScale().getDeltaTime();
        }
        if (isDashing()){
            this.timeLeftWhileDashing = this.timeLeftWhileDashing - TimeScale.getInGameTimeScale().getDeltaTime();
        }
    }

    private void meleeAttack() {
        Vector2f attackDirection = new Vector2f(MainClass.getInput().getMouseX(),MainClass.getInput().getMouseY()).sub(super.getPosition()).normalise();
        super.setSpeed(new Vector2f(0, 0));
        this.timeLeftWhileAttacking = PlayerConstants.ATTACK_DURATION;
        this.timeLeftBeforeEnablingMovement = PlayerConstants.STUN_AFTER_ATTACK_DURATION;
        Ranged.allyProjectiles.add(new MeleeAttack(super.getPosition().add(attackDirection.scale(super.getRadius()))));
        super.renderer.setLastActivity("Attack");
        super.renderer.update(attackDirection);
        this.isAttackRendered = true;
    }

    private void startDash(){
        if(!super.getSpeed().equals(new Vector2f(0,0))){
            super.renderer.setLastActivity("Dash");
            this.timeLeftWhileDashing = PlayerConstants.DASH_DURATION;
            super.setSpeed(super.getSpeed().copy().normalise().scale(MAX_SPEED*2.5f));
            this.dashCooldown = PlayerConstants.DASH_COOLDOWN;
        }
    }

    public boolean isDashing(){return this.timeLeftWhileDashing > 0;}

    private boolean isDashReady(){return dashCooldown <= 0;}

    private void shootFireball(){
        Vector2f fireballDirection = new Vector2f(MainClass.getInput().getMouseX(), MainClass.getInput().getMouseY()).sub(super.getPosition()).normalise();
        this.spellCooldown = PlayerConstants.SPELL_COOLDOWN;
        this.timeLeftBeforeEnablingMovement = PlayerConstants.STUN_AFTER_ATTACK_DURATION;
        this.timeLeftWhileAttacking = PlayerConstants.ATTACK_DURATION;
        super.setSpeed(new Vector2f(0,0));
        Ranged.allyProjectiles.add(new Fireball(super.getPosition().add(fireballDirection.copy().scale(super.getRadius()+Fireball.getFireballRadius())), fireballDirection)); //décalage car bord haut gauche
        super.renderer.setLastActivity("Cast");
        super.renderer.update(fireballDirection);
    }

    private boolean isSpellReady(){return this.spellCooldown <= 0;}

    private boolean isAttacking(){
        return timeLeftWhileAttacking > 0;
    }

    @Override
    public void takeDamage(int damage) {
        if(!this.isDashing()){
            super.takeDamage(damage);
            //this.currentHealthPoints = Math.max(0, this.getCurrentHealthPoints() - round(damage / this.getArmorPoints()));
        }
    }

    private void renderAttack(Graphics g){
        if(isAttacking() && this.isAttackRendered){
            this.attackRenderer.render(g, (int) this.getCenter().getX(), (int) this.getCenter().getY());
        }
        else {
            this.isAttackRendered=false;
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

        this.renderAttack(g);
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
                    if (!this.isAttacking()){
                        this.meleeAttack();
                    }

                    break;
                case 1:
                    if (!this.isAttacking()) {
                        this.shootFireball();
//                    if (isSpellReady()){
//                        shootFireball();
//                    }
                    }

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
