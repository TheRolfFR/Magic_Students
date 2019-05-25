package Entities;

import Entities.Attacks.MeleeAttack;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

public abstract class Melee extends Monster implements MeleeAttack {
    protected int width;
    protected int height;

    Melee(float x, float y, int width, int height, float maxSpeed, float accelerationRate, int hpCount, float armor, int damage){
        super(x , y, maxSpeed, accelerationRate, hpCount, armor, damage);
        this.width=width;
        this.height=height;
    }

    @Override
    public Shape getBounds() {
        return new Rectangle(position.x, position.y, width, height);
    }

    @Override
    public int getWidth() { return this.width; }
    @Override
    public int getHeight() { return this.height; }
}
