package Entities;

import Entities.Attacks.MeleeAttack;
import Entities.Attacks.RangedAttack;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

public class Player extends LinvingBeing implements MeleeAttack, RangedAttack {
    private float life;
    protected int width;
    protected int height;

    public Player(float x, float y, int width, int height, float maxSpeed, float accelerationRate) {
        super(x, y, maxSpeed, accelerationRate);
        this.width = width;
        this.height = height;
        this.life = 100;
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
