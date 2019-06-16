package Main;

import Entities.LivingBeings.LivingBeing;
import Entities.LivingBeings.Monsters.Monster;
import Entities.LivingBeings.Monsters.Ranged.Ranged;
import Entities.LivingBeings.Player;
import Entities.Projectiles.Projectile;
import HUD.EndScreen;
import HUD.FadeToBlack;
import HUD.PauseMenu;
import Listeners.FadeToBlackListener;
import Managers.*;
import Renderers.BackgroundRenderer;
import org.newdawn.slick.*;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainClass extends BasicGame {
    public static final int MAX_FPS = 60;
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;

    private static final String ICON_PATH = "img/icons/icon_";
    private static final int[] ICON_SIZES = { 16, 64, 32, 24, 48};

    public static GameContainer instanceGameContainer;
    private static MainClass instance = null;

    private static int numberOfFramePerSecond = 60;

    private Player player;

    private ItemManager itemManager;
    private PortalsManager portalsManager;
    private EnemiesManager enemiesManager;
    private HUDManager hudManager;
    private AttackVisualsManager attackVisualsManager;

    private PauseMenu pauseMenu;

    private FadeToBlack fadeToBlack;

    public static int getNumberOfFramePerSecond() { return numberOfFramePerSecond; }

    public static void setGamePaused() { instance.pauseMenu.setActive(true);}
    public static boolean isGamePaused() {
        return instance.pauseMenu.isActive();
    }

    private void generateRoom() {
        BackgroundRenderer.regenerateBackground();
        Ranged.allyProjectiles = new ArrayList<>();
        Ranged.enemyProjectiles = new ArrayList<>();

        if (portalsManager.getLatestPortal() != null) {
            switch (portalsManager.getLatestPortal().getType()) {
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

    private static String[] getIcons() {
        String[] array = new String[ICON_SIZES.length];
        for(int i = 0; i < ICON_SIZES.length; i++) {
            array[i] = ICON_PATH + ICON_SIZES[i] + "x" + ICON_SIZES[i] + ".png";
        }

        return array;
    }

    private void setGamePaused(boolean gamePaused) {
        TimeScale.getInGameTimeScale().setTimeScale((gamePaused) ? 0f : 1f);
        instance.pauseMenu.setActive(gamePaused);
    }

    private void triggerGamePaused() {
        TimeScale.getInGameTimeScale().setTimeScale((isGamePaused()) ? 1f : 0f);
        this.pauseMenu.setActive(!isGamePaused());
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
        this.pauseMenu = new PauseMenu(gc, (i, i1, i2, i3) -> this.setGamePaused(false));
        this.fadeToBlack = new FadeToBlack();

        this.player = new Player(gc, WIDTH / 2, HEIGHT / 2);
        this.player.setShowDebugRect(true);

        BackgroundRenderer.generateBackground(gc);

        this.itemManager = new ItemManager(this.player);
        this.portalsManager = new PortalsManager(gc, this.player, this.fadeToBlack);
        this.enemiesManager = new EnemiesManager(this.player, this.portalsManager);
        this.hudManager = new HUDManager(this.player, this.enemiesManager);
        this.attackVisualsManager = new AttackVisualsManager();

        this.fadeToBlack.addFadeToBlackListener(new FadeToBlackListener() {
            @Override
            public void atHalf() {
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
            this.triggerGamePaused();
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
        this.attackVisualsManager.render(g);

        this.fadeToBlack.render(g);

        this.pauseMenu.render(g);

        EndScreen.getInstance().Render(g);
    }

    public static void main(String[] args) {
        try {
            AppGameContainer appgc;
            appgc = new AppGameContainer(new MainClass("Magic Students"));
            appgc.setDisplayMode(WIDTH, HEIGHT, false);
            appgc.setTargetFrameRate(MAX_FPS);
            appgc.setIcons(getIcons());
            appgc.start();
        }
        catch (SlickException ex) {
            Logger.getLogger(MainClass.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
