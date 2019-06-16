package Entities.LivingBeings.Monsters.Melee;

import Entities.LivingBeings.Monsters.Monster;

public abstract class Melee extends Monster {

    protected Melee(float x, float y, int width, int height, int hpCount, int armor, int damage, int radius) {
        super(x , y, width, height, hpCount, armor, damage, radius);
    }
}
