package HUD;

import org.lwjgl.Sys;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

public class Button {

    private Image image;
    private int height;
    private int width;
    private Shape shape;
    private String text;

    private MouseListener listener;

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public Button(GameContainer gc, String text, MouseListener listener) {
        try {
            this.width = gc.getGraphics().getFont().getWidth(text);
            this.height = gc.getGraphics().getFont().getHeight(text);

            this.listener = listener;

            this.text = text;

            this.image = new Image(this.width, this.height);
            Graphics imageG = this.image.getGraphics();
            imageG.clear();
            imageG.setColor(Color.white);
            imageG.drawString(text, 0, 0);
            imageG.flush();

            this.shape = new Rectangle(-500, -500, this.width, this.height);
        } catch (SlickException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void setLocation(int x, int y) {
        this.shape.setLocation(x, y);
    }

    public void render(Graphics g) {
        g.drawImage(this.image, this.shape.getX(), this.shape.getY());
    }

    public void mouseClicked(int button, int x, int y, int clickCount) {
        if(shape.contains(x, y)) {
            listener.mouseClicked(button, x, y, clickCount);
        }
    }
}
