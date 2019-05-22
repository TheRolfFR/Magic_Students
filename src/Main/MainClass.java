package Main;

import Entities.Player;
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

    private boolean keyUp;
    private boolean keyDown;
    private boolean keyLeft;
    private boolean keyRight;


    private MainClass(String name) {
        super(name);
    }

    @Override
    public void init(GameContainer gc) throws SlickException {
        this.localImg = new Image(640,480);
        this.localImgG = localImg.getGraphics();
        this.player = new Player(100, 100, 3, 0.5f);
    }

    @Override
    public void update(GameContainer gc, int i) {
        if (this.keyUp || this.keyDown || this.keyLeft || this.keyRight) {
            if (this.keyUp) {
                this.player.updateSpeed(new Vector2f(0, -1));
            }
            if (this.keyDown) {
                this.player.updateSpeed(new Vector2f(0, 1));
            }
            if (this.keyLeft) {
                this.player.updateSpeed(new Vector2f(-1, 0));
            }
            if (this.keyRight) {
                this.player.updateSpeed(new Vector2f(1, 0));
            }
        }
        else {
            this.player.updateSpeed(this.player.getSpeed().negate().normalise());
        }
        this.player.move();
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
        this.localImgG.drawRect(round(this.player.getPosition().x), round(this.player.getPosition().y), 20, 20);
        this.localImgG.flush();

        g.drawImage(localImg, 0, 0);
    }

    public static void main(String[] args) {
        try {
            AppGameContainer appgc;
            appgc = new AppGameContainer(new MainClass("Magic Students"));
            appgc.setDisplayMode(640, 480, false);
            appgc.setTargetFrameRate(90);
            appgc.start();
        }
        catch (SlickException ex)
        {
            Logger.getLogger(MainClass.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}