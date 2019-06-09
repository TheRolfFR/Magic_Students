package Main;

import Entities.LivingBeings.LivingBeing;
import Entities.LivingBeings.Player;
import Entities.LivingBeings.monsters.Melee.Knight;
import Entities.LivingBeings.monsters.Melee.Rusher;
import Entities.LivingBeings.monsters.Monster;
import Entities.LivingBeings.monsters.Ranged.Bowman;
import Entities.LivingBeings.monsters.Ranged.Ranged;
import Entities.Projectiles.Projectile;
import HUD.FadeToBlack;
import HUD.HealthBar;
import HUD.PauseMenu;
import Managers.PortalManager;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import static Entities.Projectiles.Projectile.*;
import static java.lang.Math.round;

public class MainClass extends BasicGame {
    private Player player;
    private ArrayList<Monster> enemies = new ArrayList<>();

    public static final int MAX_FPS = 60;
    public static final int WIDTH = 640;
    public static final int HEIGHT = 480;

    private static TimeScale inGameTimeScale = new TimeScale(1f);
    private static TimeScale guiTimeScale = new TimeScale(1f);

    private static GameContainer instanceGameContainer;
    private static MainClass instance = null;

    private PortalManager portalManager;

    private HealthBar healthBar;

    private PauseMenu menu;
    private FadeToBlack fadeToBlack;

    public FadeToBlack getFadeToBlack() {
        return fadeToBlack;
    }

    public static boolean isGamePaused() {
        return instance.menu.isActive();
    }

    private void generateEnemies(Vector2f tileSize) {
        Random random = new Random();
        int randomX;
        int randomY;
        for(int i = 0; i< 6; i++){
            randomX = random.nextInt(Math.round(WIDTH-2*tileSize.getX())) + (int) tileSize.getX();
            randomY = random.nextInt(Math.round(HEIGHT-2*tileSize.getY())) + (int) tileSize.getY();
            switch(random.nextInt(4)){
                case 0 :
                    Bowman tmpb = new Bowman(randomX, randomY, (int) tileSize.getX(), (int) tileSize.getY(), 250/MAX_FPS, 60/MAX_FPS, 100,2,5,(int) Math.round(0.4*tileSize.getY()));
                    tmpb.setShowDebugRect(true);
                    this.enemies.add(tmpb);
                    break;
                case 1 :
                    Rusher tmpr = new Rusher(randomX, randomY, (int) tileSize.getX(), (int) tileSize.getY(), 250/MAX_FPS, 60/MAX_FPS, 100,2,5,(int) Math.round(0.4*tileSize.getY()));
                    tmpr.setShowDebugRect(true);
                    this.enemies.add(tmpr);
                    break;
                case 2 :
                    Knight tmpk = new Knight(randomX, randomY, (int) tileSize.getX(), (int) tileSize.getY(), 250/MAX_FPS, 60/MAX_FPS, 100,2,5,(int) Math.round(0.4*tileSize.getY()));
                    tmpk.setShowDebugRect(true);
                    this.enemies.add(tmpk);
                default: break;
            }
        }
    }

    private void generateRoom() throws SlickException {
        System.out.println("new room");
        Ranged.allyProjectiles = new ArrayList<>();
        Ranged.enemyProjectiles = new ArrayList<>();
        generateEnemies(new Vector2f(48,48));
    }

    public static void setGamePaused(boolean gamePaused) {
        getInGameTimeScale().setTimeScale((gamePaused) ? 0f : 1f);
        instance.menu.setActive(gamePaused);
    }

    private static void triggerGamePaused() {
        getInGameTimeScale().setTimeScale((isGamePaused()) ? 1f : 0f);
        instance.menu.setActive(!isGamePaused());
    }

    public static TimeScale getInGameTimeScale() {
        return inGameTimeScale;
    }

    public static MainClass getInstance() {
        return instance;
    }

    public Player getPlayer() {
        return player;
    }

    public ArrayList<Monster> getEnemies() {
        return enemies;
    }

    public static TimeScale getGuiTimeScale() {
        return guiTimeScale;
    }

    public static Input getInput() {
        return instanceGameContainer.getInput();
    }

    public MainClass(String name) { super(name); }

    @Override
    public void init(GameContainer gc) throws SlickException {
        instanceGameContainer = gc;
        instance = this;
        menu = new PauseMenu(gc);
        this.fadeToBlack = new FadeToBlack(gc);

        this.player = new Player(gc,100,100);
        this.player.setShowDebugRect(true);
        this.healthBar = new HealthBar(this.player);

        SceneRenderer.generateBackground("img/ground.png", gc);

        this.portalManager = new PortalManager();

        generateRoom();
    }

    @Override
    public void update(GameContainer gc, int timeOfOneFrame) throws SlickException {
        inGameTimeScale.setDeltaTime(timeOfOneFrame);

        this.player.update();
        if(!this.player.isDashing()){
            this.player.checkCollision();
        }
        updateEnemyProjectile(player);
        updateAllyProjectiles();

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
            portalManager.setPortals();
        }

        portalManager.update(timeOfOneFrame);

        if (fadeToBlack.isActive()) {
            fadeToBlack.update(gc);

            if (fadeToBlack.getCurrentCount() == fadeToBlack.getDuration() / 2) {
                generateRoom();

                portalManager.hidePortals();
            }
            else if (fadeToBlack.getCurrentCount() == fadeToBlack.getDuration()) {
                getInGameTimeScale().setTimeScale(1f);
            }
        }
    }

    @Override
    public void keyPressed(int key, char c) {
        if (key == Input.KEY_ESCAPE) {
            triggerGamePaused();
        }
        if (key == Input.KEY_F) {
            portalManager.setPortalEngaged(true);
        }
    }

    @Override
    public void keyReleased(int key, char c) {
        this.player.keyReleased(key, c);

        if (key == Input.KEY_F) {
            portalManager.setPortalEngaged(false);
        }
    }

    @Override
    public void render(GameContainer gc, Graphics g) {
        SceneRenderer.renderBackground(g, 0, 0);

        LivingBeing.sortAndRenderLivingBeings(g);

        this.healthBar.render(g);
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

        portalManager.render(g);

        this.menu.render(g);
        this.fadeToBlack.render(g);
    }

    public static void main(String[] args) {
        try {
            AppGameContainer appgc;
            appgc = new AppGameContainer(new MainClass("Magic Students"));
            appgc.setDisplayMode(WIDTH, HEIGHT, false);
            appgc.setTargetFrameRate(MAX_FPS);
            appgc.start();
        }
        catch (SlickException ex) {
            Logger.getLogger(MainClass.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
