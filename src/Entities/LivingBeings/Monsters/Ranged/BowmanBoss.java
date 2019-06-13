package Entities.LivingBeings.Monsters.Ranged;

import Entities.LivingBeings.LivingBeing;
import Entities.LivingBeings.Monsters.IBoss;
import Main.MainClass;
import org.newdawn.slick.geom.Vector2f;

import java.util.Random;

public class BowmanBoss extends Bowman implements IBoss {
    public static final Vector2f BOWMANBOSS_TILESIZE = new Vector2f(96,96);
    private int summonCooldown = 30*60;

    public BowmanBoss(float x, float y, float maxSpeed, float accelerationRate, int hpCount, int armor, int damage, int radius) {
        super(x, y,BOWMANBOSS_TILESIZE, maxSpeed, accelerationRate, hpCount, armor, damage, radius);
    }

    @Override
    public void update(LivingBeing target) {
        if(target.getPosition().distance(this.getPosition()) < 250) {
            this.updateSpeed(target.getPosition().sub(this.getPosition()).normalise().negate().scale(this.getAccelerationRate()));
            this.move();
        }
        else if(this.getSpeed().length()!=0){
            this.updateSpeed(this.getSpeed().normalise().negate().scale(getAccelerationRate()));
            this.move();
        }
        else if(this.delayCounter > SHOT_DELAY) {
            attack(target);
        }
        this.delayCounter = Math.min(this.delayCounter + 1, 121);
        updateCooldown();
        if(decideToSummon()){
            summon();
        }
    }

    private void updateCooldown() {
        if (summonCooldown != 0){
            summonCooldown = summonCooldown - 1;
        }
    }

    private void summon() {
        this.setSpeed(new Vector2f(0,0));
        MainClass.getInstance().getEnemiesManager().addBowman();
    }

    private boolean decideToSummon(){
        Random random = new Random();
        return (random.nextFloat()%1 < 1f/(60f*3f));
    }
}
