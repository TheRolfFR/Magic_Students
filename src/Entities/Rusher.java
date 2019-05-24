package Entities;

public class Rusher extends Melee {

    public Rusher(float x, float y, int width, int height, float maxSpeed, float accelerationRate, int hpCount, float armor, int damage){
        super(x, y, width, height, maxSpeed, accelerationRate, hpCount, armor, damage);
    }

    public void chaseAI(LivingBeing target){
        this.updateSpeed(target.position.copy().sub(this.position).normalise().scale(this.ACCELERATION_RATE));

        this.move();
        this.doDamage(target);
    }
}
