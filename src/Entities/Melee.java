package Entities;

import Entities.Attacks.MeleeAttack;
import Main.MainClass;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

public class Melee extends Monster implements MeleeAttack {
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
