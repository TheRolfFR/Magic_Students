package Entities.LivingBeings.Monsters.Ranged;

import Entities.LivingBeings.Monsters.Monster;
import Entities.Projectiles.Projectile;
import org.newdawn.slick.Graphics;

import java.util.ArrayList;

/**
 * Ranged monster are monster that can shoot projectiles
 */
public abstract class Ranged extends Monster {
    public static ArrayList<Projectile> enemyProjectiles = new ArrayList<>();
    public static ArrayList<Projectile> allyProjectiles = new ArrayList<>();

    /**
     * Constructor
     * @param x initial x-coordonate of the ranged monster
     * @param y initial y-coordonate of the ranged monster
     * @param width width of the image
     * @param height height of the image
     * @param hpCount amount of hp of the ranged monster
     * @param armor armor of the ranged monster
     * @param damage damage of the ranged monster
     * @param radius hitbox radius of the monster
     */
    public Ranged(float x, float y, int width, int height, int hpCount, int armor, int damage, int radius) {
        super(x , y, width, height, hpCount, armor, damage, radius);
    }

    public static void renderProjectiles(Graphics g) {
        for (Projectile p : Ranged.enemyProjectiles) {
            p.render(g);
        }
        for (Projectile p : Ranged.allyProjectiles) {
            p.render(g);
        }
    }
}
