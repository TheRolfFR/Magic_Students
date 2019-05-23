package Entities;

import org.newdawn.slick.geom.Shape;

public abstract class Projectile extends Entity {

    private int damage;

    public int getDamage(){
        return damage;
    }

    public boolean canMove() { return true; }
}
