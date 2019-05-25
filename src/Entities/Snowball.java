package Entities;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

public class Snowball extends Projectile {

    private Image image;

    public Snowball(float x, float y, float maxSpeed, float accelerationRate, String imagePath, Vector2f speed) {
        super(x, y, maxSpeed, accelerationRate);
        this.image = null;
        try {
            this.image = new Image(imagePath);
        } catch (SlickException e) {
            e.printStackTrace();
            System.exit(1);
        }

        this.updateSpeed(speed.scale(this.getAccelerationRate()));
    }

    @Override
    protected int getWidth() {
        return this.image.getWidth();
    }

    @Override
    protected int getHeight() {
        return this.image.getHeight();
    }

    @Override
    public Shape getBounds() {
        return new Rectangle((int) this.getPosition().getX(), (int) this.getPosition().getY(), this.getWidth(), this.getHeight());
    }

    public void render(Graphics g) {
        g.drawImage(image, this.getPosition().getX(), this.getPosition().getY());
    }
}
