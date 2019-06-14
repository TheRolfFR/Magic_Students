package Entities.LivingBeings.Monsters.Ranged;

import Entities.LivingBeings.LivingBeing;
import Entities.LivingBeings.Monsters.IBoss;
import Main.MainClass;
import org.newdawn.slick.geom.Vector2f;

import java.util.Random;

public class BowmanBoss extends Bowman implements IBoss {
    public static final Vector2f BOWMANBOSS_TILESIZE = new Vector2f(96,96);
    private int summonCooldown = 30*MainClass.getNumberOfFramePerSecond();

    public BowmanBoss(float x, float y, float maxSpeed, float accelerationRate, int hpCount, int armor, int damage, int radius) {
        super(x, y,BOWMANBOSS_TILESIZE, maxSpeed, accelerationRate, hpCount, armor, damage, radius);
    }

    @Override
    public void update(LivingBeing target) {
        updateCooldown();
        if (this.isAttacking()){
            if (this.isAttackReady()){
                attack(target);
            }
            else {
                aim(target);
            }
        }
        else {
            if (!isStun())
            {
                if (isSummonReady()){
                    if (decideToSummon()){
                        summon();
                    }
                }
                else {
                    if (isShootReady()){
                        startAttacking(target);
                    }
                    else {
                        if(target.getPosition().distance(this.getPosition()) < RUN_AWAY_THRESHOLD) {
                            runAway(target);
                        }
                        else {
                            if (!isSpeedLocked()){
                                if (decideToMove()){
                                    chooseDirection();
                                }
                                else {
                                    if(this.getSpeed().length() != 0) {
                                        this.updateSpeed(this.getSpeed().normalise().negate().scale(getAccelerationRate()));
                                    }
                                }
                            }
                        }
                    }
                }

                this.move();
            }
        }
    }

    private boolean isSummonReady() {
        return summonCooldown == 0;
    }

    @Override
    void updateCooldown() {
        if (summonCooldown != 0){
            summonCooldown = summonCooldown - 1;
        }
        super.updateCooldown();
    }

    private void summon() {
        this.setSpeed(new Vector2f(0,0));
        MainClass.getInstance().getEnemiesManager().addBowman();
        summonCooldown = 30*MainClass.getNumberOfFramePerSecond();
        this.setSpeed(new Vector2f(0,0));
        stun();
    }

    private boolean decideToSummon(){
        Random random = new Random();
        return (random.nextFloat()%1 < 1f/(60f*3f));
    }
}
