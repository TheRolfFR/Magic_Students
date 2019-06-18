package Managers;

import Entities.LivingBeings.LivingBeing;
import Entities.LivingBeings.Monsters.IBoss;
import Entities.LivingBeings.Monsters.Melee.Knight;
import Entities.LivingBeings.Monsters.Melee.KnightBoss;
import Entities.LivingBeings.Monsters.Monster;
import Entities.LivingBeings.Monsters.Ranged.Bowman;
import Entities.LivingBeings.Monsters.Ranged.BowmanBoss;
import Entities.LivingBeings.Monsters.Ranged.Ranged;
import Entities.LivingBeings.Player;
import Entities.Projectiles.Projectile;
import HUD.HealthBars.BossHealthBar;
import Listeners.SummonListener;
import Main.GameStats;
import Main.MainClass;
import org.newdawn.slick.Graphics;

import java.util.ArrayList;
import java.util.Random;

/**
 * Enemies data manager
 */
public class EnemiesManager implements SummonListener {

    private ArrayList<Monster> enemies = new ArrayList<>();
    private Player player;
    private PortalsManager portalsManager;
    private BossHealthBar bossHealthBar;

    /**
     * Enemies list getter
     * @return the enemies list
     */
    public ArrayList<Monster> getEnemies() {
        return enemies;
    }

    /**
     * boss healthbar setter
     * @param bossHealthBar the boss health bar
     */
    void setBossHealthBar(BossHealthBar bossHealthBar) {
        this.bossHealthBar = bossHealthBar;
    }

    /**
     * Default constructor
     * @param player the player related
     * @param portalsManager the portalManager instance
     */
    public EnemiesManager(Player player, PortalsManager portalsManager) {
        this.player = player;
        this.portalsManager = portalsManager;
    }

    /**
     * Random generation of enemies wherer its number depends of the difficulty
     */
    public void generateEnemies() {
        Random random = new Random();
        for (int i = 1; i < 4 + Math.round(GameStats.getInstance().getDifficulty() / 4); i++) {
            switch (random.nextInt(2)) {
                case 0 :
                    this.enemies.add(newBowman());
                    break;
                case 1 :
                    this.enemies.add(newKnight());
                    break;
                default: break;
            }
        }
    }

    /**
     * {@link Bowman} generation at a random posiion. Its armor, health points and damage depends of the difficulty
     * @return a bowan
     */
    public static Bowman newBowman() {

        Random random = new Random();

        int randomX = random.nextInt(Math.round(MainClass.WIDTH-2*Bowman.BOWMAN_TILESIZE.getX())) + (int) Bowman.BOWMAN_TILESIZE.getX();
        int randomY = random.nextInt(Math.round(MainClass.HEIGHT-2*Bowman.BOWMAN_TILESIZE.getY())) + (int) Bowman.BOWMAN_TILESIZE.getY();

        Bowman tmpb = new Bowman(randomX, randomY,
                Math.round(75 * GameStats.getInstance().getDifficulty()),
                Math.round(2 * GameStats.getInstance().getDifficulty()),
                Math.round(GameStats.getInstance().getDifficulty()),
                (int) Math.round(0.4 * Bowman.BOWMAN_TILESIZE.getY()));
        tmpb.setShowDebugRect(true);

        return tmpb;
    }


    /**
     * {@link Knight} generation at a random posiion. Its armor, health points and damage depends of the difficulty
     * @return a knight
     */
    public static Knight newKnight() {

        Random random = new Random();

        int randomX = random.nextInt(Math.round(MainClass.WIDTH-2*Knight.KNIGHT_TILESIZE.getX())) + (int) Knight.KNIGHT_TILESIZE.getX();
        int randomY = random.nextInt(Math.round(MainClass.HEIGHT-2*Knight.KNIGHT_TILESIZE.getY())) + (int) Knight.KNIGHT_TILESIZE.getY();

        Knight tmpk = new Knight(randomX, randomY,
                Math.round(100 * GameStats.getInstance().getDifficulty()),
                Math.round(2 * GameStats.getInstance().getDifficulty()),
                Math.round(6 * GameStats.getInstance().getDifficulty()),
                (int) Math.round(0.4 * Knight.KNIGHT_TILESIZE.getY()));
        tmpk.setShowDebugRect(true);

        return tmpk;
    }

    /**
     * Random boss generation
     */
    public void generateBoss() {
        Random random = new Random();

        IBoss boss = null;
        switch (random.nextInt(2)) {
            case 0 :
                boss = addBossBowman();
                break;
            case 1 :
                boss = addBossKnight();
                break;
            default: break;
        }

        if (boss != null)
            boss.addSummonListener(this);
    }

    /**
     * {@link KnightBoss} generation at a random posiion. Its armor, health points and damage depends of the difficulty
     * @return a knight boss
     */
    private IBoss addBossKnight() {
        KnightBoss knightBoss = new KnightBoss(MainClass.WIDTH / 2, MainClass.HEIGHT / 2,
                Math.round(1000 * GameStats.getInstance().getDifficulty()),
                Math.round(4 * GameStats.getInstance().getDifficulty()),
                Math.round(12 * GameStats.getInstance().getDifficulty()),
                (int) Math.round(0.4 * KnightBoss.KNIGHTBOSS_TILESIZE.getY()));
        knightBoss.setShowDebugRect(true);
        this.enemies.add(knightBoss);
        knightBoss.addHealthListener(this.bossHealthBar);
        this.bossHealthBar.onUpdate(knightBoss, 0);

        return knightBoss;
    }

    /**
     * {@link BowmanBoss} generation at a random posiion. Its armor, health points and damage depends of the difficulty
     * @return a knight boss
     */
    private IBoss addBossBowman() {
        BowmanBoss bowmanBoss = new BowmanBoss(MainClass.WIDTH / 2, MainClass.HEIGHT / 2,
                Math.round(1000 * GameStats.getInstance().getDifficulty()),
                Math.round(4 * GameStats.getInstance().getDifficulty()),
                Math.round(4 * GameStats.getInstance().getDifficulty()),
                (int) Math.round(0.4 * BowmanBoss.BOWMANBOSS_TILESIZE.getY()));
        bowmanBoss.setShowDebugRect(true);
        this.enemies.add(bowmanBoss);
        bowmanBoss.addHealthListener(this.bossHealthBar);
        this.bossHealthBar.onUpdate(bowmanBoss, 0);

        return bowmanBoss;
    }

    /**
     * In game update : collision check, player health, dead enemies removal, and portal appearance
     */
    public void update() {
        for (int i = 0; i < this.enemies.size(); i++) {
            Monster enemy = enemies.get(i);
            enemy.update(this.player);
            if (!this.player.isDashing()) {
                enemy.checkCollision();
            }
            if (this.player.isDead()) {
                MainClass.setGamePaused();
            }
            if (enemy.isDead()) {
                LivingBeing.livingBeings.remove(enemy);
                this.enemies.remove(enemy);
            }
        }
        if (this.enemies.size() == 0) {
            portalsManager.setPortals();
        }
    }

    /**
     * In game rendering
     * @param g the graphics to draw on
     */
    public void render(Graphics g) {
        for (Monster enemy : enemies) {
            enemy.render(g);

            if (!(enemy instanceof IBoss)) {
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

    /**
     * add enemies on boss summon
     * @param monster the monster to add to the list
     */
    @Override
    public void onSummon(Monster monster) {
        this.enemies.add(monster);
    }
}
