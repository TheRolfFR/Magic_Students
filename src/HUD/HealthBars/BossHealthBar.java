package HUD.HealthBars;

import Entities.LivingBeings.Monsters.IBoss;
import Main.MainClass;
import Renderers.FontRenderer;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class BossHealthBar extends NewHealthBar {

    protected static final float HEALTHBAR_WIDTH_PERCENTAGE = 0.4f;
    protected static final Color BOSS_HEALTHBAR_COLOR = Color.yellow;

    protected IBoss boss;

    public BossHealthBar(IBoss boss) {
        super((int) (HEALTHBAR_WIDTH_PERCENTAGE * MainClass.WIDTH), 20, 5, 10, BOSS_HEALTHBAR_COLOR);
    }

    @Override
    public int getCurrentValue() {
        return boss.getCurrentHealthPoints();
    }

    @Override
    public int getMaxValue() {
        return boss.getMaxHealthPoints();
    }

    @Override
    public float getPercentValue() {
        return ((float) this.getCurrentValue()) / ((float) boss.getMaxHealthPoints());
    }

    @Override
    public void render(Graphics g) {
        int x = (int) ((1f - HEALTHBAR_WIDTH_PERCENTAGE)/2f*MainClass.WIDTH);
        int y = healthBarMargin;

        Color tmp = g.getColor();

        g.setColor(HEALTHBAR_BG_COLOR);
        g.drawRect(x, y, healthBarWidth, healthBarHeight);

        int barWidth = (int) (this.getPercentValue()*healthBarWidth);

        g.setColor(healthBarColor);
        g.drawRect(x, y, barWidth, healthBarContentHeight);

        String pointsString = this.getCurrentValue() + "/" + this.getMaxValue();

        FontRenderer.getPixelFontRenderer().setPxSize(2*healthBarHeight);

        int xPoints = (MainClass.WIDTH - FontRenderer.getPixelFont().getWidth(pointsString))/2;
        int yPoints = x + healthBarHeight + healthBarMargin;

        g.setFont(FontRenderer.getPixelFont());
        g.drawString(pointsString, xPoints, yPoints);

        g.setColor(tmp);
    }
}
