package Entities;

public class Rusher extends Melee {
    public Rusher(float x, float y, int width, int height, float maxSpeed, float accelerationRate, int hpCount, float armor, int damage, int radius){
        super(x, y, width, height, maxSpeed, accelerationRate, hpCount, armor, damage, radius);
    }

    public void update(LivingBeing target, int i){
        this.updateSpeed(target.position.copy().sub(this.position).normalise().scale(this.getAccelerationRate()), i);

        this.move();
        this.doDamage(target);
    }
}
