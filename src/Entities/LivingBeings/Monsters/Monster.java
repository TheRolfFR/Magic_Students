package Entities.LivingBeings.Monsters;

import Entities.LivingBeings.LivingBeing;
import HUD.HealthBars.WorldHealthBar;
import org.newdawn.slick.Graphics;

/**
 * A monster is an living bein that isn't control by the player
 */
public abstract class Monster extends LivingBeing {
    private int damage;
    private WorldHealthBar worldHealthBar;

    /**
     * Constructor
     * @param x initial x-coordonate of this monster
     * @param y initial y-coordonare of this monster
     * @param width width of the image
     * @param height height of the image
     * @param hpCount amount of hp of this monster
     * @param armor amount of armor of this monster
     * @param damage damage of this monster
     * @param radius hitbox radius of this monster
     */
    public Monster(float x, float y, int width, int height, int hpCount, int armor, int damage, int radius) {
        super(x, y, width, height, hpCount, armor, radius);
        this.damage = damage;
        this.worldHealthBar = new WorldHealthBar(this);
        this.addMoveListener(this.worldHealthBar);
    }

    /**
     * Getter for the damage
     * @return the damage of this monster
     */
    public int getDamage() {
        return damage;
    }

    /**
     * Gette for the healthbar
     * @return the healthbar of this monster
     */
    public WorldHealthBar getWorldHealthBar() {
        return this.worldHealthBar;
    }

    /**
     * method to update the monster during the game
     * @param opponent the opponent that the monster will react to
     */
    public abstract void update(LivingBeing opponent);

    @Override
    public void render(Graphics g) {
        super.render(g, this.getSpeed());
    }

    /**
     * Allow the monster to take damage
     * @param damage damage value inflicted
     */
    @Override
    public void takeDamage(int damage) {
        super.takeDamage(damage);
    }
}
