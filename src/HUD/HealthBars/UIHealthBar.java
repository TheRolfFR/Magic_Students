package HUD.HealthBars;

import Listeners.LivingBeingHealthListener;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public abstract class UIHealthBar implements LivingBeingHealthListener {
    private static final int HEALTHBAR_BG_OPACITY = (int) (.4f*255);
    private static final Color HEALTHBAR_BG_COLOR = new Color(0,0,0,HEALTHBAR_BG_OPACITY);

    public void render(Graphics g, int healthBarXPos, int healthBarYPos, int healthBarWidth, int healthBarHeight, int healthbarContentWidth, int healthBarContentHeight, Color healthBarColor) {
        g.setColor(HEALTHBAR_BG_COLOR);
        g.fillRect(healthBarXPos, healthBarYPos, healthBarWidth, healthBarHeight);

        g.setColor(healthBarColor);
        g.fillRect(healthBarXPos, healthBarYPos, healthbarContentWidth, healthBarContentHeight);
    }
}
