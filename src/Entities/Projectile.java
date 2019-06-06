package Entities;

import Main.MainClass;
import Main.SceneRenderer;
import Renderer.ProjectileRenderer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

import java.security.KeyStore;

public abstract class Projectile extends Entity {
    private int damage;
    protected Image image;
    protected Vector2f direction;
    protected float opacity;
    private boolean isDead;

    protected ProjectileRenderer renderer;

    public float getOpacity() {
        return opacity;
    }

    public Projectile(float x, float y, float maxSpeed, float accelerationRate, int radius, Vector2f direction) {
        super(x, y, maxSpeed, accelerationRate, radius);
        this.damage = 25;
        this.direction = direction;
        this.opacity = 1f;

        this.image = null;

        this.isDead = false;

        this.renderer = null;
    }

    Projectile(float x, float y, float maxSpeed, float accelerationRate, Vector2f direction, String imagePath, int radius) {
        super(x, y, maxSpeed, accelerationRate, radius);
        this.direction = direction;

        this.damage = 25;

        this.image = null;

        this.opacity = 1f;

        this.isDead = false;

        try {
            this.image = new Image(imagePath);
        } catch (SlickException e) {
            e.printStackTrace();
            System.exit(1);
        }
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

    public Shape getBounds(){
        return new Circle(position.x + getRadius(),position.y + getRadius(), getRadius());
    }

    public void update() {
    }

    public static void updateEnemyProjectile(LivingBeing target){
        for (int i = 0; i < Ranged.enemyProjectiles.size(); i++) {
            Projectile p = Ranged.enemyProjectiles.get(i);

            p.updateSpeed(p.direction.normalise().scale(p.getAccelerationRate()));
            p.move();

            if(p.collidesWith(target)){
                p.collidingAction(target);
            }

            if (p.isFadeOut() || p.isDead()) {
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

            for(Monster enemy : MainClass.getInstance().getEnemies()){
                checkCollidesProjectile(enemy);
            }

            if (p.isFadeOut()) {
                Ranged.allyProjectiles.remove(j);
                j--;
            }
        }
    }

    private static void checkCollidesProjectile(LivingBeing opponent){
        for(int i = 0; i< Ranged.allyProjectiles.size(); i++){
            if(Ranged.allyProjectiles.get(i).collidesWith(opponent)){
                Ranged.allyProjectiles.get(i).collidingAction(opponent);
                Ranged.allyProjectiles.remove(Ranged.allyProjectiles.get(i));
            }
        }
    }

    public boolean isFadeOut() {
        return this.opacity == 0f;
    }

    public void render(Graphics g) {
        super.render(g);

        if(this.image != null) {
            g.drawImage(image, this.getPosition().getX(), this.getPosition().getY());
        }
    }

    public abstract void fadeOut();

    public void move() {
        this.position.add(this.speed.scale(MainClass.getInGameTimeScale().getTimeScale()));

        if (this.position.x < 0 || (this.position.x + this.tileSize.getX() >= MainClass.WIDTH) || this.position.y < 0 || (this.position.y + this.tileSize.getY() >= MainClass.HEIGHT)) {
           this.isDead=true;
        }
    }
}
