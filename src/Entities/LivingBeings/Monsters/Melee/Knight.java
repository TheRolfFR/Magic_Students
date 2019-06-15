package Entities.LivingBeings.Monsters.Melee;

import Entities.LivingBeings.LivingBeing;
import Main.TimeScale;
import Renderers.LivingBeingRenderer;
import Renderers.SpriteView;
import org.newdawn.slick.geom.Vector2f;

public class Knight extends Melee implements KnightConstant{

    private float timeLeftBeforeAttack = KnightConstant.ATTACK_LOADING_DURATION;
    private float timeLeftWhileStuned = KnightConstant.STUN_AFTER_ATTACK_DURATION;
    private Vector2f attackDirection = new Vector2f(0,0);

    public Knight(float x, float y, float maxSpeed, float accelerationRate, int hpCount, int armor, int damage, int radius){
        super(x, y, (int) KnightConstant.KNIGHT_TILESIZE.getX(), (int) KnightConstant.KNIGHT_TILESIZE.getY(), maxSpeed, accelerationRate, hpCount, armor, damage, radius);

        this.renderer = new LivingBeingRenderer(this, KnightConstant.KNIGHT_TILESIZE);

        final String prepath = "img/knight/";

        final int moveDuration = 1000/8;
        final int attackDuration = Math.round(1000*KnightConstant.ATTACK_LOADING_DURATION);

        String[] activities = {"Move", "Attack"};

        String fileName;
        int duration;
        for(String vision : LivingBeingRenderer.ACCEPTED_VISION_DIRECTIONS) {
            for(String activity : activities){
                fileName = vision;
                if(!activity.equals("Move")){
                    fileName += activity;
                    duration = attackDuration;
                }
                else{
                    duration = moveDuration;
                }
                this.renderer.addView(vision + activity, new SpriteView(prepath + fileName + ".png", KnightConstant.KNIGHT_TILESIZE, duration));
            }
        }
        this.renderer.addView("Idle", new SpriteView(prepath + "Idle.png", KnightConstant.KNIGHT_TILESIZE, Math.round (KnightConstant.STUN_AFTER_ATTACK_DURATION*1000)));
    }

    public Knight(float x, float y, Vector2f tileSize, float maxSpeed, float accelerationRate, int hpCount, int armor, int damage, int radius){
        super(x, y, (int) tileSize.getX(), (int) tileSize.getY(), maxSpeed, accelerationRate, hpCount, armor, damage, radius);

        this.renderer = new LivingBeingRenderer(this, tileSize);
        this.renderer.setLastActivity("Idle");

        final String prepath = "img/knight/";

        final int duration = 1000/8;

        String[] activities = {"Attack", "Move"};

        String fileName;
        for(String vision : LivingBeingRenderer.ACCEPTED_VISION_DIRECTIONS) {
            for(String activity : activities){
                fileName = vision;
                if(!activity.equals("Move")){
                    fileName += activity;
                }
                this.renderer.addView(vision + activity, new SpriteView(prepath + fileName + ".png", tileSize, duration));
            }
        }
        this.renderer.addView("Idle", new SpriteView(prepath + "Idle.png", tileSize, Math.round (KnightConstant.STUN_AFTER_ATTACK_DURATION*1000)));
    }

    public void update(LivingBeing target){
        this.updateCountdown();
        if (this.isAttacking()){
            if (this.isAttackReady()){
                this.attack(target);
            }
        }
        else {
            if (!this.isStun()){
                this.renderer.setLastActivity("Move");
                super.updateSpeed(target.getCenter().sub(super.getCenter()).normalise().scale(super.getAccelerationRate()));

                super.move();
                if (this.isTargetInRange(target)){
                    this.startAttacking(target);
                }
            }
        }
    }

    void updateCountdown() {
        if (!this.isAttackReady()){
            this.timeLeftBeforeAttack = this.timeLeftBeforeAttack - TimeScale.getInGameTimeScale().getDeltaTime();
        }
        if (this.isStun()){
            this.timeLeftWhileStuned = this.timeLeftWhileStuned - TimeScale.getInGameTimeScale().getDeltaTime();
        }
    }

    boolean isStun() {
        return this.timeLeftWhileStuned > 0;
    }

    boolean isTargetInRange(LivingBeing target){
        return (super.getCenter().add(getLocationOfTarget(target).scale(super.getRadius()*1.5f)).sub(target.getCenter()).length() < target.getRadius() + super.getRadius());
    }

    private boolean isTargetStillInRange(LivingBeing target){
        return (super.getCenter().add(this.attackDirection.scale(super.getRadius()*1.5f)).sub(target.getCenter()).length() < target.getRadius() + super.getRadius());
    }

    void startAttacking(LivingBeing target){
        super.setSpeed(new Vector2f(0,0));
        this.attackDirection = getLocationOfTarget(target);
        this.timeLeftBeforeAttack = KnightConstant.ATTACK_LOADING_DURATION;
        this.renderer.setLastActivity("Attack");
        this.renderer.update(this.attackDirection);
    }

    boolean isAttackReady(){
        return (this.timeLeftBeforeAttack <= 0);
    }

    boolean isAttacking(){
        return (!this.attackDirection.equals(new Vector2f(0,0)));
    }

    private Vector2f getLocationOfTarget(LivingBeing target){
        Vector2f directionOfTarget = new Vector2f(target.getCenter().sub(super.getCenter()));
        if (Math.abs(directionOfTarget.getX()) < Math.abs(directionOfTarget.getY())){
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

    void stun(){
        this.timeLeftWhileStuned = KnightConstant.STUN_AFTER_ATTACK_DURATION;
    }

    protected void attack(LivingBeing target){
        if (this.isTargetStillInRange(target)){
            target.takeDamage(super.getDamage());
        }
        this.attackDirection.set(0,0);
        this.stun();
    }
}
