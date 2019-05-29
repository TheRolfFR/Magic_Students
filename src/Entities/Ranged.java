package Entities;

import Entities.Attacks.RangedAttack;
import java.util.ArrayList;

public abstract class Ranged extends Monster implements RangedAttack {
    protected int width;
    protected int height;

    public static ArrayList<Projectile> enemyProjectiles = new ArrayList<>();

    public Ranged(float x, float y, int width, int height, float maxSpeed, float accelerationRate, int hpCount, int armor, int damage, int radius){
        super(x , y, maxSpeed, accelerationRate, hpCount, armor, damage, radius);
        this.width=width;
        this.height=height;
    }

    @Override
    protected int getWidth() { return this.width; }
    @Override
    protected int getHeight() { return this.height; }
}
