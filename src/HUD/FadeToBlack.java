package HUD;

import Main.MainClass;
import org.newdawn.slick.*;

import java.util.ArrayList;

public class FadeToBlack {
    private static final int FADE_DURATION = 2000;
    private static final float FADE_B = 4/ (float) FADE_DURATION;
    private static final float FADE_A = -FADE_B/ (float) FADE_DURATION;

    private boolean isActive;
    private int fadeTimer;

    private ArrayList<FadeToBlackListener> fadeToBlackListeners;

    public void addFadeToBlackListener(FadeToBlackListener listener) {
        this.fadeToBlackListeners.add(listener);
    }

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
        this.isActive = active;
    }

    /**
     * Default constructor
     */
    public FadeToBlack() {
        this.isActive = false;
        this.fadeTimer = 0;

        this.fadeToBlackListeners = new ArrayList<>();
    }

    private int getFadeTimer() { return this.fadeTimer; }
    public int getDuration() { return FADE_DURATION; }
    private boolean willBeAtHalfDuration(int deltaTime) {
        return this.fadeTimer < FADE_DURATION / 2 && this.fadeTimer + deltaTime >= FADE_DURATION / 2;
    }
    public boolean isDone() { return this.getFadeTimer() == this.getDuration(); }

    public void update(int deltaTime) {
        if (this.isActive()) {
            if (fadeTimer < FADE_DURATION) {
                fadeTimer = Math.min(fadeTimer + deltaTime, FADE_DURATION);

                if(willBeAtHalfDuration(deltaTime)) {
                    for(FadeToBlackListener listener : this.fadeToBlackListeners) {
                        listener.atHalf();
                    }
                }
                else if(isDone()) {
                    for(FadeToBlackListener listener : this.fadeToBlackListeners) {
                        listener.atEnd();
                    }
                }
            }
            else {
                fadeTimer = 0;
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
            float opacity = FADE_A * fadeTimer*fadeTimer + FADE_B*fadeTimer; // a*x^2 + b*x^2

            g.setColor(new Color(0, 0, 0, opacity));
            g.fillRect(0, 0, MainClass.instanceGameContainer.getWidth(), MainClass.instanceGameContainer.getHeight());
        }
    }
}
