package Main;

import Entities.Player;
import Entities.Rusher;
import Entities.SpriteRenderer;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;

import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.Math.round;

public class MainClass extends BasicGame
{
    private Image localImg;
    private Graphics localImgG;
    private Player player;
    private Rusher rusher;

    private static int MAX_FPS = 60;
    public static int WIDTH = 640;
    public static int HEIGHT = 480;

    private boolean keyUp;
    private boolean keyDown;
    private boolean keyLeft;
    private boolean keyRight;

    private SpriteRenderer pokemon;
    private SpriteRenderer rusherRenderer;

    private MainClass(String name) {
        super(name);
    }

    @Override
    public void init(GameContainer gc) throws SlickException {
        this.localImg = new Image(640,480);
        this.localImgG = localImg.getGraphics();

        Image original = new Image("img/24x24.png", false, Image.FILTER_NEAREST);
        original = original.getScaledCopy(2);
        Vector2f tileSize = new Vector2f(48, 48);
        int[] viewFrames =  {2, 2, 2, 2, 2, 2, 2, 2};

        this.player = new Player(100, 100, (int) tileSize.getX(), (int) tileSize.getY(),
                450 / MAX_FPS, 135 / MAX_FPS);

        this.rusher = new Rusher(400, 400, (int) tileSize.getX(), (int) tileSize.getY(),
                150 / MAX_FPS, 135 / MAX_FPS, 100, 5f, 10);

        try {
            this.pokemon = new SpriteRenderer(this.player, tileSize, original.getSubImage(0,
                    (int) tileSize.getY(), original.getWidth(), (int) tileSize.getY()), viewFrames, 1000/12);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            this.rusherRenderer = new SpriteRenderer(this.rusher, tileSize, original.getSubImage(0,
                    (int) tileSize.getY() + 48, original.getWidth(), (int) tileSize.getY()), viewFrames, 1000/12);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(GameContainer gc, int i) {
        if (this.keyUp || this.keyDown || this.keyLeft || this.keyRight) {
            if (this.keyUp) {
                this.player.updateSpeed(new Vector2f(0, -1).scale(this.player.getAccelerationRate()));
            }
            if (this.keyDown) {
                this.player.updateSpeed(new Vector2f(0, 1).scale(this.player.getAccelerationRate()));
            }
            if (this.keyLeft) {
                this.player.updateSpeed(new Vector2f(-1, 0).scale(this.player.getAccelerationRate()));
            }
            if (this.keyRight) {
                this.player.updateSpeed(new Vector2f(1, 0).scale(this.player.getAccelerationRate()));
            }
        }
        else {
            this.player.updateSpeed(this.player.getSpeed().negate().scale(0.2f));
        }
        this.player.move();

        this.rusher.chaseAI(this.player);
    }

    public void keyPressed(int key, char c) {
        switch (key) {
            case Input.KEY_UP:
            case Input.KEY_Z:
                this.keyUp = true;
                break;
            case Input.KEY_DOWN:
            case Input.KEY_S:
                this.keyDown = true;
                break;
            case Input.KEY_LEFT:
            case Input.KEY_Q:
                this.keyLeft = true;
                break;
            case Input.KEY_RIGHT:
            case Input.KEY_D:
                this.keyRight = true;
                break;
        }
    }

    public void keyReleased(int key, char c) {
        switch (key) {
            case Input.KEY_UP:
            case Input.KEY_Z:
                this.keyUp = false;
                break;
            case Input.KEY_DOWN:
            case Input.KEY_S:
                this.keyDown = false;
                break;
            case Input.KEY_LEFT:
            case Input.KEY_Q:
                this.keyLeft = false;
                break;
            case Input.KEY_RIGHT:
            case Input.KEY_D:
                this.keyRight = false;
                break;
        }
    }

    @Override
    public void render(GameContainer gc, Graphics g) {
        this.localImgG.setBackground(new Color(0,0,0,0));
        this.localImgG.clear();

        this.localImgG.setColor(Color.white);

        this.pokemon.render();
        this.rusherRenderer.render();

        this.localImgG.drawRect(round(this.player.getPosition().x), round(this.player.getPosition().y),
                this.player.getWidth(), this.player.getHeight());
        this.localImgG.drawRect(round(this.rusher.getPosition().x), round(this.rusher.getPosition().y),
                this.rusher.getWidth(), this.rusher.getHeight());
        this.localImgG.flush();

        g.drawImage(localImg, 0, 0);
        System.out.println("player health " + this.player.getHp() + " / rusher health " + this.rusher.getHp());
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