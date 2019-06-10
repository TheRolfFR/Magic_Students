package Entities.LivingBeings;

import Entities.Entity;
import Entities.LivingBeings.monsters.Monster;
import Main.MainClass;
import Main.TimeScale;
import Renderer.LivingBeingRenderer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

public abstract class LivingBeing extends Entity implements Comparable {
    private int currentHealthPoints;
    private int maxHealthPoints;
    private int armorPoints;

    public static ArrayList<LivingBeing> livingBeings = new ArrayList<>();

    protected LivingBeingRenderer renderer;

    public void heal(int amountOfHealing) {
        this.currentHealthPoints = this.currentHealthPoints + amountOfHealing;
        if (currentHealthPoints > maxHealthPoints){
            currentHealthPoints = maxHealthPoints;
        }
    }

    public void buffHP(int buffAmount){
        this.maxHealthPoints = this.maxHealthPoints + buffAmount;
        this.heal(buffAmount);
    }

    public void buffArmor(int buffAmount){
        this.armorPoints = this.armorPoints + buffAmount;
    }

    public void buffSpeed(float buffAmount){
        this.MAX_SPEED = this.MAX_SPEED + buffAmount;
        this.ACCELERATION_RATE = this.ACCELERATION_RATE + buffAmount * 135/450;
    }

    /**
     * In game rendering of all Living beings
     * @param g the graphics to draw on
     */
    public static void sortAndRenderLivingBeings(Graphics g) {
        Collections.sort(livingBeings);

        for (LivingBeing lb : livingBeings) {
            lb.render(g);
        }
    }

    public int getArmorPoints() {
        return armorPoints;
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
    public LivingBeing(float x, float y, int width, int height, float maxSpeed, float accelerationRate, int maxHealthPoints, int armorPoints, int radius) {
        super(x, y, width, height, maxSpeed, accelerationRate, radius);
        this.currentHealthPoints = maxHealthPoints;
        this.maxHealthPoints = maxHealthPoints;
        this.armorPoints = armorPoints;

        livingBeings.add(this);
    }

    public LivingBeing(float x, float y, float maxSpeed, float accelerationRate, int maxHealthPoints, int armorPoints, int radius) {
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
    public void takeDamage(int damage) {
        currentHealthPoints = Math.max(0, currentHealthPoints - Math.max(damage - armorPoints, 0));
    }

    public boolean isDead(){
        return this.currentHealthPoints <= 0;
    }

    private void solveCollision(LivingBeing pusher, LivingBeing percuted, int level) {
        if (level <= MainClass.getInstance().getEnemies().size()) {
            percuted.collidingAction(pusher);
            if (percuted.collidesWith(MainClass.getInstance().getPlayer())) {
                solveCollision(percuted,MainClass.getInstance().getPlayer(),level+1);
            }
            for (Monster m: MainClass.getInstance().getEnemies()) {
                if (percuted.collidesWith(m)) {
                    solveCollision(percuted,m,level+1);
                }
            }
        }
    }

    public void checkCollision() {
        if (this.collidesWith(MainClass.getInstance().getPlayer())) {
            solveCollision(this,MainClass.getInstance().getPlayer(),1);
        }
        for (Monster m: MainClass.getInstance().getEnemies()) {
            if (this.collidesWith(m)){
                solveCollision(this,m,1);
            }
        }
    }

    private void tpOutOf(LivingBeing opponent) {
        Vector2f diff = this.getCenter().sub(opponent.getCenter()).normalise().scale((float) ceil(getRadius() + opponent.getRadius() - opponent.getCenter().sub(getCenter()).length()));
        setPosition(getPosition().add(diff));
        tpInBounds();
    }

    public void collidingAction(LivingBeing opponent) {
        if (collidesWith(opponent)) {
            this.tpOutOf(opponent);
            opponent.tpOutOf(this);
        }
    }
    private void tpInBounds() {
        if (this.getCenter().x < getRadius()) {
            this.setPosition(new Vector2f(getRadius() - this.getTileSize().getX()/2 , this.getPosition().getY()));
        }
        if (this.getCenter().x >= MainClass.WIDTH - getRadius()) {
            this.setPosition(new Vector2f(MainClass.WIDTH - getRadius() - this.getTileSize().getX()/2, this.getPosition().getY()));
        }
        if (this.getCenter().y < getRadius()) {
            this.setPosition(new Vector2f(this.getPosition().getX(), getRadius() - this.getTileSize().getY()/2));
        }
        if (this.getCenter().y >= MainClass.HEIGHT - getRadius()) {
            this.setPosition(new Vector2f(this.getPosition().getX(), MainClass.HEIGHT - getRadius() - this.getTileSize().getY()/2));
        }
    }

    public void move() {
        this.setPosition(this.getPosition().add(this.getSpeed().scale(TimeScale.getInGameTimeScale().getTimeScale())));
        this.tpInBounds();
    }

    public void render(Graphics g, Vector2f facedDirection) {
        if(this.renderer != null) {
            this.renderer.render(g, facedDirection);
        }
        super.render(g);
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
