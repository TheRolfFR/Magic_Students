package Entities;

import Main.MainClass;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.ceil;
import static java.lang.Math.round;

public abstract class LivingBeing extends Entity implements Comparable {
    private int currentHealthPoints;
    private int maxHealthPoints;
    private float armorPoints;

    private static ArrayList<LivingBeing> livingBeings = new ArrayList<>();

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

    private void solveCollision(LivingBeing pusher, LivingBeing percuted, int level){
        if (level <= MainClass.getInstance().enemies.size()){
            percuted.collidingAction(pusher);
            if (percuted.collidesWith(MainClass.getInstance().player)){
                solveCollision(percuted,MainClass.getInstance().player,level+1);
            }
            for (Monster m: MainClass.getInstance().enemies) {
                if (percuted.collidesWith(m)){
                    solveCollision(percuted,m,level+1);
                }
            }
        }
    }

    public void checkCollision(){
        if (this.collidesWith(MainClass.getInstance().player)){
            solveCollision(this,MainClass.getInstance().player,1);
        }
        for (Monster m: MainClass.getInstance().enemies) {
            if (this.collidesWith(m)){
                solveCollision(this,m,1);
            }
        }
    }

    private void tpOutOf(LivingBeing opponent) {
        Vector2f diff = this.getCenter().sub(opponent.getCenter()).normalise().scale((float) ceil(radius + opponent.radius - opponent.getCenter().sub(getCenter()).length()));
        position.add(diff);
        if (position.x < 0) {
            position.x = 0;
        }
        if (position.x > MainClass.WIDTH - radius * 2) {
            position.x = MainClass.WIDTH - radius * 2;
        }
        if (position.y < 0) {
            position.y = 0;
        }
        if (position.y > MainClass.HEIGHT - radius * 2) {
            position.y = MainClass.HEIGHT - radius * 2;
        }
    }

    public void collidingAction(LivingBeing opponent) {
        if (collidesWith(opponent)){
            this.tpOutOf(opponent);
            opponent.tpOutOf(this);
        }
    }

    public void move() {
        this.position.add(this.speed.scale(MainClass.getInGameTimeScale().getTimeScale()));

        if (this.position.x < 0) {
            this.position.set(0, this.position.y);
        }
        if (this.position.x + this.getWidth() >= MainClass.WIDTH) {
            this.position.set(MainClass.WIDTH - this.getWidth(), this.position.y);
        }
        if (this.position.y < 0) {
            this.position.set(this.position.x, 0);
        }
        if (this.position.y + this.getHeight() >= MainClass.HEIGHT) {
            this.position.set(this.position.x, MainClass.HEIGHT - this.getHeight());
        }
    }


    @Override
    public int compareTo(Object o) {
        if (this.getPosition().getY() > ((LivingBeing) o).getPosition().getY()) {
            return 1;
        } else {
            return 0;
        }
    }
}
