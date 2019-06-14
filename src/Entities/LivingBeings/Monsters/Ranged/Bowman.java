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

import java.util.Random;

public class Bowman extends Ranged {

    public static final Vector2f BOWMAN_TILESIZE = new Vector2f(48,48);
    static final int SHOT_DELAY = 120;
    static final int RUN_AWAY_THRESHOLD = 200;
    int delayCounter;
    private int framesLeftBeforeAttack;
    private Vector2f attackDirection = new Vector2f(0,0);
    private int framesLeftWhileStuned = 0;
    private int framesLeftWhileSpeedLocked = 0;
    private int shootCooldown = 0;

    public Bowman(float x, float y, float maxSpeed, float accelerationRate, int hpCount, int armor, int damage, int radius){
        super(x, y, (int) BOWMAN_TILESIZE.getX(), (int) BOWMAN_TILESIZE.getY(), maxSpeed, accelerationRate, hpCount, armor, damage, radius);
        this.delayCounter = 0;

        this.renderer = new LivingBeingRenderer(this, BOWMAN_TILESIZE);

        final String prepath = "img/bowman/";

        final int duration = 1000/8;

        for(String vision : LivingBeingRenderer.ACCEPTED_VISION_DIRECTIONS) {
            this.renderer.addView(vision + "Move", new SpriteView(prepath + vision + ".png", BOWMAN_TILESIZE, duration));
        }
    }

    public Bowman(float x, float y, Vector2f tileSize, float maxSpeed, float accelerationRate, int hpCount, int armor, int damage, int radius){
        super(x, y, (int) tileSize.getX(), (int) tileSize.getY(), maxSpeed, accelerationRate, hpCount, armor, damage, radius);
        this.delayCounter = 0;

        this.renderer = new LivingBeingRenderer(this, tileSize);

        final String prepath = "img/bowman/";

        final int duration = 1000/8;

        for(String vision : LivingBeingRenderer.ACCEPTED_VISION_DIRECTIONS) {
            this.renderer.addView(vision + "Move", new SpriteView(prepath + vision + ".png", tileSize, duration));
        }
    }

    @Override
    public void update(LivingBeing target) {
        updateCooldown();
        if (this.isAttacking()){
            if (this.isAttackReady()){
                attack(target);
            }
        }
        else {
            if (!isStun())
            {
                if (isShootReady()){
                    startAttacking(target);
                }
                else {
                    if(target.getPosition().distance(this.getPosition()) < RUN_AWAY_THRESHOLD) {
                        runAway(target);
                    }
                    else {
                        if (!isSpeedLocked()){
                            if (decideToMove()){
                                chooseDirection();
                            }
                            else {
                                if(this.getSpeed().length() != 0) {
                                    this.updateSpeed(this.getSpeed().normalise().negate().scale(getAccelerationRate()));
                                }
                            }
                        }
                    }
                }
                this.move();
            }
        }
    }

    private void runAway(LivingBeing target) {
        this.updateSpeed(target.getPosition().sub(this.getPosition()).normalise().negate().scale(this.getAccelerationRate()));
        framesLeftWhileSpeedLocked = 0;
    }

    private void updateCooldown() {
        if (!isAttackReady()){
            framesLeftBeforeAttack = framesLeftBeforeAttack - 1;
        }
        if (!isShootReady()){
            shootCooldown = shootCooldown - 1;
        }
        if (isStun()){
            framesLeftWhileStuned = framesLeftWhileStuned - 1;
        }
        if (isSpeedLocked()) {
            framesLeftWhileSpeedLocked = framesLeftWhileSpeedLocked - 1;
        }
    }

    private boolean isShootReady() {
        return shootCooldown == 0;
    }

    private void startAttacking(LivingBeing target) {
        attackDirection.set(target.getPosition().sub(this.getPosition()).normalise());
        this.setSpeed(new Vector2f(0,0));
    }

    private void chooseDirection() {
        Random random = new Random();
        this.updateSpeed(new Vector2f(random.nextFloat(),random.nextFloat()).normalise().scale(this.getAccelerationRate()));
        System.out.println(this.getSpeed());
        framesLeftWhileSpeedLocked = MainClass.getNumberOfFramePerSecond()*2;
    }

    private boolean decideToMove() {
        Random random = new Random();
        return (random.nextFloat()%1 < 1f/(MainClass.getNumberOfFramePerSecond()*2f));
    }

    private boolean isSpeedLocked() {
        return framesLeftWhileSpeedLocked != 0;
    }

    private void recover() {
        framesLeftWhileStuned = framesLeftWhileStuned - 1;
    }

    private boolean isStun() {
        return framesLeftWhileStuned != 0;
    }

    boolean isAttackReady(){
        return (this.framesLeftBeforeAttack == 0);
    }

    boolean isAttacking(){
        return (!this.attackDirection.equals(new Vector2f(0,0)));
    }

    void gettingReady(LivingBeing target){
        this.framesLeftBeforeAttack = this.framesLeftBeforeAttack - 1;
        attackDirection.set(target.getPosition().sub(this.getPosition()).normalise());
    }

    protected void attack(LivingBeing target){

        attackDirection.set(target.getPosition().sub(this.getPosition()).normalise());
        enemyProjectiles.add(new Arrow(this.getPosition().add(attackDirection.copy().scale(this.getRadius())), attackDirection));
        enemyProjectiles.get(enemyProjectiles.size()-1).setShowDebugRect(true);
        attackDirection.set(0,0);
        this.shootCooldown = MainClass.getNumberOfFramePerSecond()*2;
        this.framesLeftWhileStuned = MainClass.getNumberOfFramePerSecond()/10;
    }

    public void render(Graphics g) {
        super.render(g);
    }
}
