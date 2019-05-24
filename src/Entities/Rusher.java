package Entities;

import org.newdawn.slick.geom.Vector2f;

public class Rusher extends Melee {

    public Rusher(float x, float y, int width, int height, float maxSpeed, float accelerationRate, int hpCount, int armor, int damage){
        super(x,y,width,height,maxSpeed,accelerationRate, hpCount,armor, damage);
    }

    public void chaseAI(Entity target){
        if(this.position.getX() < target.position.getX()){
            this.updateSpeed(new Vector2f(1,0));
        }
        if(this.position.getX() > target.position.getX()){
            this.updateSpeed(new Vector2f(-1,0));
        }
        if(this.position.getY() < target.position.getY()){
            this.updateSpeed(new Vector2f(0,1));
        }
        if(this.position.getY() > target.position.getY()){
            this.updateSpeed(new Vector2f(0,-1));
        }
        this.move();
    }
}
