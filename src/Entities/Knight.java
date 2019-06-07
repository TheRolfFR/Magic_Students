package Entities;

import org.newdawn.slick.geom.Vector2f;

public class Knight extends Melee {

    private int framesLeftBeforeAttack;
    private Vector2f attackDirection = new Vector2f(0,0);



    public Knight(float x, float y, int width, int height, float maxSpeed, float accelerationRate, int hpCount, float armor, int damage, int radius){
        super(x, y, width, height, maxSpeed, accelerationRate, hpCount, armor, damage, radius);
    }

    public void update(LivingBeing target){
        if (this.isAttacking()){
            if (this.isAttackReady()){
                attack(target);
            }
            else {
                gettingReady();
            }
        }
        else {
            this.updateSpeed(target.position.copy().sub(this.position).normalise().scale(this.getAccelerationRate()));

            this.move();
            if (isTargetInRange(target)){
                startAttacking(target);
            }
        }
    }

    private boolean isTargetInRange(LivingBeing target){
        return (this.getCenter().add(getLocationOfTarget(target).scale(this.getRadius())).sub(target.getCenter()).length() < target.getRadius() + this.getRadius());
    }

    private void startAttacking(LivingBeing target){
        this.speed.set(0,0);
        this.attackDirection = getLocationOfTarget(target);
        this.framesLeftBeforeAttack = 30;
    }

    private void gettingReady(){
        this.framesLeftBeforeAttack = this.framesLeftBeforeAttack - 1;
    }

    private boolean isAttackReady(){
        return (this.framesLeftBeforeAttack == 0);
    }

    private boolean isAttacking(){
        return (!this.attackDirection.equals(new Vector2f(0,0)));
    }

    private Vector2f getLocationOfTarget(LivingBeing target){
        Vector2f directionOfTarget = new Vector2f(target.getCenter().sub(this.getCenter()));
        if (directionOfTarget.getX() < directionOfTarget.getY()){
            if (directionOfTarget.getY() < 0){
                return new Vector2f(0,-1);
            }
            else {
                return new Vector2f(0,1);
            }
        }
        else {
            if (directionOfTarget.getX() < 0){
                return new Vector2f(-1,0);
            }
            else {
                return new Vector2f(1,0);
            }
        }
    }

    private void attack(LivingBeing target){
        if (isTargetInRange(target)){
            target.takeDamage(this.getDamage());
        }
        this.attackDirection.set(0,0);
    }
}
