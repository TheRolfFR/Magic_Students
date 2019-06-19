package Entities.LivingBeings.Monsters.Melee;

import Entities.LivingBeings.LivingBeing;
import Main.TimeScale;
import Renderers.EffectRenderer;
import Renderers.LivingBeingRenderer;
import Renderers.SpriteView;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

/**
 * A knight is a melee monster with a sword
 */
public class Knight extends Melee implements KnightConstants{

    private float timeLeftBeforeAttack = KnightConstants.ATTACK_LOADING_DURATION;
    private float timeLeftWhileStuned = KnightConstants.STUN_AFTER_ATTACK_DURATION;
    private Vector2f attackDirection = new Vector2f(0, 0);

    EffectRenderer attackRenderer;

    /**
     * Constructor
     * Constructor
     * @param x initial x-coordonate of the bowman
     * @param y initial y-coordonate of the bowman
     * @param hpCount hpcount of the bowman
     * @param armor amout of armor of the bowman
     * @param damage damage of the bowman
     * @param radius hitbox radius of the bowman
     */
    public Knight(float x, float y, int hpCount, int armor, int damage, int radius) {
        super(x, y, (int) KnightConstants.KNIGHT_TILESIZE.getX(), (int) KnightConstants.KNIGHT_TILESIZE.getY(), hpCount, armor, damage, radius);

        this.renderer = new LivingBeingRenderer(this, KnightConstants.KNIGHT_TILESIZE);

        final String prepath = "img/knight/";

        final int moveDuration = 1000/8;
        final int attackDuration = Math.round(1000*KnightConstants.ATTACK_LOADING_DURATION);

        Vector2f attackTileSize = new Vector2f(96, 58);
        this.attackRenderer = new EffectRenderer(prepath + "animationAttackWhite.png", attackTileSize, Math.round (KnightConstants.ATTACK_LOADING_DURATION*1000/10));

        String[] activities = {"Move", "Attack"};

        String fileName;
        int duration;
        for (String vision : LivingBeingRenderer.ACCEPTED_VISION_DIRECTIONS) {
            for (String activity : activities) {
                fileName = vision;
                if (!activity.equals("Move")) {
                    fileName += activity;
                    duration = attackDuration;
                }
                else{
                    duration = moveDuration;
                }
                this.renderer.addView(vision + activity, new SpriteView(prepath + fileName + ".png", KnightConstants.KNIGHT_TILESIZE, duration));
            }
        }
        this.renderer.addView("bottomIdle", new SpriteView(prepath + "bottomIdle.png", KnightConstants.KNIGHT_TILESIZE, Math.round (KnightConstants.STUN_AFTER_ATTACK_DURATION*1000)));
    }

    /**
     * Constructor
     * @param x initial x-coordonate of the bowman
     * @param y initial y-coordonate of the bowman
     * @param tileSize size of the image
     * @param hpCount hpcount of the bowman
     * @param armor amout of armor of the bowman
     * @param damage damage of the bowman
     * @param radius hitbox radius of the bowman
     */
    public Knight(float x, float y, Vector2f tileSize, int hpCount, int armor, int damage, int radius) {
        super(x, y, (int) tileSize.getX(), (int) tileSize.getY(), hpCount, armor, damage, radius);

        this.renderer = new LivingBeingRenderer(this, tileSize);
        this.renderer.setLastActivity("Idle");

        final String prepath = "img/knight/";

        final int moveDuration = 1000/8;
        final int attackDuration = Math.round(1000*KnightConstants.ATTACK_LOADING_DURATION);

        Vector2f attackTileSize = new Vector2f(96, 58);
        this.attackRenderer = new EffectRenderer(prepath + "animationAttackWhite.png", attackTileSize, Math.round (KnightConstants.ATTACK_LOADING_DURATION*1000/10));

        String[] activities = {"Attack", "Move"};

        int duration;
        String fileName;
        for (String vision : LivingBeingRenderer.ACCEPTED_VISION_DIRECTIONS) {
            for (String activity : activities) {
                fileName = vision;
                if (!activity.equals("Move")) {
                    fileName += activity;
                    duration = attackDuration;
                }
                else{
                    duration = moveDuration;
                }
                this.renderer.addView(vision + activity, new SpriteView(prepath + fileName + ".png", tileSize, duration));
            }
        }
        this.renderer.addView("bottomIdle", new SpriteView(prepath + "bottomIdle.png", tileSize, Math.round (KnightConstants.STUN_AFTER_ATTACK_DURATION*1000)));
    }

    /**
     * update the knight according to his behavior
     * @param target the player
     */
    public void update(LivingBeing target) {
        this.updateCountdown();
        if (this.isAttacking()) { //if the knight is attacking
            if (this.isAttackReady()) { //if he's ready to deal damage
                this.attack(target); //attack
            }
        }
        else { //if the knight isn't attacking
            if (!this.isStun()) { //if he's not stun
                this.renderer.setLastActivity("Move");
                super.updateSpeed(target.getCenter().sub(super.getCenter()).normalise().scale(this.getAccelerationRate())); //run toward the player
                super.move();

                if (this.isTargetInRange(target)) { //if the player is close enough
                    this.startAttacking(target); //start attacking the player
                }
            }
        }
    }

    /**
     * update every timer relative to the knight
     */
    void updateCountdown() {
        if (!this.isAttackReady()) {
            this.timeLeftBeforeAttack = this.timeLeftBeforeAttack - TimeScale.getInGameTimeScale().getDeltaTime();
        }
        if (this.isStun()) {
            this.timeLeftWhileStuned = this.timeLeftWhileStuned - TimeScale.getInGameTimeScale().getDeltaTime();
        }
    }

    /**
     * Indicate the knight is stun
     * @return true if he's stun, false otherwise
     */
    boolean isStun() {
        return this.timeLeftWhileStuned > 0;
    }

    /**
     * Indicate if the target is close enough in order to be hit by an attack
     * @param target the player
     * @return true if the player is in range, false otherwise
     */
    boolean isTargetInRange(LivingBeing target) {
        return (super.getCenter().add(getLocationOfTarget(target).scale(super.getRadius()*1.5f)).sub(target.getCenter()).length() < target.getRadius() + super.getRadius());
    }

    /**
     * Indicate if the target is in ranged according to the location set at the start of the attack
     * @param target the player
     * @return true if the player is stille in range, false otherwise
     */
    private boolean isTargetStillInRange(LivingBeing target) {
        return (super.getCenter().add(this.attackDirection.scale(super.getRadius()*1.5f)).sub(target.getCenter()).length() < target.getRadius() + super.getRadius());
    }

    /**
     * The knight start attacking the player
     * @param target the player
     */
    void startAttacking(LivingBeing target) {
        this.timeLeftBeforeAttack = KnightConstants.ATTACK_LOADING_DURATION;
        super.setSpeed(new Vector2f(0, 0));
        this.attackDirection = getLocationOfTarget(target);
        this.renderer.setLastActivity("Attack");
        this.renderer.update(this.attackDirection);
    }

    /**
     * Indicate if the knight is ready to deliver his shot
     * @return true if he's ready, false otherwise
     */
    boolean isAttackReady() {
        return (this.timeLeftBeforeAttack <= 0);
    }

    /**
     * Indicate if the knight is currently attacking
     * @return true if he's attacking, false otherwise
     */
    boolean isAttacking() {
        return (!this.attackDirection.equals(new Vector2f(0, 0)));
    }

    /**
     * Compute where is the player (NORTH, SOUTH, WEST or EAST)
     * @param target the player
     * @return a vector that reprensent roughly the direction to the player
     */
    private Vector2f getLocationOfTarget(LivingBeing target) {
        Vector2f directionOfTarget = new Vector2f(target.getCenter().sub(super.getCenter()));
        if (Math.abs(directionOfTarget.getX()) < Math.abs(directionOfTarget.getY())) {
            if (directionOfTarget.getY() < 0) {
                return new Vector2f(0, -1);
            }
            else {
                return new Vector2f(0, 1);
            }
        }
        else {
            if (directionOfTarget.getX() < 0) {
                return new Vector2f(-1, 0);
            }
            else {
                return new Vector2f(1, 0);
            }
        }
    }

    /**
     * Stun the knight to prevent him from doing another action for a set amount of time
     */
    void stun() {
        this.timeLeftWhileStuned = KnightConstants.STUN_AFTER_ATTACK_DURATION;
    }

    /**
     * Deal damage to the player if he is still in range
     * @param target the player
     */
    protected void attack(LivingBeing target) {
        if (this.isTargetStillInRange(target)) {
            target.takeDamage(super.getDamage());
        }
        this.attackDirection.set(0, 0);
        this.stun();
    }

    /**
     * Getter for the maximum speed
     * @return the norm of the maximum speed of the knight
     */
    @Override
    public float getMaxSpeed() {
        return MAX_SPEED;
    }

    /**
     * Getter for the acceleration
     * @return the norm of the acceleration of the knight
     */
    @Override
    public float getAccelerationRate() {
        return ACCELERATION_RATE;
    }

    public void render(Graphics g){
        super.render(g);
        if(isAttacking() && this.timeLeftBeforeAttack < ATTACK_LOADING_DURATION/5 && this.timeLeftBeforeAttack > -ATTACK_LOADING_DURATION/5){
            Vector2f position = this.getCenter().add(this.attackDirection.copy().scale(this.getRadius()));
            this.attackRenderer.render(g, (int) position.getX(), (int) position.getY(), (float) attackDirection.getTheta()-90);
        }
    }
}
