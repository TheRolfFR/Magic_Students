package Entities.LivingBeings;

import Entities.LivingBeings.Monsters.Ranged.Ranged;
import Entities.Projectiles.Fireball;
import Entities.Projectiles.MeleeAttack;
import HUD.AttackVisual;
import HUD.EndScreen;
import Main.GameStats;
import Main.MainClass;
import Main.TimeScale;
import Managers.AttackVisualsManager;
import Renderers.EffectRenderer;
import Renderers.LivingBeingRenderer;
import Renderers.PlayerMarkerRenderer;
import Renderers.SpriteView;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;

import static Main.MainClass.MAX_FPS;

/**
 * the player is a being that the user can control
 */
public class Player extends LivingBeing implements KeyListener, MouseListener, PlayerConstants {

    private static final String MELEE_ATTACK_IMG_PATH = "img/items/meleeAttack.png";
    private static final String SPELL_ATTACK_IMG_PATH = "img/items/spellAttack.png";
    private static final String DASH_EFFECT_IMG_PATH = "img/items/dashEffect.png";

    //boolean that indicate if a key is pressed
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

    private float timeLeftWhileDashing = 0;

    private float dashCooldown = 0;
    private float spellCooldown = 0;

    private AttackVisual dashEffectVisual;
    private AttackVisual meleeAttackVisual;
    private AttackVisual spellAttackVisual;

    private float timeLeftWhileAttacking = 0;
    private float maxSpeed = BASE_MAX_SPEED;
    private float accelerationNorm = BASE_ACCELERATION_NORM;


    /**
     * Single contructor
     *
     * @param gc game container
     * @param x initial x position of the player
     * @param y initial y position of the player
     */
    public Player(GameContainer gc, float x, float y) {
        super(x, y, 100, 1, (int) (0.4 * 45));

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
        Vector2f attackTileSize = new Vector2f(96, 58);
        this.renderer = new LivingBeingRenderer(this, this.getTileSize(), capeColor);
        this.attackRenderer = new EffectRenderer(prepath + "animationAttackWhite.png", attackTileSize, Math.round(1000 * PlayerConstants.ATTACK_DURATION / 3));

        String[] activities = {"Move", "Idle", "Dash", "Attack", "Cast"};

        String fileName;
        for (String vision : LivingBeingRenderer.ACCEPTED_VISION_DIRECTIONS) {
            for (String activity : activities) {
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

        this.addHealthListener(GameStats.getInstance());
        this.addHealthListener(EndScreen.getInstance());
    }

    /**
     * Setter for the angle that the player is facing
     * @param x the x-coordonate of the mouse
     * @param y the y-coordonate of the mouse
     */
    private void setAngleFaced(int x, int y) {
        this.angleFaced = new Vector2f(x, y).sub(this.getCenter()).getTheta() + 90.0;
    }

    /**
     * Increase the maximum speed and the acceleration of the player
     * @param buffAmount increase in max speed
     */
    public void buffSpeed(float buffAmount) {
        this.maxSpeed = this.maxSpeed + buffAmount;
        this.accelerationNorm = this.accelerationNorm + buffAmount * 135/450;
    }

    /**
     * Update the position and timer of the player
     */
    public void update() {
        this.updateCountdown(); //update every timer
        if (!this.isDashing()) { //if the player isn't dashing
            if (this.keySpace && this.isDashReady()) { //if the player want to dash and the dash is ready
                this.startDash(); //dash
            }
            else { //if the player isn't dashing
                if (!this.isAttacking()) { //if the player isn't attacking
                    if (this.keyUp || this.keyDown || this.keyLeft || this.keyRight) { //if a key is pressed
                        super.renderer.setLastActivity("Move"); //set animation to move
                        //update the speed base on what key is pressed
                        if (this.keyUp) {
                            super.updateSpeed(new Vector2f(0, -1).scale(this.getAccelerationRate()));
                        }
                        if (this.keyDown) {
                            super.updateSpeed(new Vector2f(0, 1).scale(this.getAccelerationRate()));
                        }
                        if (this.keyLeft) {
                            super.updateSpeed(new Vector2f(-1, 0).scale(this.getAccelerationRate()));
                        }
                        if (this.keyRight) {
                            super.updateSpeed(new Vector2f(1, 0).scale(this.getAccelerationRate()));
                        }
                    }
                    else { //if no key is pressed
                        if (super.getSpeed().equals(new Vector2f(0, 0))) { //if the player has no speed
                            super.renderer.setLastActivity("Idle"); //set animation to idle
                        } else { //if the player has speed
                            super.updateSpeed(super.getSpeed().negate().scale(0.2f)); //decrease the speed
                        }
                    }
                }
            }
            this.checkCollision();
        }

        this.setAngleFaced((int) mousePosition.getX(), (int) mousePosition.getY());
        super.move();
    }

    /**
     * update every timer relative to the player
     */
    private void updateCountdown() {
        if (this.isAttacking()) {
            this.timeLeftWhileAttacking = this.timeLeftWhileAttacking - TimeScale.getInGameTimeScale().getDeltaTime();
        }
        if (!this.isDashReady()) {
            this.dashCooldown = this.dashCooldown - TimeScale.getInGameTimeScale().getDeltaTime();
            this.dashEffectVisual.onCooldownUpdate(this.dashCooldown, PlayerConstants.DASH_COOLDOWN);
        }
        if (!this.isSpellReady()) {
            this.spellCooldown = this.spellCooldown - TimeScale.getInGameTimeScale().getDeltaTime();
            this.spellAttackVisual.onCooldownUpdate(this.spellCooldown, PlayerConstants.SPELL_COOLDOWN);
        }
        if (isDashing()) {
            this.timeLeftWhileDashing = this.timeLeftWhileDashing - TimeScale.getInGameTimeScale().getDeltaTime();
        }
    }

    /**
     * Allow the player to attack close to him in the direction of the mouse
     */
    private void meleeAttack() {
        this.attackDirection = this.mousePosition.copy().sub(super.getCenter()).normalise(); //compute the direction of the attack
        super.setSpeed(new Vector2f(0, 0)); //make the player stand still

        this.timeLeftWhileAttacking = PlayerConstants.ATTACK_DURATION; //avoiding the spam of attack

        Ranged.allyProjectiles.add(new MeleeAttack(super.getCenter().add(attackDirection.copy().scale(super.getRadius()*2f)))); //create a projectile that reprensents the melee attack

        super.renderer.setLastActivity("Attack"); //set animation to attack
        super.renderer.update(attackDirection);
        this.isAttackRendered = true;

        this.meleeAttackVisual.onCooldownStart();
    }

    /**
     * Initiate dashing
     */
    private void startDash() {
        if (!super.getSpeed().equals(new Vector2f(0, 0))) { //if the player is currently moving
            super.renderer.setLastActivity("Dash"); //set animation to dash
            this.timeLeftWhileDashing = PlayerConstants.DASH_DURATION; //setting the timer to the timer that the player will be dashing
            super.setSpeed(super.getSpeed().copy().normalise().scale(getMaxSpeed()*2.5f)); //setting the speed of the dash
            this.dashCooldown = PlayerConstants.DASH_COOLDOWN; //Setting a cooldown to avoid spam

            this.dashEffectVisual.onCooldownStart();
        }
    }

    /**
     * Indicate if the player is currently dashing
     * @return true if the player is dashing, false otherwise
     */
    public boolean isDashing() {
        return this.timeLeftWhileDashing > 0;
    }

    /**
     * Indicate if the player can use his dash again
     * @return true if he can, false otherwise
     */
    private boolean isDashReady() {
        return dashCooldown <= 0;
    }

    /**
     * Allow the player the shoot a fireball in the direction of the mouse
     */
    private void shootFireball() {
        Vector2f fireballDirection = this.mousePosition.copy().sub(super.getCenter()).normalise(); //Getting the mouse position
        this.spellCooldown = PlayerConstants.SPELL_COOLDOWN; //Setting the cooldown
        this.timeLeftWhileAttacking = PlayerConstants.ATTACK_DURATION; //Imobilize the player for a short period of time
        super.setSpeed(new Vector2f(0, 0)); //kill the speed of the player
        Ranged.allyProjectiles.add(new Fireball(super.getCenter().add(fireballDirection.copy().scale(super.getRadius()+Fireball.getFireballRadius())), fireballDirection)); //create the fireball
        super.renderer.setLastActivity("Cast"); //set animation to "cast"
        super.renderer.update(fireballDirection);
        this.spellAttackVisual.onCooldownStart();
    }

    /**
     * Indicate if the player can shoot a fireball again
     * @return true if he can, false otherwise
     */
    private boolean isSpellReady() {
        return this.spellCooldown <= 0;
    }

    /**
     * Indicate if the player is currently attacking
     * @return trus if he does, false otherwise
     */
    private boolean isAttacking() {
        return timeLeftWhileAttacking > 0;
    }

    /**
     * Getter for the maximum speed
     * @return the norm of the maximum speed
     */
    @Override
    public float getMaxSpeed() {
        return this.maxSpeed;
    }

    /**
     * Getter for the acceleration
     * @return the norm of the acceleration
     */
    @Override
    public float getAccelerationRate() {
        return this.accelerationNorm;
    }

    /**
     * Allow the player to take damage if he isn't dashing
     * @param damage damage value inflicted
     */
    @Override
    public void takeDamage(int damage) {
        if (!this.isDashing()) {
            super.takeDamage(damage);
        }
    }

    private void renderAttack(Graphics g) {
        if (isAttacking() && this.isAttackRendered) {
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
        Vector2f facedDirection = new Vector2f(0, 0);
        if (this.keyDown) {
            facedDirection.y = 1;
        } else if (this.keyUp) {
            facedDirection.y = -1;
        }
        if (this.keyRight) {
            facedDirection.x = 1;
        } else if (this.keyLeft) {
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

    /**
     * Mouse wheel listener
     * @param change amount of block changed
     */
    @Override
    public void mouseWheelMoved(int change) {}

    /**
     * Mouse listener on button button down
     * @param button the button of the mouse that was down
     * @param x the x-coordonate of the mouse
     * @param y the y-coordonate of the mouse
     * @param clickCount the number of clicks
     */
    @Override
    public void mouseClicked(int button, int x, int y, int clickCount) {}

    /**
     * Mouse listener on button pressed
     * @param button the button of the mouse that was pressed
     * @param x the x-coordonate of the mouse
     * @param y the y-coordonate of the mouse
     */
    @Override
    public void mousePressed(int button, int x, int y) {
        if (!MainClass.isGamePaused() && !isDashing()) { //if the game is running and the player isn't dashing
            switch (button) {
                case 0:
                    if (!this.isAttacking()) { //if the player isn't already attacking
                        this.meleeAttack();
                    }

                    break;
                case 1:
                    if (!this.isAttacking()) { //if the player isn't already attacking
                        if (isSpellReady()) { //if the fireball is ready
                            shootFireball();
                        }
                    }

                    break;
            }
        }
    }

    /**
     * Mouse listener on mouse button up
     * @param button the button of the mouse that became up
     * @param x x-coordonate of the last position of the mouse
     * @param y y-coordonate of the last position of the mouse
     */
    @Override
    public void mouseReleased(int button, int x, int y) {
    }

    /**
     * Mouse listener on mouse move
     * @param oldx x-coordonate of the last position of the mouse
     * @param oldy y-coordonate of the last position of the mouse
     * @param newx x-coordonate of the new position of the mouse
     * @param newy y-coordonate of the new position of the mouse
     */
    @Override
    public void mouseMoved(int oldx, int oldy, int newx, int newy) {
        this.mousePosition = new Vector2f(newx, newy);
    }

    /**
     * Mouse listener on mouse drag
     * @param oldx x-coordonate of the last position of the mouse
     * @param oldy y-coordonate of the last position of the mouse
     * @param newx x-coordonate of the new position of the mouse
     * @param newy y-coordonate of the new position of the mouse
     */
    @Override
    public void mouseDragged(int oldx, int oldy, int newx, int newy) {

    }
}
