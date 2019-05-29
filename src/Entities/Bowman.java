package Entities;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

import java.util.ArrayList;

public class Bowman extends Ranged {

    ArrayList<Projectile> monsterProjectiles;


    public Bowman(float x, float y, int width, int height, float maxSpeed, float accelerationRate, int hpCount, int armor, int damage, int radius){
        super(x, y, width, height, maxSpeed, accelerationRate, hpCount, armor, damage, radius);
        monsterProjectiles = new ArrayList<>();
    }

    @Override
    public void update(LivingBeing target) {
        if(target.position.distance(this.position)<100) {
            this.updateSpeed(target.position.copy().sub(this.position).normalise().negate().scale(this.getAccelerationRate()));
            this.move();
        }

        //try of foresight the player's movement : Vector2f direction = target.position.copy().add(target.speed.copy().scale(target.position.distance(this.position)/Snowball.MAX_SPEED).sub(this.position));
        Vector2f direction = target.position.copy().sub(this.position);
        this.monsterProjectiles.add(new Snowball(this.getPosition(), direction));

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
