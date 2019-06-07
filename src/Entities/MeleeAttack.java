package Entities;

import org.newdawn.slick.geom.Vector2f;

public class MeleeAttack extends Projectile {

    private static final int RADIUS = 15;

    public MeleeAttack(Vector2f position, Vector2f direction) {
        super(position.getX(), position.getY(), 0, 0, RADIUS, direction);
    }

    static int getMeleeRadius(){return RADIUS;} //package private

    public void update(){fadeOut();}

    @Override
    public void fadeOut() {
        if(this.renderer != null)
            this.renderer.setOpacity(0);
    }
}
