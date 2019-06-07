package Entities;

import Main.MainClass;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

import java.util.ArrayList;

public class Bowman extends Ranged {

    private static final int SHOT_DELAY = 120;
    private int delayCounter;

    public Bowman(float x, float y, int width, int height, float maxSpeed, float accelerationRate, int hpCount, int armor, int damage, int radius){
        super(x, y, width, height, maxSpeed, accelerationRate, hpCount, armor, damage, radius);
        this.delayCounter = 0;
    }

    @Override
    public void update(LivingBeing target) {
        this.delayCounter = Math.min(this.delayCounter+1, 121);
        if(target.position.distance(this.position)<150) {
            this.updateSpeed(target.position.copy().sub(this.position).normalise().negate().scale(this.getAccelerationRate()));
            this.move();
        }
        else if(this.speed.length()!=0){
            this.updateSpeed(this.speed.copy().normalise().negate().scale(getAccelerationRate()));
            this.move();
        }

        if(this.delayCounter > SHOT_DELAY && !MainClass.isGamePaused()) {
            attack(target);
        }

        if(this.collidesWith(target)){
            this.collidingAction(target);
        }
    }

    public void attack(LivingBeing target){
        Vector2f futureTargetPosition = target.getCenter().copy().add(target.speed.copy().scale(target.getCenter().distance(this.getCenter())/Snowball.MAX_SPEED));

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

        Vector2f direction = futureTargetPosition.sub(this.position);

        //target the center of the opponent, not the top left corner
        direction.set(direction.getX() + target.getRadius()/4, direction.getY() + target.getRadius()/4);

        //this.monsterProjectiles.add(new Snowball(this.getPosition(), direction));
        enemyProjectiles.add(new Fireball(direction.copy().normalise().scale(this.getRadius()).add(new Vector2f(this.getCenter().x - Fireball.getFireballRadius(), this.getCenter().y - Fireball.getFireballRadius())), direction));
        //enemyProjectiles.get(enemyProjectiles.size()-1).setShowDebugRect(true);

        this.delayCounter = 0;
    }

    public void render(Graphics g) {
        super.render(g);

        for(Projectile p : enemyProjectiles) {
            p.render(g);
        }
    }
}
