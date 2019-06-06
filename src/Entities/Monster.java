package Entities;

import HUD.HealthBar;

public abstract class Monster extends LivingBeing {
    private int damage;
    private HealthBar healthBar;

    Monster(float x, float y, float maxSpeed, float accelerationRate, int hpCount, float armor, int damage, int radius){
        super(x, y, maxSpeed, accelerationRate, hpCount, armor, radius);
        this.damage = damage;
    }

    void doDamage(LivingBeing opponent){
        if (this.collidesWith(opponent)){
            opponent.takeDamage(this.damage);
        }
    }

    public void setHealthBar(HealthBar healthBar){
        this.healthBar = healthBar;
    }

    public HealthBar getHealthBar(){
        return this.healthBar;
    }

    /**
     * method to update the monster during the game
     * @param opponent the opponent that the monster will react to
     */
    public abstract void update(LivingBeing opponent);
}
