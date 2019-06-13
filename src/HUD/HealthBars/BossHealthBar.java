package HUD.HealthBars;

import Entities.LivingBeings.Monsters.IBoss;
import Main.MainClass;
import Managers.EnemiesManager;
import Renderers.FontRenderer;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class BossHealthBar extends UIHealthBar {

    private static final float BOSS_HEALTHBAR_WIDTH_PERCENTAGE = 0.4f;
    private static final int BOSS_HEALTHBAR_WIDTH = (int) (BOSS_HEALTHBAR_WIDTH_PERCENTAGE * MainClass.WIDTH);
    private static final int BOSS_HEALTHBAR_CONTENT_HEIGHT = 20;
    private static final int BOSS_HEALTHBAR_BOTTOM_SPACE = 5;
    private static final int BOSS_HEALTHBAR_MARGIN = 10;
    private static final Color BOSS_HEALTHBAR_COLOR = new Color(0xFDD835);

    private EnemiesManager enemiesManager;

    public BossHealthBar(EnemiesManager enemiesManager) {
        super(BOSS_HEALTHBAR_WIDTH, BOSS_HEALTHBAR_CONTENT_HEIGHT, BOSS_HEALTHBAR_BOTTOM_SPACE, BOSS_HEALTHBAR_MARGIN, BOSS_HEALTHBAR_COLOR);
        this.enemiesManager = enemiesManager;
    }

    @Override
    public int getCurrentValue() {
        return enemiesManager.getBoss().getCurrentHealthPoints();
    }

    @Override
    public int getMaxValue() {
        return enemiesManager.getBoss().getMaxHealthPoints();
    }

    @Override
    public float getPercentValue() {
        return (float) this.getCurrentValue() / (float) enemiesManager.getBoss().getMaxHealthPoints();
    }

    @Override
    public void render(Graphics g) {
        if(enemiesManager.getBoss() != null) {
            int x = (int) ((1f - BOSS_HEALTHBAR_WIDTH_PERCENTAGE)/2f*MainClass.WIDTH);
            int y = healthBarMargin;

            Color tmp = g.getColor();

            g.setColor(HEALTHBAR_BG_COLOR);
            g.fillRect(x, y, healthBarWidth, healthBarHeight);

            int barWidth = (int) (this.getPercentValue()*healthBarWidth);

            g.setColor(healthBarColor);
            g.fillRect(x, y, barWidth, healthBarContentHeight);

            String pointsString = this.getCurrentValue() + "/" + this.getMaxValue();

            FontRenderer.getPixelFontRenderer().setPxSize((int) (1.5f*healthBarHeight));

            int xPoints = (MainClass.WIDTH - FontRenderer.getPixelFont().getWidth(pointsString))/2;
            int yPoints = y + healthBarHeight + healthBarMargin;

            g.setFont(FontRenderer.getPixelFont());
            g.drawString(pointsString, xPoints, yPoints);

            g.setColor(tmp);
        }
    }
}
