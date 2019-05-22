package Main;

import org.newdawn.slick.*;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MainClass extends BasicGame
{
    private Image localImg;
    private Graphics localImgG;


    private MainClass(String name) {
        super(name);
    }

    @Override
    public void init(GameContainer gc) throws SlickException {
        this.localImg = new Image(640,480);
        this.localImgG = localImg.getGraphics();
    }

    @Override
    public void update(GameContainer gc, int i) {}

    @Override
    public void render(GameContainer gc, Graphics g) {
        this.localImgG.setBackground(new Color(0,0,0,0));
        this.localImgG.clear();

        this.localImgG.setColor(Color.red);
        for (int i=0; i<500; i++) {
            this.localImgG.drawRect((int) (Math.random() * 620), (int) (Math.random() * 460), 20, 20);
        }
        this.localImgG.flush();

        g.drawImage(localImg, 0, 0);

        g.drawString("Damn son!", 10, 30);
    }

    public static void main(String[] args) {
        try {
            AppGameContainer appgc;
            appgc = new AppGameContainer(new MainClass("Simple Slick Game"));
            appgc.setDisplayMode(640, 480, false);
            appgc.start();
        }
        catch (SlickException ex)
        {
            Logger.getLogger(MainClass.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}