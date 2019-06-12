package Entities.LivingBeings.Monsters.Ranged;

import Entities.LivingBeings.LivingBeing;
import Entities.Projectiles.Arrow;
import Entities.Projectiles.Fireball;
import Entities.Projectiles.Snowball;
import Main.MainClass;
import Renderers.LivingBeingRenderer;
import Renderers.SpriteView;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

public class Bowman extends Ranged {

    protected static final int SHOT_DELAY = 120;
    protected int delayCounter;

    public Bowman(float x, float y, int width, int height, float maxSpeed, float accelerationRate, int hpCount, int armor, int damage, int radius){
        super(x, y, width, height, maxSpeed, accelerationRate, hpCount, armor, damage, radius);
        this.delayCounter = 0;

        Vector2f tileSize = new Vector2f(48, 48);
        this.renderer = new LivingBeingRenderer(this, tileSize);

        final String prepath = "img/bowman/";

        final int duration = 1000/8;

        for(String vision : LivingBeingRenderer.ACCEPTED_VISION_DIRECTIONS) {
            this.renderer.addView(vision + "move", new SpriteView(prepath + vision + ".png", tileSize, duration));
        }
    }

    @Override
    public void update(LivingBeing target) {

        if(target.getPosition().distance(this.getPosition()) < 150) {
            this.updateSpeed(target.getPosition().sub(this.getPosition()).normalise().negate().scale(this.getAccelerationRate()));
            this.move();
        }
        else if(this.getSpeed().length()!=0){
            this.updateSpeed(this.getSpeed().normalise().negate().scale(getAccelerationRate()));
            this.move();
        }
        else if(this.delayCounter > SHOT_DELAY && !MainClass.isGamePaused()) {
            attack(target);
        }
        else{
            this.delayCounter = Math.min(this.delayCounter + 1, 121);
        }
    }

    protected void attack(LivingBeing target){

        Vector2f direction = target.getCenter().sub(this.getCenter());
        //enemyProjectiles.add(new Arrow(this.getCenter().add(direction.normalise().scale(this.getRadius()+25)), direction));
        enemyProjectiles.add(new Arrow(direction.copy().normalise().scale(this.getRadius()).add(this.getPosition().sub(direction.copy().normalise().scale(Arrow.getArrowRadius()))), direction));
        enemyProjectiles.get(enemyProjectiles.size()-1).setShowDebugRect(true);

        this.delayCounter = 0;
    }

    public void render(Graphics g) {
        super.render(g);
    }
}
