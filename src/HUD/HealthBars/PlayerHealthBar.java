package HUD.HealthBars;

import Entities.LivingBeings.Player;
import Main.MainClass;
import Renderers.FontRenderer;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class PlayerHealthBar extends UIHealthBar {

    protected static final int PLAYER_HEALTHBAR_WIDTH = 100;
    protected static final int PLAYER_HEALTHBAR_CONTENT_HEIGHT = PLAYER_HEALTHBAR_WIDTH/10;
    protected static final int PLAYER_HEALTHBAR_BOTTOM_SPACE = PLAYER_HEALTHBAR_CONTENT_HEIGHT/3;
    protected static final int PLAYER_HEALTHBAR_MARGIN = 10;
    protected static final Color PLAYER_HEALTHBAR_COLOR = new Color(0xd32f2f);

    protected Player player;

    public PlayerHealthBar(Player player) {
        super(PLAYER_HEALTHBAR_WIDTH, PLAYER_HEALTHBAR_CONTENT_HEIGHT, PLAYER_HEALTHBAR_BOTTOM_SPACE, PLAYER_HEALTHBAR_MARGIN, PLAYER_HEALTHBAR_COLOR);
        this.player = player;
    }

    @Override
    public int getCurrentValue() {
        return player.getCurrentHealthPoints();
    }

    @Override
    public int getMaxValue() {
        return player.getMaxHealthPoints();
    }

    @Override
    public float getPercentValue() {
        return ((float) this.getCurrentValue()) / ((this).getMaxValue());
    }

    public void render(Graphics g) {
        int x = healthBarMargin;
        int y = MainClass.HEIGHT - healthBarMargin - healthBarHeight;

        Color tmp = g.getColor();

        g.setColor(HEALTHBAR_BG_COLOR);
        g.fillRect(x, y, healthBarWidth, healthBarHeight);

        int barWidth = (int) (this.getPercentValue()*healthBarWidth);

        g.setColor(healthBarColor);
        g.fillRect(x, y, barWidth, healthBarContentHeight);

        String pointsString = "" + this.getCurrentValue() + '/' + this.getMaxValue();

        FontRenderer.getPixelFontRenderer().setPxSize((int) (this.healthBarHeight*1.25f));
        g.setFont(FontRenderer.getPixelFont());

        int yPoints = y - FontRenderer.getPixelFont().getHeight(pointsString);

        g.setColor(Color.black);
        g.drawString(pointsString, x, yPoints);

        g.setColor(tmp);
    }
}
