package HUD;

import org.newdawn.slick.*;

import static java.lang.Math.pow;

public class FadeToBlack {
    private Image background;

    public boolean isActive;

    private int DURATION;
    private int currentCount;

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
    public FadeToBlack(GameContainer gc) {
        try {
            this.DURATION = 120;
            this.currentCount = 0;
            this.isActive = false;
            this.background = new Image(gc.getWidth(), gc.getHeight());

        } catch (SlickException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public int getCurrentCount() { return this.currentCount; }
    public int getDuration() { return this.DURATION; }

    public void update(GameContainer gc) {
        if (this.isActive()) {
            if (currentCount < DURATION) {
                currentCount += 1;

                try {
                    Graphics imageG = this.background.getGraphics();
                    imageG.clear();
                    imageG.setColor(new Color(0, 0, 0, (int) (-17 * pow(this.currentCount, 2) / 240) + 17 * this.currentCount / 2));
                    imageG.fillRect(0, 0, gc.getWidth(), gc.getHeight());
                    imageG.flush();

                } catch (SlickException e) {
                    e.printStackTrace();
                    System.err.println(e.getMessage());
                    System.exit(1);
                }
            }
            else {
                currentCount = 0;
                this.setActive(false);
            }
        }
    }

    /**
     * In game rendering
     * @param g the graphics to draw on
     */
    public void render(Graphics g) {
        if (this.isActive()) {
            g.drawImage(this.background, 0, 0);
        }
    }
}
