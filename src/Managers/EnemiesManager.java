package Managers;

import Entities.LivingBeings.LivingBeing;
import Entities.LivingBeings.Player;
import Entities.LivingBeings.Monsters.Melee.Knight;
import Entities.LivingBeings.Monsters.Melee.Rusher;
import Entities.LivingBeings.Monsters.Monster;
import Entities.LivingBeings.Monsters.Ranged.Bowman;
import Entities.LivingBeings.Monsters.Ranged.Ranged;
import Entities.Projectiles.Projectile;
import HUD.HealthBar;
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

    public ArrayList<Monster> getEnemies() {
        return enemies;
    }

    public EnemiesManager(Player player, PortalsManager portalsManager) {
        this.player = player;
        this.portalsManager = portalsManager;
    }

    public void generateEnemies(Vector2f tileSize) {
        Random random = new Random();
        for(int i = 0; i< 6; i++){
            switch(random.nextInt(2)){
                case 0 :
                    addBowman(tileSize);
                    break;
                case 1 :
                    addKnight(tileSize);
                    break;
                default: break;
            }
        }
    }

    private void addBowman(Vector2f tileSize){

        Random random = new Random();

        int randomX = random.nextInt(Math.round(WIDTH-2*tileSize.getX())) + (int) tileSize.getX();
        int randomY = random.nextInt(Math.round(HEIGHT-2*tileSize.getY())) + (int) tileSize.getY();

        Bowman tmpb = new Bowman(randomX, randomY, (int) tileSize.getX(), (int) tileSize.getY(), 250/MAX_FPS, 60/MAX_FPS, 100,2,1,(int) Math.round(0.4*tileSize.getY()));
        tmpb.setShowDebugRect(true);
        this.enemies.add(tmpb);
    }

    public void addKnight(Vector2f tileSize){

        Random random = new Random();

        int randomX = random.nextInt(Math.round(WIDTH-2*tileSize.getX())) + (int) tileSize.getX();
        int randomY = random.nextInt(Math.round(HEIGHT-2*tileSize.getY())) + (int) tileSize.getY();

        Knight tmpk = new Knight(randomX, randomY, (int) tileSize.getX(), (int) tileSize.getY(), 250/MAX_FPS, 60/MAX_FPS, 100,2,1,(int) Math.round(0.4*tileSize.getY()));
        tmpk.setShowDebugRect(true);
        this.enemies.add(tmpk);
    }

    public void update() {
        for(Monster enemy : this.enemies){
            enemy.update(this.player);
            if(!this.player.isDashing()){
                enemy.checkCollision();
            }
            if (this.player.isDead()){
                //setGamePaused(true);
            }
        }

        for (int j=0; j<enemies.size(); j++) {
            if (enemies.get(j).isDead()) {
                System.out.println("You killed an enemy");
                LivingBeing.livingBeings.remove(this.enemies.get(j));
                this.enemies.remove(this.enemies.get(j));
            }
        }

        if (this.enemies.size() == 0) {
            portalsManager.setPortals();
        }
    }

    public void render(Graphics g) {
        for (Monster enemy: enemies){
            enemy.setHealthBar(new HealthBar(enemy ,(int) enemy.getPosition().x, (int) enemy.getPosition().y + (int) round(enemy.getRadius()*2.5)));
            enemy.render(g);
            enemy.getHealthBar().render(g);
        }
        for (Projectile p : Ranged.enemyProjectiles) {
            p.render(g);
        }
        for (Projectile p : Ranged.allyProjectiles) {
            p.render(g);
        }
    }
}
