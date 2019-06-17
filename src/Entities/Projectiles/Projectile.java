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


public abstract class Projectile extends Entity {
    //A projectil is an object that deal damage to the contact

    protected Image image; //the image of the projectile
    protected Vector2f direction; //direction of the projectile
    protected float opacity;
    protected boolean isDead; //Indication if the projectile still exist
    private Vector2f tileSizeOffset;

    protected ProjectileRenderer renderer;

    public Projectile(float x, float y, int radius, Vector2f direction, Vector2f tileSize) {
        super(x, y, radius);
        this.direction = direction;
        this.opacity = 1f;
        this.image = null;
        super.setSpeed(direction.copy().scale(this.getMaxSpeed()));
        this.isDead = false;

        this.renderer = null;

        this.tileSizeOffset = tileSize.copy().scale(0.5f);

        // for debugging purposes
        this.showDebugRect = true;
    }

    public Projectile(float x, float y, Vector2f direction, String imagePath, int radius) {
        super(x, y, radius);
        this.direction = direction;

        this.image = null;

        this.opacity = 1f;

        this.isDead = false;

        try {
            this.image = new Image(imagePath);
        } catch (SlickException e) {
            e.printStackTrace();
            System.exit(1);
        }

        // for debugging purposes
        this.showDebugRect = true;
    }

    public float getOpacity() {
        return opacity;
    }

    public abstract float getMaxSpeed();

    public abstract int getDamage();

    public Vector2f getDirection() {
        return direction;
    }

    public void collidingAction(LivingBeing opponent) {
        if (super.collidesWith(opponent)) {
            opponent.takeDamage(this.getDamage());
            this.isDead = true;
        }
    }

    public boolean isDead() {
        return this.isDead;
    }

    public static void updateEnemyProjectile(Player target) {
        for (int i = 0; i < Ranged.enemyProjectiles.size(); i++) {
            Projectile p = Ranged.enemyProjectiles.get(i);

            p.move();

            if (p.collidesWith(target) && !target.isDashing()) {
                p.collidingAction(target);
            }

            if (p.isFadeOut() || p.isDead) {
                Ranged.enemyProjectiles.remove(i);
                i--;
            }
        }
    }

    public static void updateAllyProjectiles() {
        Projectile p;
        for (int j = 0; j < Ranged.allyProjectiles.size(); j++) {
            p = Ranged.allyProjectiles.get(j);
            p.move();

            for (Monster enemy : MainClass.getInstance().getEnemies()) {
                checkCollidesProjectile(p, enemy);
            }

            if (p.isFadeOut() || p.isDead()) {
                Ranged.allyProjectiles.remove(j);
                j--;
            }
        }
    }

    private static void checkCollidesProjectile(Projectile p, LivingBeing opponent) {
        if (p.collidesWith(opponent)) {
                p.collidingAction(opponent);
                p.opacity=0;
        }
    }

    public boolean isFadeOut() {
        return this.opacity == 0f;
    }

    public void render(Graphics g) {
        super.render(g);

        Vector2f location = this.getCenter().sub(tileSizeOffset);

        if (this.image != null) {
            g.drawImage(image, location.getX(), location.getY());
        }

        if (this.renderer != null) {
            this.renderer.render(g, (int) location.getX(), (int) location.getY());
        }
    }

    public abstract void fadeOut();

    public void move() {
        super.setCenter(super.getCenter().add(super.getSpeed().scale(TimeScale.getInGameTimeScale().getTimeScale())));

        if (super.getCenter().getX() < super.getRadius() || (super.getCenter().getX() >= MainClass.WIDTH - super.getRadius() || super.getCenter().getY() < super.getRadius() || (super.getCenter().getY() >= MainClass.HEIGHT - super.getRadius()))) {
           this.isDead=true;
        }
    }
}
