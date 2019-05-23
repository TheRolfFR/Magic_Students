package Entities;

import Entities.Attacks.MeleeAttack;
import Entities.Attacks.RangedAttack;
import Main.MainClass;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

public class Player extends Entity implements MeleeAttack, RangedAttack {
    private float life;
    protected int width;
    protected int height;

    public Player(float x, float y, float maxSpeed, float accelerationRate) {
        super(x, y, maxSpeed, accelerationRate);
        this.life = 100;
    }

    @Override
    public Shape getBounds() {
        return new Rectangle(position.x, position.y, width, height);
    }

    @Override
    protected int getWidth() { return this.width; }
    @Override
    protected int getHeight() { return this.height; }
}
