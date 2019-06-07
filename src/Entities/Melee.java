package Entities;

public abstract class Melee extends Monster{

    Melee(float x, float y, int width, int height, float maxSpeed, float accelerationRate, int hpCount, float armor, int damage, int radius){
        super(x , y, width, height, maxSpeed, accelerationRate, hpCount, armor, damage, radius);
    }
}
