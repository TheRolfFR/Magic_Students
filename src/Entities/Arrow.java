package Entities;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

public class Arrow extends Projectile {
    protected int width;
    protected int height;

    public Shape getBounds(){
        Vector2f arrowHeadposition = position.copy().add(this.speed.copy().normalise().scale(this.height));

        return new Rectangle(arrowHeadposition.x, arrowHeadposition.y,1,1);
    }

    @Override
    protected int getWidth() { return this.width; }
    @Override
    protected int getHeight() { return this.height; }
}
