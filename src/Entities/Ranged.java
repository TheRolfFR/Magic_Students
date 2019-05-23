package Entities;

import Entities.Attacks.RangedAttack;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

public class Ranged extends Monster implements RangedAttack {
    protected int width;
    protected int height;

    @Override
    public Shape getBounds() {
        return new Rectangle(position.x, position.y, width, height);
    }

    @Override
    protected int getWidth() { return this.width; }
    @Override
    protected int getHeight() { return this.height; }
}
