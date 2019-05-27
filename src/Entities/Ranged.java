package Entities;

import Entities.Attacks.RangedAttack;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

public abstract class Ranged extends Monster implements RangedAttack {
    protected int width;
    protected int height;

    public Ranged(float x, float y, int width, int height, float maxSpeed, float accelerationRate, int hpCount, int armor, int damage){
        super(x,y,maxSpeed,accelerationRate,hpCount,armor, damage);
        this.width=width;
        this.height=height;
    }

    @Override
    protected int getWidth() { return this.width; }
    @Override
    protected int getHeight() { return this.height; }
}
