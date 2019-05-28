package Entities;

public abstract class Monster extends LivingBeing {
    protected int damage;

    Monster(float x, float y, float maxSpeed, float accelerationRate, int hpCount, float armor, int damage, int radius){
        super(x, y, maxSpeed, accelerationRate, hpCount, armor, radius);
        this.damage=damage;
    }

    void doDamage(LivingBeing opponent){
        if (this.collides(opponent)){
            opponent.takeDamage(this.damage);
        }
    }
}
