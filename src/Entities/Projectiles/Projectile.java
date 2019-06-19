package Entities.Projectiles;

import Entities.Entity;
import Entities.LivingBeings.LivingBeing;
import Entities.LivingBeings.Monsters.Monster;
import Entities.LivingBeings.Monsters.Ranged.Ranged;
import Entities.LivingBeings.Player;
import Main.MainClass;
import Main.TimeScale;
import Renderers.ProjectileRenderer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

/**
 * A projectile is an {@link Entity} wihout life summoned by a {@link LivingBeing}
 */
public abstract class Projectile extends Entity {
    //A projectil is an object that deal damage to the contact

    protected Image image; //the image of the projectile
    protected Vector2f direction; //direction of the projectile
    protected float opacity;
    protected boolean isDead; //Indication if the projectile still exist
    private Vector2f tileSizeOffset;

    /**
     * The projectile renderer
     */
    protected ProjectileRenderer renderer;

    /**
     * Constructor
     * @param x the first coordonate of the projectile
     * @param y the second coordonate of the projectile
     * @param radius the hitbox radius of the projectile
     * @param direction the direction of the porjectile
     * @param tileSize the size of the image
     */
    public Projectile(float x, float y, int radius, Vector2f direction, Vector2f tileSize) {
        super(x, y, radius); //creat an entity

        this.direction = direction;
        this.opacity = 1f;
        this.image = null;
        this.isDead = false;

        super.setSpeed(direction.copy().scale(this.getMaxSpeed())); //projectiles are create at max speed

        this.renderer = null;

        this.tileSizeOffset = tileSize.copy().scale(0.5f);
    }

    /**
     * Getter for the opacity
     * @return the opacity of the projectile
     */
    public float getOpacity() {
        return opacity;
    }

    /**
     * Getter for the speed
     * @return the speed od the object
     */
    public abstract float getMaxSpeed();

    /**
     * Getter for the damage
     * @return the damage that inflict the object
     */
    public abstract int getDamage();

    /**
     * Getter for the direction
     * @return the direction of the object
     */
    public Vector2f getDirection() {
        return direction.copy();
    }

    /**
     * Inflict damage to the target on the contact
     * @param target the potential LivingBeing that it collides with
     */
    public void collidingAction(LivingBeing target) {
        if (super.collidesWith(target)) { //if collides with the target
            target.takeDamage(this.getDamage()); //deal damage to the target
            this.isDead = true; //the porjectile won't exist anymore on the next frame
        }
    }

    /**
     * Indicate if the porjectile won't exist on the next frame
     * @return if the projectile is "dead"
     */
    public boolean isDead() {
        return this.isDead;
    }

    /**
     * Move every enemy projectile and check if they collide with the player
     * @param target the player
     */
    public static void updateEnemyProjectile(Player target) {
        for (int i = 0; i < Ranged.enemyProjectiles.size(); i++) { //for each projectile
            Projectile p = Ranged.enemyProjectiles.get(i);

            p.move(); //Move the current projectile

            if (p.collidesWith(target) && !target.isDashing()) { //If collides with the player and the player isn't dashing
                p.collidingAction(target); //deal damage to the player
            }

            if (p.isFadeOut() || p.isDead) { //if the porjectile is dead
                Ranged.enemyProjectiles.remove(i); //remove the projectile from the list of projectile
                i--;
            }
        }
    }

    /**
     * Move every ally porjectile and check if thay collide with any monster
     */
    public static void updateAllyProjectiles() {
        Projectile p;
        for (int j = 0; j < Ranged.allyProjectiles.size(); j++) { //for each porjectile
            p = Ranged.allyProjectiles.get(j);

            p.move(); //Move the projectile

            for (Monster enemy : MainClass.getInstance().getEnemies()) { //for each enemy
                checkCollidesProjectile(p, enemy); //Check if the projectile collides with the current ennemy
            }

            if (p.isFadeOut() || p.isDead()) { //if the projectile is dead
                Ranged.allyProjectiles.remove(j); //remove the porjectile from the list
                j--;
            }
        }
    }

    /**
     * Check if the porjectile collides with the being
     * @param p the projectile
     * @param opponent the being
     */
    private static void checkCollidesProjectile(Projectile p, LivingBeing opponent) {
        if (p.collidesWith(opponent)) { //If they collide
                p.collidingAction(opponent); //deal damage to the being
                p.opacity=0; //Make the projectil disapear
        }
    }

    /**
     * Indicate if the projectile has fade out
     * @return if the projectile has fade out
     */
    public boolean isFadeOut() {
        return this.opacity == 0f;
    }

    /**
     * In game rendering
     * @param g the graphics to draw on
     * @see Projectile#render(Graphics)
     * @see ProjectileRenderer#render(Graphics, int, int)
     */
    public void render(Graphics g) {
        super.render(g); // render debug rect

        Vector2f location = this.getCenter().sub(tileSizeOffset); // calculate

        if (this.image != null) {
            g.drawImage(image, location.getX(), location.getY());
        }

        if (this.renderer != null) {
            this.renderer.render(g, (int) location.getX(), (int) location.getY());
        }
    }

    /**
     * Make the projectile fade out
     */
    public abstract void fadeOut();

    /**
     * move the porjectile
     */
    public void move() {
        super.setCenter(super.getCenter().add(super.getSpeed().scale(TimeScale.getInGameTimeScale().getTimeScale()))); //update the position

        if (super.getCenter().getX() < super.getRadius() || (super.getCenter().getX() >= MainClass.WIDTH - super.getRadius() || super.getCenter().getY() < super.getRadius() || (super.getCenter().getY() >= MainClass.HEIGHT - super.getRadius()))) { //if the projectile is out of bounds
           this.isDead=true; //the projectile is dead
        }
    }
}
