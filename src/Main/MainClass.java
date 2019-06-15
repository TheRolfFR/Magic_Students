package Main;

import Entities.LivingBeings.LivingBeing;
import Entities.LivingBeings.Monsters.Monster;
import Entities.LivingBeings.Monsters.Ranged.Ranged;
import Entities.LivingBeings.Player;
import Entities.Projectiles.Projectile;
import HUD.FadeToBlack;
import Listeners.FadeToBlackListener;
import HUD.PauseMenu;
import Managers.EnemiesManager;
import Managers.HUDManager;
import Managers.ItemManager;
import Managers.PortalsManager;
import Renderers.BackgroundRenderer;
import org.newdawn.slick.*;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainClass extends BasicGame {
    public static final int MAX_FPS = 60;
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;

    public static GameContainer instanceGameContainer;
    private static MainClass instance = null;

    private static double difficulty = 1;
    private static int numberOfFramePerSecond = 60;

    private Player player;

    private ItemManager itemManager;
    private PortalsManager portalsManager;
    private EnemiesManager enemiesManager;
    private HUDManager hudManager;

    private PauseMenu pauseMenu;

    private FadeToBlack fadeToBlack;

    public static double getDifficulty(){return difficulty;}

    public static int getNumberOfFramePerSecond() { return numberOfFramePerSecond; }

    public static void nextDifficulty() {
        difficulty++;
    }

    public static boolean isGamePaused() {
        return instance.pauseMenu.isActive();
    }

    private void generateRoom() {
        System.out.println("new room");
        BackgroundRenderer.regenerateBackground();
        Ranged.allyProjectiles = new ArrayList<>();
        Ranged.enemyProjectiles = new ArrayList<>();

        if(portalsManager.getActualPortal()!=null){
            switch (portalsManager.getActualPortal().getType()) {
                case "boss":
                    enemiesManager.generateBoss();
                    break;
                case "item":
                    itemManager.newItem();
                    break;
                case "classic":
                    enemiesManager.generateEnemies();
                    break;
            }
        }
    }

    public static void setGamePaused(boolean gamePaused) {
        TimeScale.getInGameTimeScale().setTimeScale((gamePaused) ? 0f : 1f);
        instance.pauseMenu.setActive(gamePaused);
    }

    private static void triggerGamePaused() {
        TimeScale.getInGameTimeScale().setTimeScale((isGamePaused()) ? 1f : 0f);
        instance.pauseMenu.setActive(!isGamePaused());
    }

    public static MainClass getInstance() {
        return instance;
    }

    public Player getPlayer() {
        return player;
    }

    public ArrayList<Monster> getEnemies() {
        return enemiesManager.getEnemies();
    }

    public MainClass(String name) { super(name); }

    @Override
    public void init(GameContainer gc) {
        instanceGameContainer = gc;
        instance = this;
        this.pauseMenu = new PauseMenu(gc);
        this.fadeToBlack = new FadeToBlack();

        this.player = new Player(gc,WIDTH/2,HEIGHT/2);
        this.player.setShowDebugRect(true);

        BackgroundRenderer.generateBackground(gc);

        this.itemManager = new ItemManager(this.player);
        this.portalsManager = new PortalsManager(gc, this.player, this.fadeToBlack);
        this.enemiesManager = new EnemiesManager(this.player, this.portalsManager);
        this.hudManager = new HUDManager(this.player, this.enemiesManager);

        this.fadeToBlack.addFadeToBlackListener(new FadeToBlackListener() {
            @Override
            public void atHalf() {
                System.out.println("Fade at half");
                generateRoom();
                portalsManager.hidePortals();
            }

            @Override
            public void atEnd() {
                TimeScale.resume();
            }
        });
    }

    @Override
    public void update(GameContainer gc, int timeOfOneFrame) {
        numberOfFramePerSecond = gc.getFPS();

        if (!MainClass.isGamePaused()) {
            TimeScale.getInGameTimeScale().setDeltaTime(timeOfOneFrame);

            this.player.update();

            Projectile.updateEnemyProjectile(player);
            Projectile.updateAllyProjectiles();

            this.enemiesManager.update();
            this.portalsManager.update(timeOfOneFrame);
            this.hudManager.update(timeOfOneFrame);

            fadeToBlack.update(timeOfOneFrame);
        }
    }

    @Override
    public void keyPressed(int key, char c) {
        if (key == Input.KEY_ESCAPE) {
            triggerGamePaused();
        }
    }

    @Override
    public void render(GameContainer gc, Graphics g) {
        BackgroundRenderer.renderBackground(g);

        // must be rendered before the living beings to be behind
        this.portalsManager.render(g);

        LivingBeing.sortAndRenderLivingBeings(g);

        this.itemManager.render(g);
        this.enemiesManager.render(g);
        this.hudManager.render(g);

        this.pauseMenu.render(g);

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
