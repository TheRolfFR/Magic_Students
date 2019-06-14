package Main;

import Entities.Item;
import Entities.LivingBeings.LivingBeing;
import Entities.LivingBeings.Player;
import Entities.LivingBeings.Monsters.Monster;
import Entities.LivingBeings.Monsters.Ranged.Ranged;
import HUD.FadeToBlack;
import HUD.PauseMenu;
import Managers.EnemiesManager;
import Managers.HUDManager;
import Managers.PortalsManager;
import Renderers.BackgroundRenderer;
import org.newdawn.slick.*;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import static Entities.Projectiles.Projectile.*;

public class MainClass extends BasicGame {
    public static final int MAX_FPS = 60;
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;

    private static GameContainer instanceGameContainer;
    private static MainClass instance = null;

    private static double difficulty = 1;
    private static int numberOfFramePerSecond = 60;

    private PortalsManager portalsManager;
    private EnemiesManager enemiesManager;

    private Player player;

    private HUDManager hudManager;

    private PauseMenu menu;
    private FadeToBlack fadeToBlack;

    private Item item;

    public static double getDifficulty(){return difficulty;}

    public static int getNumberOfFramePerSecond() { return numberOfFramePerSecond; }

    public static void nextDifficulty(){difficulty = difficulty+1;}

    public FadeToBlack getFadeToBlack() {
        return fadeToBlack;
    }

    public static boolean isGamePaused() {
        return instance.menu.isActive();
    }

    private void generateRoom() {
        System.out.println("new room");
        BackgroundRenderer.regenerateBackground();
        this.item = null;
        Ranged.allyProjectiles = new ArrayList<>();
        Ranged.enemyProjectiles = new ArrayList<>();

        if(portalsManager.getActualPortal()!=null){
            switch (portalsManager.getActualPortal().getType()) {
                case "boss":
                    enemiesManager.generateBoss();
                    break;
                case "item":
                    item = new Item();
                    break;
                case "classic":
                    enemiesManager.generateEnemies();
                    break;
            }
        }
    }

    public static void setGamePaused(boolean gamePaused) {
        TimeScale.getInGameTimeScale().setTimeScale((gamePaused) ? 0f : 1f);
        instance.menu.setActive(gamePaused);
    }

    private static void triggerGamePaused() {
        TimeScale.getInGameTimeScale().setTimeScale((isGamePaused()) ? 1f : 0f);
        instance.menu.setActive(!isGamePaused());
    }

    public EnemiesManager getEnemiesManager() {
        return enemiesManager;
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

    public static Input getInput() {
        return instanceGameContainer.getInput();
    }

    public MainClass(String name) { super(name); }

    @Override
    public void init(GameContainer gc) {
        instanceGameContainer = gc;
        instance = this;
        this.menu = new PauseMenu(gc);
        this.fadeToBlack = new FadeToBlack(gc);

        this.player = new Player(gc,100,100);
        this.player.setShowDebugRect(true);

        BackgroundRenderer.generateBackground(gc);

        this.portalsManager = new PortalsManager();

        this.enemiesManager = new EnemiesManager(this.player, this.portalsManager);

        this.hudManager = new HUDManager(this.player, this.enemiesManager);
        this.player.addPlayerHurtListener(this.hudManager);

        //generateRoom();
    }

    @Override
    public void update(GameContainer gc, int timeOfOneFrame) {
        numberOfFramePerSecond = gc.getFPS();

        if (!MainClass.isGamePaused()) {
            TimeScale.getInGameTimeScale().setDeltaTime(timeOfOneFrame);

            this.player.update();
            if (!this.player.isDashing()) {
                this.player.checkCollision();
            }
            if( item!=null) {
                item = item.update(this.player);
            }
            updateEnemyProjectile(player);
            updateAllyProjectiles();

            enemiesManager.update();
            portalsManager.update(timeOfOneFrame);

            hudManager.update(timeOfOneFrame);

            fadeToBlack.update(gc);
            if (fadeToBlack.atHalfDuration()) {
                generateRoom();
                portalsManager.hidePortals();
            }
            else if (fadeToBlack.isDone()) {
                TimeScale.resume();
            }
        }
    }

    @Override
    public void keyPressed(int key, char c) {
        if (key == Input.KEY_ESCAPE) {
            triggerGamePaused();
        }
        if (key == Input.KEY_F) {
            portalsManager.setPortalEngaged(true);
        }
    }

    @Override
    public void keyReleased(int key, char c) {
        this.player.keyReleased(key, c);

        if (key == Input.KEY_F) {
            portalsManager.setPortalEngaged(false);
        }
    }

    @Override
    public void render(GameContainer gc, Graphics g) {
        BackgroundRenderer.renderBackground(g, 0, 0);

        if(item!=null){
            item.render(g);
        }

        portalsManager.render(g);
        LivingBeing.sortAndRenderLivingBeings(g);
        this.enemiesManager.render(g);

        this.hudManager.render(g);
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
