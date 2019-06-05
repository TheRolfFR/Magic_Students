package Entities;

import Main.MainClass;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

import java.util.ArrayList;

public class Bowman extends Ranged {

    ArrayList<Projectile> monsterProjectiles;

    private static final float SHOT_DELAY = 2;
    private float delayCounter;

    public Bowman(float x, float y, int width, int height, float maxSpeed, float accelerationRate, int hpCount, int armor, int damage, int radius){
        super(x, y, width, height, maxSpeed, accelerationRate, hpCount, armor, damage, radius);
        monsterProjectiles = new ArrayList<>();
        this.delayCounter = 0f;
    }

    @Override
    public void update(LivingBeing target) {
        if(target.position.distance(this.position)<150) {
            this.updateSpeed(target.position.copy().sub(this.position).normalise().negate().scale(this.getAccelerationRate()));
            this.move();
        }
        else if(this.speed.length()!=0){
            this.updateSpeed(this.speed.copy().normalise().negate().scale(getAccelerationRate()));
            this.move();
        }

        this.delayCounter += MainClass.getInGameTimeScale().getDeltaTime();
        if(this.delayCounter > SHOT_DELAY && !MainClass.isGamePaused()) {
            Vector2f futureTargetPosition = target.position.copy().add(target.speed.copy().scale(target.position.distance(this.position)/Snowball.MAX_SPEED));

            if(futureTargetPosition.getY() > MainClass.HEIGHT){
                futureTargetPosition.set(futureTargetPosition.getX(), MainClass.HEIGHT - target.getRenderer().getHeight()/4);
            }
            else if(futureTargetPosition.getY() < 0){
                futureTargetPosition.set(futureTargetPosition.getX(), 0);
            }
            if(futureTargetPosition.getX() > MainClass.WIDTH){
                futureTargetPosition.set(MainClass.WIDTH - target.getRenderer().getWidth()/4, futureTargetPosition.getY());
            }
            else if(futureTargetPosition.getX() < 0){
                futureTargetPosition.set(0, futureTargetPosition.getY());
            }

            Vector2f direction = futureTargetPosition.sub(this.position);

            //target the center of the opponent, not the top left corner
            direction.set(direction.getX() + target.getRenderer().getHeight()/4, direction.getY() + target.getRenderer().getWidth()/4);

            //this.monsterProjectiles.add(new Snowball(this.getPosition(), direction));
            this.monsterProjectiles.add(new Fireball(this.getPosition().copy().add(new Vector2f(this.getHeight()/2f, this.getWidth()/2f)), direction));
            this.monsterProjectiles.get(monsterProjectiles.size()-1).setShowDebugRect(true);

            this.delayCounter = 0f;
        }

        if(this.collidesWith(target)){
            this.collidingAction(target);
        }

        for (int i = 0; i < monsterProjectiles.size(); i++) {
            Projectile p = monsterProjectiles.get(i);
            p.update(i);

            if(p.collidesWith(target)){
                p.collidingAction(target);
            }

            if (p.isFadeOut() || p.isDead()) {
                monsterProjectiles.remove(i);
                i--;
            }
        }
    }

    public void render(Graphics g) {
        super.render(g);

        for(Projectile p : monsterProjectiles) {
            p.render(g);
        }
    }
}
