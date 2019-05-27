package HUD;

import Main.MainClass;
import org.newdawn.slick.*;

import java.util.LinkedList;
import java.util.List;

/**
 * Pause menu graphic interface
 * @author TheRolf
 */
public class PauseMenu implements MouseListener {
    private Image background;
    private List<Button> buttons;

    private int windowHeight;
    private int windowWidth;
    private int totalButtonsHeight;
    private int totalButtonsWidth;
    private static final int SPACING = 10;

    public boolean isActive;

    /**
     * Returns whether the menu is visible
     * @return whether the menu is visible
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Set active or not the menu
     * @param active the state of the menu
     */
    public void setActive(boolean active) {
        isActive = active;
    }

    /**
     * Default constructor
     * @param gc the GameContainer instance
     */
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

    /**
     * In game rendering
     * @param g the graphics to draw on
     */
    public void render(Graphics g) {
        if(this.isActive()) {
            g.drawImage(this.background, 0, 0);

            for(Button btn : this.buttons) {
                btn.render(g);
            }
        }
    }


    /**
     * Implementation of the MouseListener interface (empty)
     * @param i The amount of the wheel has moved
     */
    @Override
    public void mouseWheelMoved(int i) {

    }

    /**
     * Implementation of the MouseListener interface : triggered if the menu is active
     * @param i The index of the button (starting at 0)
     * @param i1 The x position of the mouse when the button was pressed
     * @param i2 The y position of the mouse when the button was pressed
     * @param i3 The number of times the button was clicked
     */
    @Override
    public void mouseClicked(int i, int i1, int i2, int i3) {
        if(this.isActive()) {
            for(Button btn : this.buttons) {
                btn.mouseClicked(i, i1, i2, i3);
            }
        }
    }

    /**
     * Implementation of the MouseListener interface (empty)
     * @param i button
     * @param i1 x
     * @param i2 y
     */
    @Override
    public void mousePressed(int i, int i1, int i2) {

    }

    /**
     * Implementation of the MouseListener interface (empty)
     * @param i button
     * @param i1 x
     * @param i2 y
     */
    @Override
    public void mouseReleased(int i, int i1, int i2) {

    }

    /**
     * Implementation of the MouseListener interface (empty)
     * @param i oldx
     * @param i1 oldy
     * @param i2 newx
     * @param i3 newy
     */
    @Override
    public void mouseMoved(int i, int i1, int i2, int i3) {

    }

    /**
     * Implementation of the MouseListener interface (empty)
     * @param i oldx
     * @param i1 oldy
     * @param i2 newx
     * @param i3 newy
     */
    @Override
    public void mouseDragged(int i, int i1, int i2, int i3) {

    }

    /**
     * Implementation of the ControlledInputReciever interface (empty)
     * @param input The input instance sending events
     */
    @Override
    public void setInput(Input input) {

    }

    /**
     * Implementation of the ControlledInputReciever interface (empty)
     * @return True if the input listener should receive events
     */
    @Override
    public boolean isAcceptingInput() {
        return true;
    }

    /**
     * Implementation of the ControlledInputReciever interface (empty)
     * Notification that all input events have been sent for this frame
     */
    @Override
    public void inputEnded() {

    }

    /**
     * Implementation of the ControlledInputReciever interface (empty)
     * Notification that input is about to be processed
     */
    @Override
    public void inputStarted() {

    }
}
