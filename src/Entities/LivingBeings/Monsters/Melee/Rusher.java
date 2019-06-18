package Entities.LivingBeings.Monsters.Melee;

import Entities.LivingBeings.LivingBeing;
import Renderers.LivingBeingRenderer;
import Renderers.SpriteView;
import org.newdawn.slick.geom.Vector2f;

public class Rusher extends Melee {
    public Rusher(float x, float y, int width, int height, int hpCount, int armor, int damage, int radius) {
        super(x, y, width, height, hpCount, armor, damage, radius);

        Vector2f tileSize = new Vector2f(48, 48);
        this.renderer = new LivingBeingRenderer(this, tileSize);

        final String prepath = "img/rusher/";

        final int duration = 1000/8;

        for (String vision : LivingBeingRenderer.ACCEPTED_VISION_DIRECTIONS) {
            this.renderer.addView(vision + "Move", new SpriteView(prepath + vision + ".png", tileSize, duration));
        }
    }

    public void update(LivingBeing target) {
        this.updateSpeed(target.getCenter().sub(this.getCenter()).normalise().scale(this.getAccelerationRate()));

        this.move();
    }

    @Override
    public float getMaxSpeed() {
        return 0;
    }

    @Override
    public float getAccelerationRate() {
        return 0;
    }
}
