package Entities.LivingBeings.Monsters.Ranged;

import Entities.LivingBeings.LivingBeing;
import Entities.Projectiles.Arrow;
import Main.MainClass;
import Main.TimeScale;
import Renderers.LivingBeingRenderer;
import Renderers.SpriteView;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

import java.util.Random;

public class Bowman extends Ranged implements BowmanConstants{

    public static final Vector2f BOWMAN_TILESIZE = new Vector2f(48, 48);
    private float framesLeftBeforeAttack;
    private Vector2f attackDirection = new Vector2f(0, 0);
    private float framesLeftWhileStuned = BowmanConstants.STUN_AFTER_ATTACK_DURATION;
    private float framesLeftWhileSpeedLocked = BowmanConstants.MOVEMENT_DURATION;
    private float shootCooldown = BowmanConstants.SHOOT_COOLDOWN;

    public Bowman(float x, float y, int hpCount, int armor, int damage, int radius) {
        super(x, y, (int) BOWMAN_TILESIZE.getX(), (int) BOWMAN_TILESIZE.getY(), hpCount, armor, damage, radius);

        this.renderer = new LivingBeingRenderer(this, BOWMAN_TILESIZE);

        final String prepath = "img/bowman/";

        final int duration = 1000/8;

        for (String vision : LivingBeingRenderer.ACCEPTED_VISION_DIRECTIONS) {
            this.renderer.addView(vision + "Move", new SpriteView(prepath + vision + ".png", BOWMAN_TILESIZE, duration));
        }
    }

    public Bowman(float x, float y, Vector2f tileSize, int hpCount, int armor, int damage, int radius) {
        super(x, y, (int) tileSize.getX(), (int) tileSize.getY(), hpCount, armor, damage, radius);

        this.renderer = new LivingBeingRenderer(this, tileSize);

        final String prepath = "img/bowman/";

        final int duration = 1000/8;

        for (String vision : LivingBeingRenderer.ACCEPTED_VISION_DIRECTIONS) {
            this.renderer.addView(vision + "Move", new SpriteView(prepath + vision + ".png", tileSize, duration));
        }
    }

    @Override
    public void update(LivingBeing target) {
        this.updateCountdown();
        if (this.isAttacking()) {
            if (this.isAttackReady()) {
                this.attack(target);
            }
            else {
                this.aim(target);
            }
        }
        else {
            if (!this.isStun())
            {
                if (this.isShootReady()) {
                    this.startAttacking(target);
                }
                else {
                    if (this.targetIsClose(target)) {
                        this.runAway(target);
                    }
                    else {
                        if (!this.isSpeedLocked()) {
                            if (this.decideToMove()) {
                                this.chooseDirection();
                            }
                            else {
                                if (super.getSpeed().length() != 0) {
                                    super.updateSpeed(super.getSpeed().normalise().negate().scale(getAccelerationRate()));
                                }
                            }
                        }
                    }
                }
                super.move();
            }
        }
    }

    boolean targetIsClose(LivingBeing target) {
        return target.getCenter().distance(super.getCenter()) < BowmanConstants.RUN_AWAY_THRESHOLD;
    }

    void runAway(LivingBeing target) {
        super.updateSpeed(target.getCenter().sub(super.getCenter()).normalise().negate().scale(this.getAccelerationRate()));
        this.framesLeftWhileSpeedLocked = 0;
    }

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

    boolean isShootReady() {
        return this.shootCooldown <= 0;
    }

    void startAttacking(LivingBeing target) {
        this.attackDirection.set(target.getCenter().sub(this.getCenter()).normalise());
        this.setSpeed(new Vector2f(0, 0));
        this.framesLeftBeforeAttack = BowmanConstants.ATTACK_LOADING_DURATION;
    }

    void chooseDirection() {
        Random random = new Random();
        this.updateSpeed(new Vector2f(random.nextFloat(), random.nextFloat()).normalise().scale(this.getAccelerationRate()));
        this.framesLeftWhileSpeedLocked = BowmanConstants.MOVEMENT_DURATION;
    }

    boolean decideToMove() {
        Random random = new Random();
        return (random.nextFloat()%1 < 1f/(MainClass.getNumberOfFramePerSecond()*BowmanConstants.AVERAGE_SECONDS_BEFORE_MOVEMENT));
    }

    boolean isSpeedLocked() {
        return this.framesLeftWhileSpeedLocked > 0;
    }

    boolean isStun() {
        return this.framesLeftWhileStuned > 0;
    }

    boolean isAttackReady() {
        return (this.framesLeftBeforeAttack <= 0);
    }

    boolean isAttacking() {
        return (!this.attackDirection.equals(new Vector2f(0, 0)));
    }

    void aim(LivingBeing target) {
        this.attackDirection.set(target.getCenter().sub(super.getCenter()).normalise());
    }

    protected void attack(LivingBeing target) {

        this.attackDirection.set(target.getCenter().sub(super.getCenter()).normalise());
        Ranged.enemyProjectiles.add(new Arrow(super.getCenter().add(this.attackDirection.copy().scale(super.getRadius())), this.attackDirection.copy()));
        Ranged.enemyProjectiles.get(Ranged.enemyProjectiles.size()-1).setShowDebugRect(true);
        this.attackDirection.set(0, 0);
        this.shootCooldown = BowmanConstants.SHOOT_COOLDOWN;
        this.stun();
    }

    void stun() {
        this.framesLeftWhileStuned = BowmanConstants.STUN_AFTER_ATTACK_DURATION;
    }

    public void render(Graphics g) {
        super.render(g);
    }

    @Override
    public float getMaxSpeed() {
        return MAX_SPEED;
    }

    @Override
    public float getAccelerationRate() {
        return ACCELERATION_RATE;
    }
}
