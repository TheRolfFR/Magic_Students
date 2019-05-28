package Main;

import Entities.*;
import HUD.Button;
import HUD.HealthBar;
import HUD.PauseMenu;
import org.lwjgl.Sys;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.Math.round;

public class MainClass extends BasicGame
{
    private Player player;
    private ArrayList<Monster> enemies = new ArrayList<>();

    public static int MAX_FPS = 60;
    public static int WIDTH = 640;
    public static int HEIGHT = 480;

    private static TimeScale inGameTimeScale = new TimeScale(1f);
    private static TimeScale guiTimeScale = new TimeScale(1f);

    private static GameContainer instanceGameContainer;
    private static MainClass instance = null;

    private HealthBar healthBar;

    private PauseMenu menu;

    public static boolean isGamePaused() {
        return instance.menu.isActive();
    }

    public static void setGamePaused(boolean gamePaused) {
        getInGameTimeScale().setTimeScale((gamePaused) ? 0f : 1f);
        instance.menu.setActive(gamePaused);
    }

    public static void triggerGamePaused() {
        getInGameTimeScale().setTimeScale((isGamePaused()) ? 1f : 0f);
        instance.menu.setActive(isGamePaused() == false);
    }

    public static TimeScale getInGameTimeScale() {
        return inGameTimeScale;
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

        Image original = new Image("img/24x24.png", false, Image.FILTER_NEAREST);
        original = original.getScaledCopy(2);
        Vector2f tileSize = new Vector2f(48, 48);
        int[] viewFrames =  {2, 2, 2, 2, 2, 2, 2, 2};

        this.player = new Player(gc,100, 100, (int) tileSize.getX(), (int) tileSize.getY(),
                450 / MAX_FPS, 135 / MAX_FPS, (int) round(0.4*tileSize.getY()));
        this.player.setRenderer(new SpriteRenderer(this.player, tileSize, original.getSubImage(0,
                (int) tileSize.getY(), original.getWidth(), (int) tileSize.getY()), viewFrames, 1000/12));
        //this.player.setShowDebugRect(true);

        enemies.add(new Rusher(400, 400, (int) tileSize.getX(), (int) tileSize.getY(),
                150 / MAX_FPS, 60 / MAX_FPS, 100, 5f,
                10, (int) round(0.4*tileSize.getY())));
        this.enemies.get(0).setRenderer(new SpriteRenderer(this.enemies.get(0), tileSize, original.getSubImage(0,
                (int) tileSize.getY()*2, original.getWidth(), (int) tileSize.getY()), viewFrames, 1000/12));
        //this.rusher.setShowDebugRect(true);

        this.healthBar = new HealthBar(this.player);
        SceneRenderer.generateBackground("img/ground.png", gc);

        gc.getInput().addKeyListener(this.player);

        System.out.println(Configuration.getConfigurationFile().getJSONObject("glossary").getString("title"));
    }

    @Override
    public void update(GameContainer gc, int i) {
        this.player.update();
        for(Monster enemy : this.enemies){
            enemy.update(this.player);
            if (enemy.collides(player)){
                enemy.collidingAction(player);
                if(this.player.isDead()){
                    triggerGamePaused();
                }
            }
        }

        for(int j=0; j<this.enemies.size(); j++){
            this.player.checkCollidesProjectile(this.enemies.get(j));
            if(this.enemies.get(j).isDead()){
                System.out.println("You killed an enemy");
                this.enemies.get(j).setRenderer(null);
                this.enemies.remove(this.enemies.get(j));
            }
        }
    }

    @Override
    public void keyPressed(int key, char c) {

        if(key == Input.KEY_ESCAPE) {
            triggerGamePaused();
        }
    }

    @Override
    public void keyReleased(int key, char c) {
        this.player.keyReleased(key, c);
    }

    @Override
    public void render(GameContainer gc, Graphics g) {
        SceneRenderer.renderBackground(g, 0, 0);

        LivingBeing.sortAndRenderLivingBeings(g);

        this.healthBar.render(g);
        for(Monster enemy : enemies){
            enemy.setHealthBar(new HealthBar(enemy ,(int) enemy.getPosition().x, (int) enemy.getPosition().y + (int) round(enemy.getRadius()*2.5)));
            enemy.getHealthBar().render(g);
        }
        this.menu.render(g);
    }

    public static void main(String[] args) {
        float arr[] = {2,3,5,6,6,0,7};
        try {
            AppGameContainer appgc;
            appgc = new AppGameContainer(new MainClass("Magic Students"));
            appgc.setDisplayMode(WIDTH, HEIGHT, false);
            appgc.setTargetFrameRate(MAX_FPS);
            appgc.start();
        }
        catch (SlickException ex)
        {
            Logger.getLogger(MainClass.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}