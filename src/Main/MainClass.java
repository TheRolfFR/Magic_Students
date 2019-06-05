package Main;

import Entities.*;
import HUD.HealthBar;
import HUD.PauseMenu;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import static Entities.Projectile.*;
import static java.lang.Math.round;

public class MainClass extends BasicGame
{
    public Player player;
    public static ArrayList<Monster> enemies = new ArrayList<>();

    public static final int MAX_FPS = 60;
    public static final int WIDTH = 640;
    public static final int HEIGHT = 480;

    private static TimeScale inGameTimeScale = new TimeScale(1f);
    private static TimeScale guiTimeScale = new TimeScale(1f);

    private static GameContainer instanceGameContainer;
    private static MainClass instance = null;

    private HealthBar healthBar;

    private PauseMenu menu;

    public static boolean isGamePaused() {
        return instance.menu.isActive();
    }

    public void generateEnemies(Image skin, Vector2f tileSize, int[] viewFrames){
        Random random = new Random();
        int randomX;
        int randomY;
        for(int i = 0; i< random.nextInt(9)+1; i++){
            randomX = random.nextInt(Math.round(WIDTH-2*tileSize.getX())) + (int) tileSize.getX();
            randomY = random.nextInt(Math.round(HEIGHT-2*tileSize.getY())) + (int) tileSize.getY();
            switch(random.nextInt(2)){
                case 0 :
                    Bowman tmpb = new Bowman(randomX, randomY, (int) tileSize.getX(), (int) tileSize.getY(), 250/MAX_FPS, 60/MAX_FPS, 100,10,5,(int) Math.round(0.4*tileSize.getY()));
                    tmpb.setRenderer(new SpriteRenderer(tmpb, tileSize, skin.getSubImage(0,
                            (int) tileSize.getY()*3, skin.getWidth(), (int) tileSize.getY()), viewFrames, 1000/12));
                    this.enemies.add(tmpb);
                    break;
                case 1 :
                    Rusher tmpr = new Rusher(randomX, randomY, (int) tileSize.getX(), (int) tileSize.getY(), 250/MAX_FPS, 60/MAX_FPS, 100,10,5,(int) Math.round(0.4*tileSize.getY()));
                    tmpr.setRenderer(new SpriteRenderer(tmpr, tileSize, skin.getSubImage(0,
                            (int) tileSize.getY()*2, skin.getWidth(), (int) tileSize.getY()), viewFrames, 1000/12));
                    this.enemies.add(tmpr);
                    break;
                default: break;
            }
        }
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

        Image original = new Image("img/wizard_48x48.png", false, Image.FILTER_NEAREST, Color.red);
        original = original.getScaledCopy(2f);
        Vector2f tileSize = new Vector2f(96, 96);
        int[] viewFrames =  {10, 10, 10, 10};

        this.player = new Player(gc,100,
                100,
                (int) tileSize.getX(),
                (int) tileSize.getY(),
                450 / MAX_FPS,
                135 / MAX_FPS,
                (int) Math.round(0.4*tileSize.getY())
        );
        this.player.setRenderer(new SpriteRenderer(
                this.player,
                tileSize,
                original,
                viewFrames,
                1000/12)
        );
        this.player.setShowDebugRect(true);

        generateEnemies(new Image("img/24x24.png", false, Image.FILTER_NEAREST).getScaledCopy(2).getSubImage(48, 0, 384, 48), new Vector2f(48,48), new int[] {2, 2, 2, 2});


        this.healthBar = new HealthBar(this.player);
        SceneRenderer.generateBackground("img/ground.png", gc);

        gc.getInput().addKeyListener(this.player);

        System.out.println(Configuration.getConfigurationFile().getJSONObject("glossary").getString("title"));
    }

    @Override
    public void update(GameContainer gc, int i) {
        inGameTimeScale.setDeltaTime(i);

        this.player.update();
        this.player.checkCollision();
        updateEnemyProjectile(player);
        updateAllyProjectiles();

        for(Monster enemy : this.enemies){
            enemy.update(this.player);
            enemy.checkCollision();
            if (this.player.isDead()){
                setGamePaused(true);
            }
        }

        for (int j=0; j<enemies.size(); j++) {
            if (enemies.get(j).isDead()) {
                System.out.println("You killed an enemy");
                enemies.get(j).setRenderer(null);
                this.enemies.remove(this.enemies.get(j));
            }
        }
    }

    @Override
    public void keyPressed(int key, char c) {

        if (key == Input.KEY_ESCAPE) {
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
        for (Monster enemy: enemies){
            enemy.setHealthBar(new HealthBar(enemy ,(int) enemy.getPosition().x, (int) enemy.getPosition().y + (int) round(enemy.getRadius()*2.5)));
            enemy.getHealthBar().render(g);
        }
        this.menu.render(g);
    }

    public static void main(String[] args) {
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
