package Entities;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;
import java.lang.Math;

public class Arrow extends Projectile {
    protected int width;
    protected int hitBoxWidth;
    protected int height;

    public Shape getBounds() {
        Vector2f arrowHeadPosition = position.copy().add(this.speed.copy().normalise().scale(this.height - hitBoxWidth)).add(new Vector2f(-1, -1).scale(hitBoxWidth * (float) Math.sqrt(2)));
        return new Rectangle(arrowHeadPosition.x, arrowHeadPosition.y, hitBoxWidth, hitBoxWidth);
    }

    @Override
    protected int getWidth() { return this.width; }
    @Override
    protected int getHeight(){ return this.height; }
}
