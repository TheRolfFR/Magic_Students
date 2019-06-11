package Managers;

import HUD.NewHealthBar;
import Main.MainClass;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class BossHealthBar extends NewHealthBar {

    protected static final float HEALTHBAR_WIDTH_PERCENTAGE = 0.4f;

    public BossHealthBar(int healthBarWidth, int healthBarContentHeight, int healthBarBottomSpace, int healthBarMargin, float percentValue) {
        super((int) (HEALTHBAR_WIDTH_PERCENTAGE * MainClass.WIDTH), healthBarContentHeight, healthBarBottomSpace, healthBarMargin, Color.yellow, percentValue);
    }

    @Override
    public void render(Graphics g) {
        int x = (int) ((1f - HEALTHBAR_WIDTH_PERCENTAGE)/2f*MainClass.WIDTH);
        int y = healthBarMargin;

        Color tmp = g.getColor();

        g.setColor(HEALTHBAR_BG_COLOR);
        g.drawRect(x, y, healthBarWidth, healthBarHeight);

        g.setColor(healthBarColor);
        g.drawRect(x, y, healthBarWidth, healthBarContentHeight);

        g.setColor(tmp);
    }
}
