package Main;

import Entities.Player;
import Entities.Rusher;
import Entities.Snowball;
import Entities.SpriteRenderer;
import HUD.HealthBar;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;

import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.Math.round;

public class MainClass extends BasicGame
{
    private Player player;
    private Rusher rusher;

    private static int MAX_FPS = 60;
    public static int WIDTH = 640;
    public static int HEIGHT = 480;

    private HealthBar healthBar;

    private SpriteRenderer rusherRenderer;

    private Snowball snowball;

    private MainClass(String name) {
        super(name);
    }

    @Override
    public void init(GameContainer gc) throws SlickException {
        Image original = new Image("img/24x24.png", false, Image.FILTER_NEAREST);
        original = original.getScaledCopy(2);
        Vector2f tileSize = new Vector2f(48, 48);
        int[] viewFrames =  {2, 2, 2, 2, 2, 2, 2, 2};

        this.player = new Player(100, 100, (int) tileSize.getX(), (int) tileSize.getY(),
                450 / MAX_FPS, 135 / MAX_FPS);

        this.rusher = new Rusher(400, 400, (int) tileSize.getX(), (int) tileSize.getY(),
                150 / MAX_FPS, 60 / MAX_FPS, 100, 5f, 10);

        this.player.setRenderer(new SpriteRenderer(this.player, tileSize, original.getSubImage(0,
                (int) tileSize.getY(), original.getWidth(), (int) tileSize.getY()), viewFrames, 1000/12));

        this.rusherRenderer = new SpriteRenderer(this.rusher, tileSize, original.getSubImage(0,
                (int) tileSize.getY()*2, original.getWidth(), (int) tileSize.getY()), viewFrames, 1000/12);

        this.snowball = new Snowball(200, 200, 250/MAX_FPS, 135/MAX_FPS, "img/snowball.png", new Vector2f(1, 0));

        healthBar = new HealthBar(this.player);
        SceneRenderer.generateBackground("img/ground.png", gc);
    }

    @Override
    public void update(GameContainer gc, int i) {
        this.player.update(gc, i);
        this.rusher.chaseAI(this.player);
        this.snowball.updateSpeed(new Vector2f(1, 0));
    }

    @Override
    public void keyPressed(int key, char c) {
        this.player.keyPressed(key, c);
    }

    @Override
    public void keyReleased(int key, char c) {
        this.player.keyReleased(key, c);
    }

    @Override
    public void render(GameContainer gc, Graphics g) {
        SceneRenderer.renderBackground(g, 0, 0);

        this.player.render(g);
        this.rusherRenderer.render();
        this.snowball.render(g);

        g.setColor(Color.white);
        g.drawRect(round(this.rusher.getPosition().x), round(this.rusher.getPosition().y),
                this.rusher.getWidth(), this.rusher.getHeight());

        healthBar.render(g);
        //System.out.println("player health " + this.player.getHp() + " / rusher health " + this.rusher.getHp());
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