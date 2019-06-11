package Entities.Projectiles;

import Entities.Entity;
import Entities.LivingBeings.LivingBeing;
import Entities.LivingBeings.Player;
import Entities.LivingBeings.Monsters.Monster;
import Entities.LivingBeings.Monsters.Ranged.Ranged;
import Main.MainClass;
import Main.TimeScale;
import Renderers.ProjectileRenderer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;


public abstract class Projectile extends Entity {
    public static int damage;
    protected Image image;
    protected Vector2f direction;
    protected float opacity;
    protected boolean isDead;

    protected ProjectileRenderer renderer;

    public float getOpacity() {
        return opacity;
    }

    public Projectile(float x, float y, float maxSpeed, float accelerationRate, int radius, Vector2f direction) {
        super(x, y, maxSpeed, accelerationRate, radius);
        damage = 25;
        this.direction = direction;
        this.opacity = 1f;

        this.image = null;

        this.isDead = false;

        this.renderer = null;
        // for debugging purposes
        this.showDebugRect = true;
    }

    public Projectile(float x, float y, float maxSpeed, float accelerationRate, Vector2f direction, String imagePath, int radius) {
        super(x, y, maxSpeed, accelerationRate, radius);
        this.direction = direction;

        damage = 25;

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

    public int getDamage(){
        return damage;
    }

    public void collidingAction(LivingBeing opponent) {
        if (collidesWith(opponent)){
            opponent.takeDamage(damage);
            this.isDead = true;
        }
    }

    public boolean isDead(){
        return this.isDead;
    }

    public void update() {}

    public static void updateEnemyProjectile(Player target){
        for (int i = 0; i < Ranged.enemyProjectiles.size(); i++) {
            Projectile p = Ranged.enemyProjectiles.get(i);

            p.updateSpeed(p.direction.normalise().scale(p.getAccelerationRate()));
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

    public static void updateAllyProjectiles(){
        Projectile p;
        for (int j = 0; j < Ranged.allyProjectiles.size(); j++) {
            p = Ranged.allyProjectiles.get(j);
            p.updateSpeed(p.direction.normalise().scale(p.getAccelerationRate()));
            p.move();

            p.update();

            for(Monster enemy : MainClass.getInstance().getEnemies()){
                checkCollidesProjectile(p,enemy);
            }

            if (p.isFadeOut() || p.isDead()) {
                Ranged.allyProjectiles.remove(j);
                j--;
            }
        }
    }

    private static void checkCollidesProjectile(Projectile p, LivingBeing opponent){
        if(p.collidesWith(opponent)){
                p.collidingAction(opponent);
                p.opacity=0;
        }
    }

    public boolean isFadeOut() {
        return this.opacity == 0f;
    }

    public void render(Graphics g) {
        super.render(g);

        if (this.image != null) {
            g.drawImage(image, this.getPosition().getX(), this.getPosition().getY());
        }

        if (this.renderer != null) {
            this.renderer.render(g, (int) this.getPosition().getX(), (int) this.getPosition().getY());
        }
    }

    public abstract void fadeOut();

    public void move() {
        this.setPosition(this.getPosition().add(this.getSpeed().scale(TimeScale.getInGameTimeScale().getTimeScale())));

        if (this.getCenter().getX() < 0 || (this.getCenter().getX() + this.getRadius()>= MainClass.WIDTH) || this.getCenter().getY() < 0 || (this.getCenter().getY() + this.getRadius()>= MainClass.HEIGHT)) {
           this.isDead=true;
        }
    }
}
