package Entities;

public abstract class Projectile extends Entity {
    protected int damage;

    public Projectile() {}
    public Projectile(float x, float y, float maxSpeed, float accelerationRate) {
        super(x, y, maxSpeed, accelerationRate);
    }

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
