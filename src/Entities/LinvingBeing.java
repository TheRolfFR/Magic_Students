package Entities;

public abstract class LinvingBeing extends Entity {
    protected int hpCount;
    protected int armor;

    public void takeDamage(int damage){
        hpCount = hpCount - (damage - armor)*(damage>armor? 1:0);
    }

    public LinvingBeing (float x,float y,float maxSpeed, float accelerationRate, int hpCount, int armor){
        super(x, y, maxSpeed, accelerationRate);
        this.hpCount=hpCount;
        this.armor=armor;
    }

    public LinvingBeing (){
        super();
    }
}
