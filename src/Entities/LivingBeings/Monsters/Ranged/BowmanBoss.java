package Entities.LivingBeings.Monsters.Ranged;

import Entities.LivingBeings.LivingBeing;
import Entities.LivingBeings.Monsters.BossConstants;
import Entities.LivingBeings.Monsters.IBoss;
import Main.MainClass;
import Main.TimeScale;
import Managers.EnemiesManager;
import Renderers.SpriteView;
import org.newdawn.slick.geom.Vector2f;

import java.util.Random;

public class BowmanBoss extends Bowman implements IBoss, BossConstants {
    public static final Vector2f BOWMANBOSS_TILESIZE = new Vector2f(144, 144);
    private float summonCooldown = BossConstants.SUMMON_COOLDOWN;

    public BowmanBoss(float x, float y, int hpCount, int armor, int damage, int radius) {
        super(x, y, BOWMANBOSS_TILESIZE, hpCount, armor, damage, radius);
        this.renderer.addView("bottomSummon", new SpriteView("img/bowman/Summon.png", BOWMANBOSS_TILESIZE, Math.round(1000*STUN_AFTER_SUMMON/6)));
    }

    @Override
    public void update(LivingBeing target) {
        this.updateCountdown();
        if (super.isAttacking()) {
            if (super.isAttackReady()) {
                super.attack(target);
            }
            else {
                super.aim(target);
            }
        }
        else {
            if (!super.isStun()) {
                if (this.isSummonReady()) {
                    if (this.decideToSummon()) {
                        this.summon();
                    }
                }
                else {
                    if (super.isShootReady()) {
                        super.startAttacking(target);
                    }
                    else {
                        if (super.targetIsClose(target)) {
                            super.runAway(target);
                        }
                        else {
                            if (!super.isSpeedLocked()) {
                                if (super.decideToMove()) {
                                    super.chooseDirection();
                                }
                                else {
                                    this.renderer.setLastActivity("Idle");
                                    if (super.getSpeed().length() != 0) {
                                        super.updateSpeed(super.getSpeed().normalise().negate().scale(getAccelerationRate()));
                                    }
                                }
                            }
                        }
                    }
                }
                super.move();
            }
        }
    }

    private boolean isSummonReady() {
        return this.summonCooldown <= 0;
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
        this.renderer.update(new Vector2f(0,1));
        this.triggerListener(EnemiesManager.newBowman());
        this.summonCooldown = BossConstants.SUMMON_COOLDOWN;
        super.setSpeed(new Vector2f(0, 0));
        this.stun();
    }

    void stun(){super.framesLeftWhileStuned = BossConstants.STUN_AFTER_SUMMON;}

    private boolean decideToSummon() {
        Random random = new Random();
        return (random.nextFloat()%1 < 1f/(MainClass.getNumberOfFramePerSecond()*BossConstants.AVERAGE_SECONDS_BEFORE_SUMMONING));
    }
}
