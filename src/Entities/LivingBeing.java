package Entities;

import static java.lang.Math.round;

public abstract class LivingBeing extends Entity {
    private int currentHealthPoints;
    private int maxHealthPoints;
    private float armor;

    public int getCurrentHealthPoints() {
        return currentHealthPoints;
    }

    public int getMaxHealthPoints() {
        return maxHealthPoints;
    }

    LivingBeing(float x, float y, float maxSpeed, float accelerationRate, int maxHealthPoints, float armor){
        super(x, y, maxSpeed, accelerationRate);
        this.currentHealthPoints = maxHealthPoints;
        this.maxHealthPoints = maxHealthPoints;
        this.armor = armor;
    }

    void takeDamage(int damage){
        currentHealthPoints = Math.max(0, currentHealthPoints - round(damage / armor));
    }
}
