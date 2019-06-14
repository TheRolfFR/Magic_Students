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
    public static int damage = 25;
    protected Image image;
    protected Vector2f direction;
    protected float opacity;
    protected boolean isDead;
    private Vector2f tilesetOffset;

    protected ProjectileRenderer renderer;

    public float getOpacity() {
        return opacity;
    }

    public Projectile(float x, float y, float maxSpeed, float accelerationRate, int radius, Vector2f direction, Vector2f tileSize) {
        super(x, y, maxSpeed, accelerationRate, radius);
        this.direction = direction;
        this.opacity = 1f;

        this.image = null;

        this.isDead = false;

        this.renderer = null;

        this.tilesetOffset = tileSize.copy().scale(0.5f);

        // for debugging purposes
        this.showDebugRect = true;
    }

    public Projectile(float x, float y, float maxSpeed, float accelerationRate, Vector2f direction, String imagePath, int radius) {
        super(x, y, maxSpeed, accelerationRate, radius);
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

    @Override
    protected void updateSpeed(Vector2f acceleration) {
        this.setSpeed(this.getSpeed().add(acceleration));

        if (this.getSpeed().length() > this.MAX_SPEED * TimeScale.getInGameTimeScale().getTimeScale()) {
            this.setSpeed(this.getSpeed().normalise().scale(this.MAX_SPEED * TimeScale.getInGameTimeScale().getTimeScale()));
        }
    }

    public static void updateEnemyProjectile(Player target){
        for (int i = 0; i < Ranged.enemyProjectiles.size(); i++) {
            Projectile p = Ranged.enemyProjectiles.get(i);

            p.updateSpeed(p.direction.copy().normalise().scale(p.getAccelerationRate()));
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
            p.updateSpeed(p.direction.copy().normalise().scale(p.getAccelerationRate()));
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

        Vector2f location = this.getPosition().sub(tilesetOffset);

        if (this.image != null) {
            g.drawImage(image, location.getX(), location.getY());
        }

        if (this.renderer != null) {
            this.renderer.render(g, (int) location.getX(), (int) location.getY());
        }
    }

    public abstract void fadeOut();

    public void move() {
        this.setPosition(this.getPosition().add(this.getSpeed().scale(TimeScale.getInGameTimeScale().getTimeScale())));

        if (this.getPosition().getX() < 0 || (this.getPosition().getX() + this.getRadius()>= MainClass.WIDTH) || this.getPosition().getY() < 0 || (this.getPosition().getY() + this.getRadius()>= MainClass.HEIGHT)) {
           this.isDead=true;
        }
    }
}
