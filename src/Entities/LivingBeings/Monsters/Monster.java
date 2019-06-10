package Entities.LivingBeings.Monsters;

import Entities.LivingBeings.LivingBeing;
import HUD.HealthBar;
import org.newdawn.slick.Graphics;

public abstract class Monster extends LivingBeing {
    private int damage;
    private HealthBar healthBar;

    public Monster(float x, float y, int width, int height, float maxSpeed, float accelerationRate, int hpCount, int armor, int damage, int radius){
        super(x, y, width, height, maxSpeed, accelerationRate, hpCount, armor, radius);
        this.damage = damage;
    }

    protected void doDamage(LivingBeing opponent){
        if (this.collidesWith(opponent)){
            opponent.takeDamage(this.damage);
        }
    }

    public int getDamage() {
        return damage;
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

    @Override
    public void render(Graphics g) {
        super.render(g, this.getSpeed());
    }
}
