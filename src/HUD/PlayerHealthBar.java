package HUD;

import Main.MainClass;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class PlayerHealthBar extends NewHealthBar {

    protected float percentValue;

    public void setValue(float percentValue) {
        this.percentValue = percentValue;
    }

    public PlayerHealthBar(float percentValue) {
        super(200, 20, 5, 10, Color.red, percentValue);
    }

    public void render(Graphics g) {
        int x = healthBarMargin;
        int y = MainClass.HEIGHT - healthBarMargin - healthBarHeight;

        Color tmp = g.getColor();

        g.setColor(HEALTHBAR_BG_COLOR);
        g.fillRect(x, y, healthBarWidth, healthBarHeight);

        int barWidth = (int) (this.percentValue*healthBarWidth);

        g.setColor(healthBarColor);
        g.fillRect(x, y, barWidth, healthBarContentHeight);

        g.setColor(tmp);
    }
}
