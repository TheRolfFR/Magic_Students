package Entities.LivingBeings.Monsters.Ranged;

import Entities.LivingBeings.LivingBeing;
import Main.MainClass;

public class BowmanBoss extends Bowman{
    private int recoverTime = 0;
    private int summonCouldown = 30*60;

    public BowmanBoss(float x, float y, int width, int height, float maxSpeed, float accelerationRate, int hpCount, int armor, int damage, int radius) {
        super(x, y, width, height, maxSpeed, accelerationRate, hpCount, armor, damage, radius);
    }

    @Override
    public void update(LivingBeing target) {
        this.delayCounter = Math.min(this.delayCounter + 1, 121);
        if(target.getPosition().distance(this.getPosition()) < 150) {
            this.updateSpeed(target.getPosition().sub(this.getPosition()).normalise().negate().scale(this.getAccelerationRate()));
            this.move();
        }
        else if(this.getSpeed().length()!=0){
            this.updateSpeed(this.getSpeed().normalise().negate().scale(getAccelerationRate()));
            this.move();
        }
        else if(this.delayCounter > SHOT_DELAY && !MainClass.isGamePaused()) {
            attack(target);
        }
    }
}
