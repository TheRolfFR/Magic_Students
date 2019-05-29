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
        if(target.position.distance(this.position)<100) {
            this.updateSpeed(target.position.copy().sub(this.position).normalise().negate().scale(this.getAccelerationRate()));
            this.move();
        }

        this.delayCounter += MainClass.getInGameTimeScale().getDeltaTime();
        if(this.delayCounter > SHOT_DELAY) {
            //try of foresight the player's movement : Vector2f direction = target.position.copy().add(target.speed.copy().scale(target.position.distance(this.position)/Snowball.MAX_SPEED).sub(this.position));
            Vector2f direction = target.position.copy().sub(this.position);
            //this.monsterProjectiles.add(new Snowball(this.getPosition(), direction));
            this.monsterProjectiles.add(new Fireball(this.getPosition().add(new Vector2f(this.getHeight()/2f, this.getWidth()/2f)), direction));

            this.delayCounter = 0f;
        }

        if(this.collides(target)){
            this.collidingAction(target);
        }

        for (int i = 0; i < monsterProjectiles.size(); i++) {
            Projectile p = monsterProjectiles.get(i);
            p.update(i);

            if (p.isFadeOut()) {
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
