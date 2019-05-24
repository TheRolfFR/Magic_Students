package Entities;

public abstract class Monster extends LinvingBeing {

    protected int damage;

    public Monster(float x,float y,float maxSpeed, float accelerationRate, int hpCount, int armor, int damage){
        super(x,y,maxSpeed,accelerationRate,hpCount,armor);
        this.damage=damage;
    }

    public void doDamage(LinvingBeing opponent){
        if(opponent.collides(this)){
            opponent.takeDamage(this.damage);
        }
    }
}
