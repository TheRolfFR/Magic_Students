package Entities;

import Main.SceneRenderer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

public class Snowball extends Projectile {
    public Snowball(float x, float y, float maxSpeed, float accelerationRate, String imagePath, Vector2f direction) {
        super(x, y, maxSpeed, accelerationRate, direction, imagePath);

        this.updateSpeed(direction.normalise().scale(this.getAccelerationRate()));
    }

    @Override
    public Shape getBounds(){
        return new Circle(position.x + this.getWidth()/2, position.y + this.getHeight()/2, this.getWidth()/2);
    }

    @Override
    protected int getWidth() {
        return this.image.getWidth();
    }

    @Override
    protected int getHeight() {
        return this.image.getHeight();
    }

    public void render(Graphics g) {
        super.render(g);
        if(showDebugRect) {
            g.draw(this.getBounds());
        }
    }
}
