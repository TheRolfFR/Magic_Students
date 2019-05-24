package Entities;

public abstract class Projectile extends Entity {
    protected int damage;

    public int getDamage(){
        return damage;
    }

    public void collidingAction(LivingBeing opponent) {
        if (collides(opponent)){
            opponent.takeDamage(damage);
            //kill();
        }
    }
}
