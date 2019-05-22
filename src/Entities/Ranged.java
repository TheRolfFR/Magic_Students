package Entities;

import Entities.Attacks.RangedAttack;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

public class Ranged extends Monster implements RangedAttack {
    public boolean canMove() { return true; }
    protected int width;
    protected int height;

    @Override
    public Shape getBounds() {
        return new Rectangle(position.x,position.y,width,height);
    }
}
