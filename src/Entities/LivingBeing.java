package Entities;

import org.newdawn.slick.Graphics;
import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.round;

public abstract class LivingBeing extends Entity implements Comparable {
    private int currentHealthPoints;
    private int maxHealthPoints;
    private float armorPoints;

    protected static ArrayList<LivingBeing> livingBeings = new ArrayList<>();

    /**
     * In game rendering of all Living beings
     * @param g the graphics to draw on
     */
    public static void sortAndRenderLivingBeings(Graphics g) {
        Collections.sort(livingBeings);

        for(LivingBeing lb : livingBeings) {
            lb.render(g);
        }
    }

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
    LivingBeing(float x, float y, float maxSpeed, float accelerationRate, int maxHealthPoints, float armorPoints, int radius){
        super(x, y, maxSpeed, accelerationRate, radius);
        this.currentHealthPoints = maxHealthPoints;
        this.maxHealthPoints = maxHealthPoints;
        this.armorPoints = armorPoints;

        livingBeings.add(this);
    }

    /**
     * allows the living being to take damage
     * @param damage damage value inflicted
     */
    void takeDamage(int damage){
        currentHealthPoints = Math.max(0, currentHealthPoints - round(damage / armorPoints));
    }

    public boolean isDead(){
        return this.currentHealthPoints<=0;
    }

    void tpOutside(LivingBeing opponent){
        position = position.add(position.copy().sub(opponent.position).normalise().scale(radius+opponent.radius-opponent.position.copy().sub(position).length()));
    }

    public void collidingAction(LivingBeing opponent) {
        if (collides(opponent)){
            if (this.getMaxHealthPoints() <= opponent.getMaxHealthPoints()){
                tpOutside(opponent);
            }
            else {
                opponent.tpOutside(this);
            }
        }
    }

    @Override
    public int compareTo(Object o) {
        if(this.getPosition().getY() > ((LivingBeing) o).getPosition().getY()) {
            return 1;
        } else {
            return 0;
        }
    }
}
