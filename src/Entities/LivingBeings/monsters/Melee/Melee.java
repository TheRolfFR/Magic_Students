package Entities.LivingBeings.monsters.Melee;

import Entities.LivingBeings.monsters.Monster;

public abstract class Melee extends Monster {

    protected Melee(float x, float y, int width, int height, float maxSpeed, float accelerationRate, int hpCount, int armor, int damage, int radius){
        super(x , y, width, height, maxSpeed, accelerationRate, hpCount, armor, damage, radius);
    }
}
