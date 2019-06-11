package HUD;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public abstract class NewHealthBar {
    protected int healthBarWidth;
    protected int healthBarContentHeight;
    protected int healthBarHeight;

    protected int healthBarMargin;

    protected Color healthBarColor;
    protected static final int HEALTHBAR_BG_OPACITY = (int) (.4f*255);
    protected static final Color HEALTHBAR_BG_COLOR = new Color(0,0,0,HEALTHBAR_BG_OPACITY);

    protected float percentValue;

    public void setPercentValue(float percentValue) {
        this.percentValue = percentValue;
    }

    public NewHealthBar(int healthBarWidth, int healthBarContentHeight, int healthBarBottomSpace, int healthBarMargin, Color healthBarColor, float percentValue) {
        this.healthBarWidth = healthBarWidth;
        this.healthBarContentHeight = healthBarContentHeight;
        this.healthBarHeight = healthBarContentHeight + healthBarBottomSpace;
        this.healthBarMargin = healthBarMargin;
        this.healthBarColor = healthBarColor;
        this.percentValue = percentValue;
    }

    public abstract void render(Graphics g);
}
