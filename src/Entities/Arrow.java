package Entities;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

public class Arrow extends Projectile {
    protected int lentgh;

    @Override
    public Shape getBounds(){
        Vector2f arrowHeadposition = position.add(speed.scale(lentgh/(2*this.speed.length())));

        return new Rectangle(arrowHeadposition.x,arrowHeadposition.y,1,1);
    }
}
