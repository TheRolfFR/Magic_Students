package Entities.LivingBeings.Monsters.Ranged;

import Entities.LivingBeings.LivingBeing;
import Entities.Projectiles.Arrow;
import Main.MainClass;
import Main.TimeScale;
import Renderers.EffectRenderer;
import Renderers.LivingBeingRenderer;
import Renderers.SpriteView;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

import java.util.Random;

/**
 * Bowman is a ranged monster that shoot arrow and run away if the player is too close
 */
public class Bowman extends Ranged implements BowmanConstants{

    public static final Vector2f BOWMAN_TILESIZE = new Vector2f(96, 96);
    private float framesLeftBeforeAttack; //time while aiming
    private Vector2f attackDirection = new Vector2f(0, 0);
    float framesLeftWhileStuned = BowmanConstants.STUN_AFTER_ATTACK_DURATION;
    private float framesLeftWhileSpeedLocked = BowmanConstants.MOVEMENT_DURATION;
    private float shootCooldown = BowmanConstants.SHOOT_COOLDOWN;

    private EffectRenderer bowCharge;
    private EffectRenderer bowRelease;
    private Vector2f renderDirection = new Vector2f(0,0);

    /**
     * Constructor
     * @param x initial x-coordonate of the bowman
     * @param y initial y-coordonate of the bowman
     * @param hpCount hpcount of the bowman
     * @param armor amout of armor of the bowman
     * @param damage damage of the bowman
     * @param radius hitbowradius of the bowman
     */
    public Bowman(float x, float y, int hpCount, int armor, int damage, int radius) {
        super(x, y, (int) BOWMAN_TILESIZE.getX(), (int) BOWMAN_TILESIZE.getY(), hpCount, armor, damage, radius);

        this.renderer = new LivingBeingRenderer(this, BOWMAN_TILESIZE);

        final String prepath = "img/bowman/";

        bowCharge = new EffectRenderer(prepath + "bowCharge.png", this.getTileSize(), Math.round(1000*BowmanConstants.ATTACK_LOADING_DURATION/6));
        bowCharge.noLoop();

        bowRelease = new EffectRenderer(prepath + "bowRelease.png", this.getTileSize(), Math.round(1000*BowmanConstants.STUN_AFTER_ATTACK_DURATION/4));
        bowRelease.noLoop();

        final int duration = 1000/8;

        for (String vision : LivingBeingRenderer.ACCEPTED_VISION_DIRECTIONS) {
            this.renderer.addView(vision + "Move", new SpriteView(prepath + vision + ".png", BOWMAN_TILESIZE, duration));
            this.renderer.addView(vision + "Attack", new SpriteView(prepath + vision + "attack.png", BOWMAN_TILESIZE, 1000));
            this.renderer.addView(vision + "Idle", new SpriteView(prepath + "idle.png", BOWMAN_TILESIZE, 1000));
        }
    }

    /**
     * Constructor
     * @param x initial x-coordonate of the bowman
     * @param y initial y-coordonate of the bowman
     * @param tileSize the size of the image
     * @param hpCount hpcount of the bowman
     * @param armor amout of armor of the bowman
     * @param damage damage of the bowman
     * @param radius hitboxradius of the bowman
     */
    public Bowman(float x, float y, Vector2f tileSize, int hpCount, int armor, int damage, int radius) {
        super(x, y, (int) tileSize.getX(), (int) tileSize.getY(), hpCount, armor, damage, radius);

        this.renderer = new LivingBeingRenderer(this, tileSize);

        final String prepath = "img/bowman/";
        bowCharge = new EffectRenderer(prepath + "bowCharge.png", this.getTileSize(), Math.round(1000*BowmanConstants.ATTACK_LOADING_DURATION/6));
        bowCharge.noLoop();

        bowRelease = new EffectRenderer(prepath + "bowRelease.png", this.getTileSize(), Math.round(1000*BowmanConstants.STUN_AFTER_ATTACK_DURATION/8));
        bowRelease.noLoop();

        final int duration = 1000/8;

        for (String vision : LivingBeingRenderer.ACCEPTED_VISION_DIRECTIONS) {
            this.renderer.addView(vision + "Move", new SpriteView(prepath + vision + ".png", tileSize, duration));
            this.renderer.addView(vision + "Attack", new SpriteView(prepath + vision + "attack.png", tileSize, 1000));
            this.renderer.addView(vision + "Idle", new SpriteView(prepath + "idle.png", tileSize, 1000));
        }
    }

    /**
     * update the bowman according to his behavior
     * @param target the player
     */
    @Override
    public void update(LivingBeing target) {
        this.updateCountdown(); //update all timer relative to the bowman
        if (this.isAttacking()) { //if he's attacking
            if (this.isAttackReady()) { //if attack is ready
                this.attack(target); //shoot
            }
            else { //if attack isn't ready
                this.aim(target); //aim
            }
        }
        else { //if he is not attacking
            if (!this.isStun()) { //if he's not stun
                if (this.isShootReady()) { //if he's allowed to attack
                    this.startAttacking(target); //begin to attack
                }
                else { //if he's not allowed to attack
                    if (this.targetIsClose(target)) { //if the target is close
                        this.runAway(target); //run away from the target
                    }
                    else { //if the target isn't close
                        if (!this.isSpeedLocked()) { //if he hasn't decide to move yet
                            if (this.decideToMove()) { //if he decide to move now
                                this.chooseDirection(); //choose a direction
                            }
                            else { //if he doesn't decide to move
                                this.renderer.setLastActivity("Idle"); //do nothing
                                if (super.getSpeed().length() != 0) { //if he has speed
                                    super.updateSpeed(super.getSpeed().normalise().negate().scale(getAccelerationRate())); //slow down
                                }
                            }
                        }
                    }
                }
                super.move();
            }
        }
    }

    /**
     * Indicate if the target is close
     * @param target the player
     * @return true if the player is close, false otherwise
     */
    boolean targetIsClose(LivingBeing target) {
        return target.getCenter().distance(super.getCenter()) < BowmanConstants.RUN_AWAY_THRESHOLD;
    }

    /**
     * Run away from the player
     * @param target the player
     */
    void runAway(LivingBeing target) {
        this.renderer.setLastActivity("Move");
        super.updateSpeed(target.getCenter().sub(super.getCenter()).normalise().negate().scale(this.getAccelerationRate()));
        this.framesLeftWhileSpeedLocked = 0; //if the bowman was moving according to a choosed direction, override by the runaway
    }

    /**
     * update every timer relative to the bowman
     */
    void updateCountdown() {
        if (!this.isAttackReady()) {
            this.framesLeftBeforeAttack = this.framesLeftBeforeAttack - TimeScale.getInGameTimeScale().getDeltaTime();
        }
        if (!this.isShootReady()) {
            this.shootCooldown = this.shootCooldown - TimeScale.getInGameTimeScale().getDeltaTime();
        }
        if (this.isStun()) {
            this.framesLeftWhileStuned = this.framesLeftWhileStuned - TimeScale.getInGameTimeScale().getDeltaTime();
        }
        if (this.isSpeedLocked()) {
            this.framesLeftWhileSpeedLocked = this.framesLeftWhileSpeedLocked - TimeScale.getInGameTimeScale().getDeltaTime();
        }
    }

    /**
     * Indicate if the bowman is allowed to shoot again
     * @return true if he is allowed, false otherwise
     */
    boolean isShootReady() {
        return this.shootCooldown <= 0;
    }

    /**
     * Start aiming in order to shoot at the target
     * @param target the player
     */
    void startAttacking(LivingBeing target) {
        this.attackDirection.set(target.getCenter().sub(this.getCenter()).normalise());
        this.setSpeed(new Vector2f(0, 0));
        this.unlockSpeed();
        this.framesLeftBeforeAttack = BowmanConstants.ATTACK_LOADING_DURATION;
        this.renderer.setLastActivity("Attack");
        this.renderer.update(this.correctedAttackDirectionForRenderer());
    }

    /**
     * choose a random direction
     */
    void chooseDirection() {
        Random random = new Random();
        this.updateSpeed(new Vector2f(0.5f - random.nextFloat(), 0.5f - random.nextFloat()).normalise().scale(this.getAccelerationRate()));
        this.framesLeftWhileSpeedLocked = BowmanConstants.MOVEMENT_DURATION;
        this.renderer.setLastActivity("Move");
    }

    /**
     * Indicate if the bowman want to move
     * @return true if he decide to move, false otherwise
     */
    boolean decideToMove() {
        Random random = new Random();
        return (random.nextFloat()%1 < 1f/(MainClass.getNumberOfFramePerSecond()*BowmanConstants.AVERAGE_SECONDS_BEFORE_MOVEMENT));
    }

    private void unlockSpeed(){this.framesLeftWhileSpeedLocked=0;}

    /**
     * Indicate if the bowman is doing a decided move
     * @return true if the bowman is moving due to a decision, false otherwise
     */
    boolean isSpeedLocked() {
        return this.framesLeftWhileSpeedLocked > 0;
    }

    /**
     * Indicate the bowman is stun
     * @return true if he's stun, false otherwise
     */
    boolean isStun() {
        return this.framesLeftWhileStuned > 0;
    }

    /**
     * Indicate if the bowman as aimed for long enough to allow himself to shoot
     * @return true if he can shoot, false otherwise
     */
    boolean isAttackReady() {
        return (this.framesLeftBeforeAttack <= 0);
    }

    /**
     * Indicate if the bowman is already attakcing
     * @return true if he's attacking, false otherwise
     */
    boolean isAttacking() {
        return (!this.attackDirection.equals(new Vector2f(0, 0)));
    }

    /**
     * Update the direction of the attack to aim at the player
     * @param target the player
     */
    void aim(LivingBeing target) {
        this.attackDirection.set(target.getCenter().sub(super.getCenter()).normalise());
        this.renderer.update(this.correctedAttackDirectionForRenderer());
    }

    /**
     * Modify the direction of the attack in order to show the right animation on screen
     * @return the modified direction
     */
    private Vector2f correctedAttackDirectionForRenderer(){
        if(this.attackDirection.getX()!=0){
            return new Vector2f(this.attackDirection.getX(), 0);
        }
        else{
            return new Vector2f(0,this.attackDirection.getY());
        }
    }

    /**
     * Shoot an arrow at the target
     * @param target the player
     */
    protected void attack(LivingBeing target) {

        this.attackDirection.set(target.getCenter().sub(super.getCenter()).normalise());
        this.renderDirection.set(this.attackDirection);
        Ranged.enemyProjectiles.add(new Arrow(super.getCenter().add(this.attackDirection.copy().scale(super.getRadius())), this.attackDirection.copy(), this.getDamage()));
        this.attackDirection.set(0, 0);
        this.shootCooldown = BowmanConstants.SHOOT_COOLDOWN;
        this.stun();
    }

    /**
     * Stun the bowman to prevent him from doing another action for a set amount of time
     */
    void stun() {
        this.framesLeftWhileStuned = BowmanConstants.STUN_AFTER_ATTACK_DURATION;
    }

    /**
     * In game rendering : character rendering, charge or release rendering
     * @param g the graphics to draw on
     * @see EffectRenderer#render(Graphics, int, int, float)
     */
    public void render(Graphics g) {
        super.render(g);
            if(isAttacking()){
                this.bowCharge.render(g, (int) super.getCenter().getX(), (int) super.getCenter().getY(), (float) this.attackDirection.getTheta());
            }
            else if(isStun()){
                this.bowRelease.render(g, (int) super.getCenter().getX(), (int) super.getCenter().getY(), (float) this.renderDirection.getTheta());
        }

    }

    /**
     * Getter for the maximum speed
     * @return the norm of the maximum speed of a bowman
     */
    @Override
    public float getMaxSpeed() {
        return MAX_SPEED;
    }

    /**
     * Getter for the acceleration
     * @return the norm of the acceleration of a bowman
     */
    @Override
    public float getAccelerationRate() {
        return ACCELERATION_RATE;
    }
}
