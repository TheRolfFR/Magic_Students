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

    /**
     * method to update the monster during the game
     * @param opponent the opponent that the monster will react to
     */
    public abstract void update(LivingBeing opponent);
}
