package Entities;

import static java.lang.Math.round;

public abstract class LivingBeing extends Entity {
    private int hpCount;
    private float armor;

    void takeDamage(int damage){
        hpCount = hpCount - round(damage / armor);
    }

    LivingBeing(float x, float y, float maxSpeed, float accelerationRate, int hpCount, float armor){
        super(x, y, maxSpeed, accelerationRate);
        this.hpCount=hpCount;
        this.armor=armor;
    }

    public int getHp() { return this.hpCount; }
}
