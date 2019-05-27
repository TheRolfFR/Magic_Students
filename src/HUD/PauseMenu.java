package HUD;

import Main.MainClass;
import org.newdawn.slick.*;

import java.util.LinkedList;
import java.util.List;

public class PauseMenu implements MouseListener {
    private Image background;
    private List<Button> buttons;

    private int windowHeight;
    private int windowWidth;
    private int totalButtonsHeight;
    private int totalButtonsWidth;
    private static final int SPACING = 10;

    public boolean isActive;

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public PauseMenu(GameContainer gc) {
        try {
            this.windowHeight = gc.getHeight();
            this.windowWidth = gc.getWidth();

            this.background = new Image(gc.getWidth(), gc.getHeight());

            this.isActive = false;

            Graphics imageG = this.background.getGraphics();
            imageG.clear();
            imageG.setColor(new Color(0, 0, 0, 128));
            imageG.fillRect(0, 0, gc.getWidth(), gc.getHeight());
            imageG.flush();

            this.buttons = new LinkedList<Button>();
            this.buttons.add(new Button(gc, "resume", new MouseListener() {
                @Override
                public void mouseWheelMoved(int i) {

                }

                @Override
                public void mouseClicked(int button, int x, int y, int clickCount) {
                    MainClass.setGamePaused(false);
                }

                @Override
                public void mousePressed(int i, int i1, int i2) {

                }

                @Override
                public void mouseReleased(int i, int i1, int i2) {

                }

                @Override
                public void mouseMoved(int i, int i1, int i2, int i3) {

                }

                @Override
                public void mouseDragged(int i, int i1, int i2, int i3) {

                }

                @Override
                public void setInput(Input input) {

                }

                @Override
                public boolean isAcceptingInput() {
                    return false;
                }

                @Override
                public void inputEnded() {

                }

                @Override
                public void inputStarted() {

                }
            }));
            this.buttons.add(new Button(gc, "settings", new MouseListener() {
                @Override
                public void mouseWheelMoved(int i) {

                }

                @Override
                public void mouseClicked(int i, int i1, int i2, int i3) {
                    System.out.println("go to settings menu");
                }

                @Override
                public void mousePressed(int i, int i1, int i2) {

                }

                @Override
                public void mouseReleased(int i, int i1, int i2) {

                }

                @Override
                public void mouseMoved(int i, int i1, int i2, int i3) {

                }

                @Override
                public void mouseDragged(int i, int i1, int i2, int i3) {

                }

                @Override
                public void setInput(Input input) {

                }

                @Override
                public boolean isAcceptingInput() {
                    return false;
                }

                @Override
                public void inputEnded() {

                }

                @Override
                public void inputStarted() {

                }
            }));
            this.buttons.add(new Button(gc, "exit", new MouseListener() {
                @Override
                public void mouseWheelMoved(int i) {

                }

                @Override
                public void mouseClicked(int i, int i1, int i2, int i3) {
                    System.exit(0);
                }

                @Override
                public void mousePressed(int i, int i1, int i2) {

                }

                @Override
                public void mouseReleased(int i, int i1, int i2) {

                }

                @Override
                public void mouseMoved(int i, int i1, int i2, int i3) {

                }

                @Override
                public void mouseDragged(int i, int i1, int i2, int i3) {

                }

                @Override
                public void setInput(Input input) {

                }

                @Override
                public boolean isAcceptingInput() {
                    return true;
                }

                @Override
                public void inputEnded() {

                }

                @Override
                public void inputStarted() {

                }
            }));

            this.totalButtonsHeight = (this.buttons.size() - 1) * SPACING;
            this.totalButtonsWidth = 0;

            int offsetY = (this.windowHeight - this.totalButtonsHeight) / 2;
            for(Button btn : this.buttons) {
                this.totalButtonsHeight += btn.getHeight();
                this.totalButtonsWidth = Math.max(totalButtonsWidth, btn.getWidth());

                btn.setLocation((this.windowWidth - btn.getWidth())/2, offsetY);

                offsetY += btn.getHeight() + SPACING;
            }

            gc.getInput().addMouseListener(this);
        } catch (SlickException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
            System.exit(1);
        }

    }

    public void render(Graphics g) {
        if(this.isActive()) {
            g.drawImage(this.background, 0, 0);

            for(Button btn : this.buttons) {
                btn.render(g);
            }
        }
    }


    @Override
    public void mouseWheelMoved(int i) {

    }

    @Override
    public void mouseClicked(int i, int i1, int i2, int i3) {
        if(this.isActive()) {
            for(Button btn : this.buttons) {
                btn.mouseClicked(i, i1, i2, i3);
            }
        }
    }

    @Override
    public void mousePressed(int i, int i1, int i2) {

    }

    @Override
    public void mouseReleased(int i, int i1, int i2) {

    }

    @Override
    public void mouseMoved(int i, int i1, int i2, int i3) {

    }

    @Override
    public void mouseDragged(int i, int i1, int i2, int i3) {

    }

    @Override
    public void setInput(Input input) {

    }

    @Override
    public boolean isAcceptingInput() {
        return true;
    }

    @Override
    public void inputEnded() {

    }

    @Override
    public void inputStarted() {

    }
}
