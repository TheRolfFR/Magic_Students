package Entities;

import static java.lang.Math.round;

public abstract class LivingBeing extends Entity {
    private int currentHealthPoints;
    private int maxHealthPoints;
    private float armorPoints;

    /**
     * Returns the current health points
     * @return the current health points
     */
    public int getCurrentHealthPoints() {
        return currentHealthPoints;
    }

    /**
     * Returns the maximum health points
     * @return the maximum health points
     */
    public int getMaxHealthPoints() {
        return maxHealthPoints;
    }

    /**
     * Single constructor
     * @param x x initial position of the living being
     * @param y y initial position of the living being
     * @param maxSpeed max speed of the living being
     * @param accelerationRate acceleration factor of the living being
     * @param maxHealthPoints maximum health points of the living being
     * @param armorPoints armor points of the living being
     */
    LivingBeing(float x, float y, float maxSpeed, float accelerationRate, int maxHealthPoints, float armorPoints){
        super(x, y, maxSpeed, accelerationRate);
        this.currentHealthPoints = maxHealthPoints;
        this.maxHealthPoints = maxHealthPoints;
        this.armorPoints = armorPoints;
    }

    /**
     * allows the living being to take damage
     * @param damage damage value inflicted
     */
    void takeDamage(int damage){
        currentHealthPoints = Math.max(0, currentHealthPoints - round(damage / armorPoints));
    }
}
