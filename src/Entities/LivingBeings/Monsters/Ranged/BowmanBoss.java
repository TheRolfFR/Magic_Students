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

/**
 * a BomanBoss is a bigger and deadlier bowman that can summon allies
 */
public class BowmanBoss extends Bowman implements IBoss, BossConstants {
    public static final Vector2f BOWMANBOSS_TILESIZE = new Vector2f(144, 144);
    private float summonCooldown = BossConstants.SUMMON_COOLDOWN;

    /**
     * Constructor
     * @param x initial x-coodonate of the boss
     * @param y initial y-coordonate of the boss
     * @param hpCount hpcount od the boss
     * @param armor armor of the boss
     * @param damage damage od the boss
     * @param radius hitbos radius of the boss
     */
    public BowmanBoss(float x, float y, int hpCount, int armor, int damage, int radius) {
        super(x, y, BOWMANBOSS_TILESIZE, hpCount, armor, damage, radius);
        this.renderer.addView("bottomSummon", new SpriteView("img/bowman/Summon.png", BOWMANBOSS_TILESIZE, Math.round(1000*STUN_AFTER_SUMMON/6)));
    }

    /**
     * update the boss according to his behavior
     * @param target the player
     */
    @Override
    public void update(LivingBeing target) {
        this.updateCountdown();
        if (super.isAttacking()) { //if he's attacking
            if (super.isAttackReady()) { //if the attack is ready
                super.attack(target); //shoot
            }
            else { //if the attack isn't ready
                super.aim(target); //aim
            }
        }
        else { //if he's not attacking
            if (!super.isStun()) { //if he's not stun
                if (this.isSummonReady() && this.decideToSummon()) { //if he can summon and decide to
                        this.summon(); //summon
                }
                else { //if he doesn't summon
                    if (super.isShootReady()) { //if he can shoot
                        super.startAttacking(target); //start attacking
                    }
                    else { //if he cannot shoot
                        if (super.targetIsClose(target)) { //if the target is close
                            super.runAway(target); //run away
                        }
                        else { //if the target isn't close
                            if (!super.isSpeedLocked()) { //if he hasn't decide to move yet
                                if (super.decideToMove()) { //if he decide to move
                                    super.chooseDirection(); //choose a direction
                                }
                                else { //if he hasn't decide to move
                                    this.renderer.setLastActivity("Idle");
                                    if (super.getSpeed().length() != 0) { //if he has speed
                                        super.updateSpeed(super.getSpeed().normalise().negate().scale(getAccelerationRate())); //slow down
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

    /**
     * Indicate if the boss can use his summon spell
     * @return true if he can, false otherwise
     */
    private boolean isSummonReady() {
        return this.summonCooldown <= 0;
    }

    /**
     * update every timer relative to the bowman boss
     */
    @Override
    void updateCountdown() {
        if (this.summonCooldown > 0) {
            this.summonCooldown = this.summonCooldown - TimeScale.getInGameTimeScale().getDeltaTime();
        }
        super.updateCountdown();
    }

    /**
     *
     */
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
