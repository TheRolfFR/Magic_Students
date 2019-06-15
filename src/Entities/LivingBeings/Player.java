package Entities.LivingBeings;

import Entities.Projectiles.Fireball;
import Entities.Projectiles.MeleeAttack;
import Entities.LivingBeings.Monsters.Ranged.Ranged;
import HUD.AttackVisual;
import Main.MainClass;
import Main.TimeScale;
import Managers.AttackVisualsManager;
import Renderers.*;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;

import static Main.MainClass.MAX_FPS;

public class Player extends LivingBeing implements KeyListener, MouseListener, PlayerConstants {

    private static final String MELEE_ATTACK_IMG_PATH = "img/meleeAttack.png";
    private static final String SPELL_ATTACK_IMG_PATH = "img/spellAttack.png";
    private static final String DASH_EFFECT_IMG_PATH = "img/dashEffect.png";

    private boolean keyUp;
    private boolean keyDown;
    private boolean keyLeft;
    private boolean keyRight;
    private boolean keySpace;

    private double angleFaced;

    private PlayerMarkerRenderer playerMarkerRenderer;
    private EffectRenderer attackRenderer;

    private Vector2f mousePosition;
    private Vector2f attackDirection;

    private boolean isAttackRendered = false;

    private float timeLeftBeforeEnablingMovement = 0;
    private float timeLeftWhileDashing = 0;

    private float dashCooldown = 0;
    private float spellCooldown = 0;

    private AttackVisual dashEffectVisual;
    private AttackVisual meleeAttackVisual;
    private AttackVisual spellAttackVisual;

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
        this.mousePosition = new Vector2f(gc.getInput().getMouseX(), gc.getInput().getMouseY());
        this.setAngleFaced(gc.getInput().getMouseX(), gc.getInput().getMouseY());

        String prepath = "img/wizard/";

        int duration = 50;

        Color capeColor = new Color(0x0094ff);

        this.setTileSize(new Vector2f(96, 96));
        Vector2f attackTileSize =  new Vector2f(96,58);
        this.renderer = new LivingBeingRenderer(this, this.getTileSize(), capeColor);
        this.attackRenderer = new EffectRenderer(prepath + "animationAttackWhite.png",attackTileSize, Math.round(1000*PlayerConstants.ATTACK_DURATION/3));

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

        this.dashEffectVisual = new AttackVisual(DASH_EFFECT_IMG_PATH);
        this.meleeAttackVisual = new AttackVisual(MELEE_ATTACK_IMG_PATH, 10);
        this.spellAttackVisual = new AttackVisual(SPELL_ATTACK_IMG_PATH);

        AttackVisualsManager.addVisual(this.dashEffectVisual);
        AttackVisualsManager.addVisual(this.meleeAttackVisual);
        AttackVisualsManager.addVisual(this.spellAttackVisual);
    }

    @Override
    public void setShowDebugRect(boolean showDebugRect) {
        super.setShowDebugRect(showDebugRect);
    }

    public void setShowPlayerMarkerDebugRect(boolean showPlayerMarkerDebugRect) {
        this.playerMarkerRenderer.setShowDebugRect(showPlayerMarkerDebugRect);
    }

    private void setAngleFaced(int x, int y) {
        this.angleFaced = new Vector2f(x, y).sub(this.getCenter()).getTheta() + 90.0;
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
            this.checkCollision();
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
            this.dashEffectVisual.onCooldownUpdate(this.dashCooldown, PlayerConstants.DASH_COOLDOWN);
        }
        if (!this.isSpellReady()){
            this.spellCooldown = this.spellCooldown - TimeScale.getInGameTimeScale().getDeltaTime();
            this.spellAttackVisual.onCooldownUpdate(this.spellCooldown, PlayerConstants.SPELL_COOLDOWN);
        }
        if (!this.isAbleToMove()){
            this.timeLeftBeforeEnablingMovement = this.timeLeftBeforeEnablingMovement - TimeScale.getInGameTimeScale().getDeltaTime();
        }
        if (isDashing()){
            this.timeLeftWhileDashing = this.timeLeftWhileDashing - TimeScale.getInGameTimeScale().getDeltaTime();
        }
    }

    private void meleeAttack() {
        this.attackDirection = this.mousePosition.copy().sub(super.getCenter()).normalise();
        super.setSpeed(new Vector2f(0, 0));

        this.timeLeftWhileAttacking = PlayerConstants.ATTACK_DURATION;
        this.timeLeftBeforeEnablingMovement = PlayerConstants.STUN_AFTER_ATTACK_DURATION;

        Ranged.allyProjectiles.add(new MeleeAttack(super.getCenter().add(attackDirection.copy().scale(super.getRadius()))));

        super.renderer.setLastActivity("Attack");
        super.renderer.update(attackDirection);
        this.isAttackRendered = true;

        this.meleeAttackVisual.onCooldownStart();
    }

    private void startDash(){
        if(!super.getSpeed().equals(new Vector2f(0,0))){
            super.renderer.setLastActivity("Dash");
            this.timeLeftWhileDashing = PlayerConstants.DASH_DURATION;
            super.setSpeed(super.getSpeed().copy().normalise().scale(MAX_SPEED*2.5f));
            this.dashCooldown = PlayerConstants.DASH_COOLDOWN;

            this.dashEffectVisual.onCooldownStart();
        }
    }

    public boolean isDashing(){return this.timeLeftWhileDashing > 0;}

    private boolean isDashReady(){return dashCooldown <= 0;}

    private void shootFireball(){
        Vector2f fireballDirection = this.mousePosition.copy().sub(super.getCenter()).normalise();
        this.spellCooldown = PlayerConstants.SPELL_COOLDOWN;
        this.timeLeftBeforeEnablingMovement = PlayerConstants.STUN_AFTER_ATTACK_DURATION;
        this.timeLeftWhileAttacking = PlayerConstants.ATTACK_DURATION;
        super.setSpeed(new Vector2f(0,0));
        Ranged.allyProjectiles.add(new Fireball(super.getCenter().add(fireballDirection.copy().scale(super.getRadius()+Fireball.getFireballRadius())), fireballDirection)); //décalage car bord haut gauche
        super.renderer.setLastActivity("Cast");
        super.renderer.update(fireballDirection);
        this.spellAttackVisual.onCooldownStart();
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
            Vector2f addVector = new Vector2f (this.attackDirection.getX()*(this.attackRenderer.getTileSize().getX()/2+super.getRadius()), this.attackDirection.getY()*(this.attackRenderer.getTileSize().getY()/2+super.getRadius()));
            Vector2f attackPosition = this.getCenter().add(addVector);
            this.attackRenderer.render(g, (int) attackPosition.getX(), (int) attackPosition.getY(), (float) attackDirection.getTheta() - 90);
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
        this.mousePosition = new Vector2f(newx, newy);
    }

    @Override
    public void mouseDragged(int oldx, int oldy, int newx, int newy) {

    }
}
