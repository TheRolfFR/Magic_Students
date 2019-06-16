package Entities.LivingBeings.Monsters.Ranged;

import Entities.LivingBeings.Monsters.Monster;
import Entities.Projectiles.Projectile;

import java.util.ArrayList;

public abstract class Ranged extends Monster {
    public static ArrayList<Projectile> enemyProjectiles = new ArrayList<>();
    public static ArrayList<Projectile> allyProjectiles = new ArrayList<>();

    public Ranged(float x, float y, int width, int height, int hpCount, int armor, int damage, int radius) {
        super(x , y, width, height, hpCount, armor, damage, radius);
    }
}
