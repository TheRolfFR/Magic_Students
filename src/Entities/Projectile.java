package Entities;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

public abstract class Projectile extends Entity {
    protected int damage;
    protected Image image;
    protected Vector2f direction;

    Projectile(float x, float y, float maxSpeed, float accelerationRate, Vector2f direction, String imagePath) {
        super(x, y, maxSpeed, accelerationRate);
        this.direction = direction;

        this.image = null;
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

    public void update() {
        this.updateSpeed(this.direction.normalise().scale(this.getAccelerationRate()));
        this.move();
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(image, this.getPosition().getX(), this.getPosition().getY());
    }
}
