package Entities.LivingBeings.Monsters.Melee;

import Entities.LivingBeings.LivingBeing;
import Entities.LivingBeings.Monsters.IBoss;
import Main.MainClass;
import org.newdawn.slick.geom.Vector2f;

import java.util.Random;

public class KnightBoss extends Knight implements IBoss {
    public static final Vector2f KNIGHTBOSS_TILESIZE = new Vector2f(96,96);
    private int recoverTime = 0;
    private int summonCooldown = 30*MainClass.getNumberOfFramePerSecond();

    public KnightBoss(float x, float y, float maxSpeed, float accelerationRate, int hpCount, int armor, int damage, int radius) {
        super(x, y, KNIGHTBOSS_TILESIZE, maxSpeed, accelerationRate, hpCount, armor, damage, radius);
    }

    @Override
    public void update(LivingBeing target){
        updateCooldown();
        if (this.isAttacking()){
            if (this.isAttackReady()){
                attack(target);
            }
            else {
                gettingReady();
            }
        }
        else {
            if (isSummonReady()){
                if (decideToSummon()){
                    summon();
                }
            }
            if (isAbleToMove()){
                this.updateSpeed(target.getPosition().sub(this.getPosition()).normalise().scale(this.getAccelerationRate()));
                this.move();
                if (isTargetInRange(target)){
                    startAttacking(target);
                }
            }
            else {
                recover();
            }
        }
    }

    private void updateCooldown() {
        if (summonCooldown != 0){
            summonCooldown = summonCooldown - 1;
        }
    }

    private void recover() {
        recoverTime = recoverTime - 1;
    }

    private boolean isAbleToMove() {
        return recoverTime == 0;
    }

    private void summon() {
        this.setSpeed(new Vector2f(0,0));
        MainClass.getInstance().getEnemiesManager().addKnight();
        this.summonCooldown = 30*MainClass.getNumberOfFramePerSecond();
    }

    private boolean decideToSummon(){
        Random random = new Random();
        return (random.nextFloat()%1 < 1f/(60f*3f));
    }

    private boolean isSummonReady() {
        return summonCooldown == 0;
    }
}


