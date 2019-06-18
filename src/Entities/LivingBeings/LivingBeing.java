package Entities.LivingBeings;

import Entities.Entity;
import Entities.LivingBeings.Monsters.Monster;
import Listeners.LivingBeingHealthListener;
import Listeners.LivingBeingMoveListener;
import Main.GameStats;
import Main.MainClass;
import Main.TimeScale;
import Renderers.LivingBeingRenderer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.ceil;

/**
 * LivingBeing are entity that have heathPoint
 */
public abstract class LivingBeing extends Entity implements Comparable {
    private int currentHealthPoints;
    private int maxHealthPoints;
    private int armorPoints;

    private ArrayList<LivingBeingHealthListener> livingBeingHealthListeners;
    private ArrayList<LivingBeingMoveListener> livingBeingMoveListeners;

    public static ArrayList<LivingBeing> livingBeings = new ArrayList<>();

    protected LivingBeingRenderer renderer;

    /**
     * Constructor
     * @param x x initial position of the living being
     * @param y y initial position of the living being
     * @param height the height of the living being
     * @param width the width of the living being
     * @param maxHealthPoints maximum health points of the living being
     * @param armorPoints armor points of the living being
     * @param radius the collision radius
     */
    public LivingBeing(float x, float y, int width, int height, int maxHealthPoints, int armorPoints, int radius) {
        super(x, y, width, height, radius);
        this.currentHealthPoints = maxHealthPoints;
        this.maxHealthPoints = maxHealthPoints;
        this.armorPoints = armorPoints;
        this.livingBeingHealthListeners = new ArrayList<>();
        this.livingBeingMoveListeners = new ArrayList<>();

        livingBeings.add(this);
    }

    /**
     * Constructor
     * @param x the initial the coordonate of the being
     * @param y the initial second coordonate of the being
     * @param maxHealthPoints the maximum amout of hp of this being
     * @param armorPoints the quantity of armor of this being
     * @param radius the hitbox radius of this being
     */
    public LivingBeing(float x, float y, int maxHealthPoints, int armorPoints, int radius) {
        super(x, y, radius);
        this.currentHealthPoints = maxHealthPoints;
        this.maxHealthPoints = maxHealthPoints;
        this.armorPoints = armorPoints;

        this.livingBeingHealthListeners = new ArrayList<>();
        this.livingBeingMoveListeners = new ArrayList<>();

        livingBeings.add(this);
    }

    /**
     * Getter for the maximum speed
     * @return the norm of the maximum speed of this being
     */
    public abstract float getMaxSpeed();

    /**
     * Getter for the acceleration
     * @return the norm of the acceleration for this being
     */
    public abstract float getAccelerationRate();

    /**
     * Heal this being
     * @param amountOfHealing amount of heal provides
     */
    public void heal(int amountOfHealing) {
        this.currentHealthPoints = this.currentHealthPoints + amountOfHealing; //heal

        if (this.currentHealthPoints > this.maxHealthPoints) { //if it has more hp than the maximum allowed
            this.currentHealthPoints = this.maxHealthPoints; //set hp to the maximum
        }

        // update bar on heal too
        for (LivingBeingHealthListener listener : livingBeingHealthListeners) {
            listener.onUpdate(this, amountOfHealing);
            listener.onHeal(this, amountOfHealing);
        }
    }

    /**
     * Increase the maximum of hp
     * @param buffAmount the increase in hp
     */
    public void buffHP(int buffAmount) {
        this.maxHealthPoints = this.maxHealthPoints + buffAmount;
        this.heal(buffAmount);
    }

    /**
     * Increase the armor
     * @param buffAmount the increase in armor
     */
    public void buffArmor(int buffAmount) {
        this.armorPoints = this.armorPoints + buffAmount;
    }

    /**
     * Updates speed with an acceleration
     * @param acceleration the given acceleration
     */
    protected void updateSpeed(Vector2f acceleration) {
        super.setSpeed(super.getSpeed().add(acceleration));

        if (super.getSpeed().length() > this.getMaxSpeed() * TimeScale.getInGameTimeScale().getTimeScale()) { //if the speed exceed the maximum allowed
            super.setSpeed(super.getSpeed().normalise().scale(this.getMaxSpeed() * TimeScale.getInGameTimeScale().getTimeScale())); //decrease to the maximum
        }

        if (super.getSpeed().getX() > -LivingBeingConstants.MINIMUM_SPEED  && super.getSpeed().getX() < LivingBeingConstants.MINIMUM_SPEED) { //if the speed isn't relevant
            super.setSpeed(0, super.getSpeed().getY()); //set the irrelevant coordonate to 0
        }

        if (super.getSpeed().getY() > -LivingBeingConstants.MINIMUM_SPEED && super.getSpeed().getY() < LivingBeingConstants.MINIMUM_SPEED) {
            super.setSpeed(super.getSpeed().getX(), 0);
        }
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

    public void addHealthListener(LivingBeingHealthListener listener) {
        this.livingBeingHealthListeners.add(listener);
    }

    public void addMoveListener(LivingBeingMoveListener listener) {
        this.livingBeingMoveListeners.add(listener);
    }

    /**
     * Getter for the armor
     * @return the armor of this being
     */
    public int getArmorPoints() {
        return this.armorPoints;
    }

    /**
     * Returns the current health points
     * @return the current health points
     */
    public int getCurrentHealthPoints() {
        return this.currentHealthPoints;
    }

    /**
     * Returns the maximum health points
     * @return the maximum health points
     */
    public int getMaxHealthPoints() {
        return maxHealthPoints;
    }

    /**
     * allows the living being to take damage
     * @param damage damage value inflicted
     */
    public void takeDamage(int damage) {
        int tmp = this.currentHealthPoints;
        this.currentHealthPoints = Math.max(0, this.currentHealthPoints - Math.max(1, damage - this.armorPoints));

        // launching listeners
        for (LivingBeingHealthListener listener : this.livingBeingHealthListeners) {
            listener.onUpdate(this, tmp-currentHealthPoints);
            listener.onHurt(this, tmp-currentHealthPoints);
        }

        if(this instanceof Monster) {
            GameStats.getInstance().onAttack(tmp-currentHealthPoints);
        }

        if(this.currentHealthPoints == 0) {
            for(LivingBeingHealthListener listener : this.livingBeingHealthListeners) {
                listener.onDeath(this);
            }
        }
    }

    /**
     * Indicate if a being is alive
     * @return true if hp are below 0, fale otherwise
     */
    public boolean isDead() {
        return this.currentHealthPoints <= 0;
    }

    /**
     * Separate two being that are superimposed and check if the superimposed with another after the separation
     * @param pusher the being that moved
     * @param percuted the being that is in the destination of the pusher
     * @param level the depth of our recursivity
     */
    private void solveCollision(LivingBeing pusher, LivingBeing percuted, int level) {
        if (level <= MainClass.getInstance().getEnemies().size()) { //limit the depth of our recursitvity

            percuted.collidingAction(pusher); //push the percuted out of the pusher

            if (percuted.collidesWith(MainClass.getInstance().getPlayer())) { //if the percuted now collides with the player
                solveCollision(percuted, MainClass.getInstance().getPlayer(), level + 1); //create a new level
            }
            for (Monster m: MainClass.getInstance().getEnemies()) { //for each monster
                if (percuted.collidesWith(m)) { //if the percuted collides with the current monster
                    solveCollision(percuted, m, level + 1); //create a new level
                }
            }
        }
    }

    /**
     * Separate this being and an other one
     * @param opponent the being that pushed this one
     */
    public void collidingAction(LivingBeing opponent) {
        if (this.collidesWith(opponent)) { //if they collides
            this.tpOutOf(opponent); //this being get out of the pusher
            opponent.tpOutOf(this); //the pusher get out of this being (this do something only if this being was pushed out of bounds by the pusher)
        }
    }

    /**
     * Check if anything collides and solve the collision
     */
    public void checkCollision() {
        if (this.collidesWith(MainClass.getInstance().getPlayer())) {
            solveCollision(this, MainClass.getInstance().getPlayer(), 1);
        }
        for (Monster m: MainClass.getInstance().getEnemies()) {
            if (this.collidesWith(m)) {
                solveCollision(this, m, 1);
            }
        }
    }

    /**
     * Get this out od the being that pushed it
     * @param opponent the being that pushed this
     */
    private void tpOutOf(LivingBeing opponent) {
        Vector2f diff = super.getCenter().sub(opponent.getCenter()).normalise().scale((float) ceil(super.getRadius() + opponent.getRadius() - opponent.getCenter().sub(super.getCenter()).length())); //compute the necessary movement to get out of the pusher
        super.setCenter(super.getCenter().add(diff)); //going out of the pusher
        this.tpInBounds(); //Check if the being in still in bounds
    }

    /**
     * get this being in bounds if it as been pushed out
     */
    private void tpInBounds() {
        if (super.getCenter().x < super.getRadius()) {
            super.setCenter(new Vector2f(super.getRadius(), super.getCenter().getY()));
        }
        if (super.getCenter().x >= MainClass.WIDTH - super.getRadius()) {
            super.setCenter(new Vector2f(MainClass.WIDTH - super.getRadius(), super.getCenter().getY()));
        }
        if (super.getCenter().y < super.getRadius()) {
            super.setCenter(new Vector2f(super.getCenter().getX(), super.getRadius()));
        }
        if (super.getCenter().y >= MainClass.HEIGHT - super.getRadius()) {
            super.setCenter(new Vector2f(super.getCenter().getX(), MainClass.HEIGHT - super.getRadius()));
        }
    }

    /**
     * Move this being
     */
    public void move() {
        super.setCenter(super.getCenter().add(super.getSpeed().scale(TimeScale.getInGameTimeScale().getTimeScale())));
        this.tpInBounds();

        for (LivingBeingMoveListener listener : this.livingBeingMoveListeners) {
            listener.onMove(this);
        }
    }

    public void render(Graphics g, Vector2f facedDirection) {
        if (this.renderer != null) {
            this.renderer.render(g, facedDirection);
        }
        super.render(g);
    }

    /**
     * Compare Being thanks to their position on the y-axis
     * @param o the other object
     * @return true if this being has a greater y-coordonate, false otherwise
     */
    @Override
    public int compareTo(Object o) {
        if (this.getCenter().getY() > ((LivingBeing) o).getCenter().getY()) {
            return 1;
        } else {
            return 0;
        }
    }
}
