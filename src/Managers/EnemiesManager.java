package Managers;

import Entities.LivingBeings.LivingBeing;
import Entities.LivingBeings.Monsters.IBoss;
import Entities.LivingBeings.Monsters.Melee.KnightBoss;
import Entities.LivingBeings.Monsters.Ranged.BowmanBoss;
import Entities.LivingBeings.Player;
import Entities.LivingBeings.Monsters.Melee.Knight;
import Entities.LivingBeings.Monsters.Monster;
import Entities.LivingBeings.Monsters.Ranged.Bowman;
import Entities.LivingBeings.Monsters.Ranged.Ranged;
import Entities.Projectiles.Projectile;
import HUD.HealthBars.WorldHealthBar;
import Main.MainClass;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

import java.util.ArrayList;
import java.util.Random;

import static Main.MainClass.*;
import static java.lang.Math.round;

public class EnemiesManager {

    private ArrayList<Monster> enemies = new ArrayList<>();
    private Player player;
    private PortalsManager portalsManager;

    private IBoss boss;

    public ArrayList<Monster> getEnemies() {
        return enemies;
    }

    public IBoss getBoss() {
        return boss;
    }

    public EnemiesManager(Player player, PortalsManager portalsManager) {
        this.player = player;
        this.portalsManager = portalsManager;
    }

    public void generateEnemies() {
        Random random = new Random();
        for(int i = 0; i< 4+(int) Math.round(MainClass.getDifficulty()/4); i++){
            switch(random.nextInt(2)){
                case 0 :
                    addBowman();
                    break;
                case 1 :
                    addKnight();
                    break;
                default: break;
            }
        }
    }

    public void addBowman(){

        Random random = new Random();

        int randomX = random.nextInt(Math.round(WIDTH-2*Bowman.BOWMAN_TILESIZE.getX())) + (int) Bowman.BOWMAN_TILESIZE.getX();
        int randomY = random.nextInt(Math.round(HEIGHT-2*Bowman.BOWMAN_TILESIZE.getY())) + (int) Bowman.BOWMAN_TILESIZE.getY();

        Bowman tmpb = new Bowman(randomX, randomY,150/MAX_FPS, 60/MAX_FPS, (int) Math.round(75*MainClass.getDifficulty()),(int) Math.round(2*MainClass.getDifficulty()),(int) Math.round(1*MainClass.getDifficulty()),(int) Math.round(0.4*Bowman.BOWMAN_TILESIZE.getY()));
        tmpb.setShowDebugRect(true);
        this.enemies.add(tmpb);
    }

    public void addKnight(){

        Random random = new Random();

        int randomX = random.nextInt(Math.round(WIDTH-2*Knight.KNIGHT_TILESIZE.getX())) + (int) Knight.KNIGHT_TILESIZE.getX();
        int randomY = random.nextInt(Math.round(HEIGHT-2*Knight.KNIGHT_TILESIZE.getY())) + (int) Knight.KNIGHT_TILESIZE.getY();

        Knight tmpk = new Knight(randomX, randomY, 250/MAX_FPS, 60/MAX_FPS, (int) Math.round(100*MainClass.getDifficulty()),(int) Math.round(2*MainClass.getDifficulty()),(int) Math.round(1*MainClass.getDifficulty()),(int) Math.round(0.4*Knight.KNIGHT_TILESIZE.getY()));
        tmpk.setShowDebugRect(true);
        this.enemies.add(tmpk);
    }

    public void generateBoss() {
        Random random = new Random();

        switch(random.nextInt(2)){
            case 0 :
                this.boss = addBossBowman();
                break;
            case 1 :
                this.boss = addBossKnight();
                break;
            default: break;
        }
    }

    private IBoss addBossKnight(){
        KnightBoss knightBoss = new KnightBoss(WIDTH/2, HEIGHT/2,200/MAX_FPS, 60/MAX_FPS, (int) Math.round(1000*MainClass.getDifficulty()), (int) Math.round(10*MainClass.getDifficulty()),(int) Math.round(10*MainClass.getDifficulty()),(int) Math.round(0.4*KnightBoss.KNIGHTBOSS_TILESIZE.getY()));
        knightBoss.setShowDebugRect(true);
        this.enemies.add(knightBoss);

        return knightBoss;
    }

    private IBoss addBossBowman(){
        BowmanBoss bowmanBoss = new BowmanBoss(WIDTH/2, HEIGHT/2,200/MAX_FPS, 60/MAX_FPS, (int) Math.round(1000*MainClass.getDifficulty()), (int) Math.round(10*MainClass.getDifficulty()),(int) Math.round(10*MainClass.getDifficulty()),(int) Math.round(0.4*BowmanBoss.BOWMANBOSS_TILESIZE.getY()));
        bowmanBoss.setShowDebugRect(true);
        this.enemies.add(bowmanBoss);

        return bowmanBoss;
    }

    public void update() {
        for(int i=0; i<this.enemies.size(); i++){
            Monster enemy = enemies.get(i);
            enemy.update(this.player);
            if(!this.player.isDashing()){
                enemy.checkCollision();
            }
            if (this.player.isDead()){
                //setGamePaused(true);
            }
            if (enemy.isDead()) {
                System.out.println("You killed an enemy");
                LivingBeing.livingBeings.remove(enemy);
                this.enemies.remove(enemy);


                if(enemy instanceof IBoss) {
                    this.boss = null;
                }
            }
        }
        if (this.enemies.size() == 0) {
            portalsManager.setPortals();
        }
    }

    public void render(Graphics g) {
        for (Monster enemy: enemies){
            enemy.render(g);

            if(!(enemy instanceof IBoss)) {
                enemy.getWorldHealthBar().render(g);
            }
        }
        for (Projectile p : Ranged.enemyProjectiles) {
            p.render(g);
        }
        for (Projectile p : Ranged.allyProjectiles) {
            p.render(g);
        }
    }
}
