package Entities;

public abstract class LinvingBeing extends Entity {
    protected int hpCount;
    protected int armor;

    public void takeDamage(int damage){
        hpCount = hpCount - damage + armor;
    }

    public LinvingBeing (float x,float y,float maxSpeed, float accelerationRate){
        super(x, y, maxSpeed, accelerationRate);
    }

    public LinvingBeing (){
        super();
    }
}
