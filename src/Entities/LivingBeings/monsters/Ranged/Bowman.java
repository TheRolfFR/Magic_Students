package Entities.LivingBeings.monsters.Ranged;

import Entities.LivingBeings.LivingBeing;
import Entities.LivingBeings.monsters.Ranged.Ranged;
import Entities.Projectiles.Arrow;
import Entities.Projectiles.Fireball;
import Entities.Projectiles.Snowball;
import Main.MainClass;
import Renderer.LivingBeingRenderer;
import Renderer.SpriteView;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

public class Bowman extends Ranged {

    private static final int SHOT_DELAY = 120;
    private int delayCounter;

    public Bowman(float x, float y, int width, int height, float maxSpeed, float accelerationRate, int hpCount, int armor, int damage, int radius){
        super(x, y, width, height, maxSpeed, accelerationRate, hpCount, armor, damage, radius);
        this.delayCounter = 0;

        Vector2f tileSize = new Vector2f(48, 48);
        this.renderer = new LivingBeingRenderer(this, tileSize);

        final String prepath = "img/bowman/";

        final int duration = 1000/8;

        this.renderer.setTopView(new SpriteView(prepath + "top.png", tileSize, duration));
        this.renderer.setBottomView(new SpriteView(prepath + "bottom.png", tileSize, duration));
        this.renderer.setLeftView(new SpriteView(prepath + "left.png", tileSize, duration));
        this.renderer.setRightView(new SpriteView(prepath + "right.png", tileSize, duration));
    }

    @Override
    public void update(LivingBeing target) {
        this.delayCounter = Math.min(this.delayCounter + 1, 121);
        if(target.getPosition().distance(this.getPosition()) < 150) {
            this.updateSpeed(target.getPosition().sub(this.getPosition()).normalise().negate().scale(this.getAccelerationRate()));
            this.move();
        }
        else if(this.getSpeed().length()!=0){
            this.updateSpeed(this.getSpeed().normalise().negate().scale(getAccelerationRate()));
            this.move();
        }

        if(this.delayCounter > SHOT_DELAY && !MainClass.isGamePaused()) {
            attack(target);
        }
    }

    private void attack(LivingBeing target){
        Vector2f futureTargetPosition = target.getCenter().add(target.getSpeed().copy().scale(target.getCenter().distance(this.getCenter())/ Snowball.MAX_SPEED));

        if(futureTargetPosition.getY() > MainClass.HEIGHT){
            futureTargetPosition.set(futureTargetPosition.getX(), MainClass.HEIGHT - target.getRadius()/4);
        }
        else if(futureTargetPosition.getY() < 0){
            futureTargetPosition.set(futureTargetPosition.getX(), 0);
        }
        if(futureTargetPosition.getX() > MainClass.WIDTH){
            futureTargetPosition.set(MainClass.WIDTH - target.getRadius()/4, futureTargetPosition.getY());
        }
        else if(futureTargetPosition.getX() < 0){
            futureTargetPosition.set(0, futureTargetPosition.getY());
        }

        Vector2f direction = futureTargetPosition.sub(this.getPosition());

        //target the center of the opponent, not the top left corner
        direction.set(direction.getX() + target.getRadius()/2f, direction.getY() + target.getRadius()/2f);

        //this.monsterProjectiles.add(new Snowball(this.getPosition(), direction));
        enemyProjectiles.add(new Arrow(direction.copy().normalise().scale(this.getRadius()).add(new Vector2f(this.getCenter().x - Fireball.getFireballRadius(), this.getCenter().y - Fireball.getFireballRadius())), direction));
        //enemyProjectiles.get(enemyProjectiles.size()-1).setShowDebugRect(true);

        this.delayCounter = 0;
    }

    public void render(Graphics g) {
        super.render(g);
    }
}
