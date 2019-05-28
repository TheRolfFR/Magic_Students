package Entities;

import Main.SceneRenderer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

public abstract class Projectile extends Entity {
    protected int damage;
    protected Image image;
    protected Vector2f direction;
    protected float opacity;

    public float getOpacity() {
        return opacity;
    }

    Projectile(float x, float y, float maxSpeed, float accelerationRate, Vector2f direction, String imagePath, int radius) {
        super(x, y, maxSpeed, accelerationRate, radius);
        this.direction = direction;

        this.image = null;

        this.opacity = 1f;

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
        if (collides(opponent)){
            opponent.takeDamage(damage);
            //kill();
        }
    }

    public Shape getBounds(){
        return new Circle(position.x,position.y,radius);
    }

    public void update() {
        this.updateSpeed(this.direction.normalise().scale(this.getAccelerationRate()));
        this.move();

        if(!SceneRenderer.inRoomLimits(this.getBounds())) {
            this.fadeOut();
            this.opacity = Math.max(0f, this.opacity - 0.125f);
        }
    }

    public boolean isFadeOut() {
        return this.opacity == 0f;
    }

    public void render(Graphics g) {
        super.render(g);
        g.drawImage(image, this.getPosition().getX(), this.getPosition().getY());
    }

    public abstract void fadeOut();
}
