package HUD;

import Listeners.AttackCooldownListener;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class AttackVisual implements AttackCooldownListener {
    public static final int ICON_SIZE = 60;
    private static final float ICON_UNAVAILABLE_OPACITY = 0.5f;
    private static final float ICON_BACKGROUND_OPACITY = 0.4f;
    private static final Color ICON_BACKGROUND_COLOR = new Color(0, 0, 0, ICON_BACKGROUND_OPACITY);

    private Image icon;

    private int backgroundWidth;

    private int framesHidden;
    private int numberOfFramesDisplayed;

    public AttackVisual(String imgPath) {
        this.initialize(imgPath);
        this.framesHidden = 0;
    }

    public AttackVisual(String imgPath, int framesHidden) {
        this.initialize(imgPath);
        this.framesHidden = framesHidden;
    }

    private void initialize(String imgPath) {
        try {
            this.icon = new Image(imgPath, false, Image.FILTER_NEAREST).getScaledCopy(ICON_SIZE, ICON_SIZE);
            this.backgroundWidth = 0;
            this.numberOfFramesDisplayed = 0;
        } catch (SlickException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void render(Graphics g, int x, int y) {
        g.setColor(ICON_BACKGROUND_COLOR);

        if (this.framesHidden == 0) {
            g.fillRect(x, y, backgroundWidth, ICON_SIZE);
        } else {
            this.numberOfFramesDisplayed += 1;
            if(this.numberOfFramesDisplayed > this.framesHidden) {
                this.onCooldownEnd();
            }
        }

        g.drawImage(icon, x, y);
    }

    @Override
    public void onCooldownStart() {
        icon.setAlpha(ICON_UNAVAILABLE_OPACITY);
        this.backgroundWidth = ICON_SIZE;
        this.numberOfFramesDisplayed = 0;
    }

    @Override
    public void onCooldownUpdate(float currentValue, float maxValue) {
        this.backgroundWidth = (int) Math.floor(currentValue / maxValue * ICON_SIZE);
        if(this.backgroundWidth < 1) {
            this.onCooldownEnd();
        }
    }

    @Override
    public void onCooldownEnd() {
        icon.setAlpha(1f);
        this.backgroundWidth = 0;
    }
}
