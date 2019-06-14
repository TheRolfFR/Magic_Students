package Entities.LivingBeings.Monsters.Melee;

import Entities.LivingBeings.LivingBeing;
import Main.MainClass;
import Renderers.LivingBeingRenderer;
import Renderers.SpriteView;
import org.newdawn.slick.geom.Vector2f;

public class Knight extends Melee {

    public static final Vector2f KNIGHT_TILESIZE = new Vector2f(48,48);
    private int framesLeftBeforeAttack;
    private Vector2f attackDirection = new Vector2f(0,0);
    private int framesLeftWhileStuned = 0;

    public Knight(float x, float y, float maxSpeed, float accelerationRate, int hpCount, int armor, int damage, int radius){
        super(x, y, (int) KNIGHT_TILESIZE.getX(), (int) KNIGHT_TILESIZE.getY(), maxSpeed, accelerationRate, hpCount, armor, damage, radius);

        this.renderer = new LivingBeingRenderer(this, KNIGHT_TILESIZE);

        final String prepath = "img/knight/";

        final int duration = 1000/8;

        for(String vision : LivingBeingRenderer.ACCEPTED_VISION_DIRECTIONS) {
            this.renderer.addView(vision + "Move", new SpriteView(prepath + vision + ".png", KNIGHT_TILESIZE, duration));
        }
    }

    public Knight(float x, float y, Vector2f tileSize, float maxSpeed, float accelerationRate, int hpCount, int armor, int damage, int radius){
        super(x, y, (int) tileSize.getX(), (int) tileSize.getY(), maxSpeed, accelerationRate, hpCount, armor, damage, radius);

        this.renderer = new LivingBeingRenderer(this, tileSize);

        final String prepath = "img/knight/";

        final int duration = 1000/8;

        for(String vision : LivingBeingRenderer.ACCEPTED_VISION_DIRECTIONS) {
            this.renderer.addView(vision + "Move", new SpriteView(prepath + vision + ".png", tileSize, duration));
        }
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
            if (isStun()){
                recover();
            }
            else {
                this.updateSpeed(target.getPosition().sub(this.getPosition()).normalise().scale(this.getAccelerationRate()));

                this.move();
                if (isTargetInRange(target)){
                    startAttacking(target);
                }
            }
        }
    }

    private void recover() {
        framesLeftWhileStuned = framesLeftWhileStuned - 1;
    }

    private boolean isStun() {
        return framesLeftWhileStuned != 0;
    }

    boolean isTargetInRange(LivingBeing target){
        return (this.getPosition().add(getLocationOfTarget(target).scale(this.getRadius()*2)).sub(target.getPosition()).length() < target.getRadius() + this.getRadius());
    }

    void startAttacking(LivingBeing target){
        this.setSpeed(new Vector2f(0,0));
        this.attackDirection = getLocationOfTarget(target);
        this.framesLeftBeforeAttack = MainClass.getNumberOfFramePerSecond()/2;
    }

    void gettingReady(){
        this.framesLeftBeforeAttack = this.framesLeftBeforeAttack - 1;
    }

    boolean isAttackReady(){
        return (this.framesLeftBeforeAttack == 0);
    }

    boolean isAttacking(){
        return (!this.attackDirection.equals(new Vector2f(0,0)));
    }

    private Vector2f getLocationOfTarget(LivingBeing target){
        Vector2f directionOfTarget = new Vector2f(target.getPosition().sub(this.getPosition()));
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

    protected void attack(LivingBeing target){
        System.out.println("attack!");
        if (isTargetInRange(target)){
            System.out.println("damage");
            target.takeDamage(this.getDamage());
        }
        this.attackDirection.set(0,0);
        framesLeftWhileStuned = MainClass.getNumberOfFramePerSecond()/10;
    }
}
