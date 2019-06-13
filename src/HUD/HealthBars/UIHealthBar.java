package HUD.HealthBars;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public abstract class UIHealthBar {
    private static final int HEALTHBAR_BG_OPACITY = (int) (.4f*255);
    protected static final Color HEALTHBAR_BG_COLOR = new Color(0,0,0,HEALTHBAR_BG_OPACITY);

    protected int healthBarWidth;
    protected int healthBarContentHeight;
    protected int healthBarHeight;

    protected int healthBarMargin;

    protected Color healthBarColor;

    public abstract int getCurrentValue();
    public abstract int getMaxValue();
    public abstract float getPercentValue();

    public int getHealthBarWidth() {
        return healthBarWidth;
    }

    public int getHealthBarHeight() {
        return healthBarHeight;
    }

    public int getHealthBarMargin() {
        return healthBarMargin;
    }

    public UIHealthBar(int healthBarWidth, int healthBarContentHeight, int healthBarBottomSpace, int healthBarMargin, Color healthBarColor) {
        this.healthBarWidth = healthBarWidth;
        this.healthBarContentHeight = healthBarContentHeight;
        this.healthBarHeight = healthBarContentHeight + healthBarBottomSpace;
        this.healthBarMargin = healthBarMargin;
        this.healthBarColor = healthBarColor;
    }

    public abstract void render(Graphics g);
}
