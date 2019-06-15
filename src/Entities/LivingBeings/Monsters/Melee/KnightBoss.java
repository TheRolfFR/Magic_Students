package Entities.LivingBeings.Monsters.Melee;

import Entities.LivingBeings.LivingBeing;
import Entities.LivingBeings.Monsters.BossConstants;
import Entities.LivingBeings.Monsters.IBoss;
import Main.MainClass;
import Main.TimeScale;
import Managers.EnemiesManager;
import Renderers.SpriteView;
import org.newdawn.slick.geom.Vector2f;

import java.util.Random;

public class KnightBoss extends Knight implements IBoss, BossConstants {
    public static final Vector2f KNIGHTBOSS_TILESIZE = new Vector2f(96, 96);
    private float summonCooldown = BossConstants.SUMMON_COOLDOWN;

    public KnightBoss(float x, float y, float maxSpeed, float accelerationRate, int hpCount, int armor, int damage, int radius) {
        super(x, y, KNIGHTBOSS_TILESIZE, maxSpeed, accelerationRate, hpCount, armor, damage, radius);
        this.renderer.addView("Summon", new SpriteView("img/knight/" + "Summon.png", KnightConstant.KNIGHT_TILESIZE, Math.round (KnightConstant.STUN_AFTER_ATTACK_DURATION*1000/3)));

    }

    @Override
    public void update(LivingBeing target) {
        this.updateCountdown();
        if (super.isAttacking()) {
            if (super.isAttackReady()) {
                super.attack(target);
            }
        }
        else {
            if (this.isSummonReady()) {
                if (this.decideToSummon()) {
                    this.summon();
                }
            }
            if (!super.isStun()) {
                this.renderer.setLastActivity("Move");
                super.updateSpeed(target.getCenter().sub(super.getCenter()).normalise().scale(super.getAccelerationRate()));
                super.move();
                if (super.isTargetInRange(target)) {
                    super.startAttacking(target);
                }
            }
        }
    }

    @Override
    void updateCountdown() {
        if (this.summonCooldown > 0) {
            this.summonCooldown = this.summonCooldown - TimeScale.getInGameTimeScale().getDeltaTime();
        }
        super.updateCountdown();
    }

    private void summon() {
        super.setSpeed(new Vector2f(0, 0));
        this.renderer.setLastActivity("Summon");
        this.triggerListener(EnemiesManager.newKnight());
        this.summonCooldown = BossConstants.SUMMON_COOLDOWN;
        super.stun();
    }

    private boolean decideToSummon() {
        Random random = new Random();
        return (random.nextFloat()%1 < 1f/(MainClass.getNumberOfFramePerSecond()*BossConstants.AVERAGE_SECONDS_BEFORE_SUMMONING));
    }

    private boolean isSummonReady() {
        return this.summonCooldown <= 0;
    }
}


