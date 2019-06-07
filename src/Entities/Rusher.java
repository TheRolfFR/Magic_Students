package Entities;

import Renderer.LivingBeingRenderer;
import Renderer.SpriteView;
import org.newdawn.slick.geom.Vector2f;

public class Rusher extends Melee {
    public Rusher(float x, float y, int width, int height, float maxSpeed, float accelerationRate, int hpCount, float armor, int damage, int radius){
        super(x, y, width, height, maxSpeed, accelerationRate, hpCount, armor, damage, radius);

        Vector2f tileSize = new Vector2f(48, 48);
        this.renderer = new LivingBeingRenderer(this, tileSize);

        String prepath = "img/rusher/";

        this.renderer.setTopView(new SpriteView(prepath + "top.png", tileSize, 1000/2));
        this.renderer.setBottomView(new SpriteView(prepath + "bottom.png", tileSize, 1000/2));
        this.renderer.setLeftView(new SpriteView(prepath + "left.png", tileSize, 1000/2));
        this.renderer.setRightView(new SpriteView(prepath + "right.png", tileSize, 1000/2));
    }

    public void update(LivingBeing target){
        this.updateSpeed(target.position.copy().sub(this.position).normalise().scale(this.getAccelerationRate()));

        this.move();
        this.doDamage(target);
    }
}
