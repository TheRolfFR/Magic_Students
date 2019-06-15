package HUD.HealthBars;

import Listeners.LivingBeingHurtListener;
import Entities.LivingBeings.LivingBeing;
import Main.MainClass;
import Renderers.FontRenderer;
import org.newdawn.slick.*;

public class BossHealthBar extends UIHealthBar implements LivingBeingHurtListener {

    private static final float BOSS_HEALTHBAR_WIDTH_PERCENTAGE = 0.4f;
    private static final int BOSS_HEALTHBAR_WIDTH = (int) (BOSS_HEALTHBAR_WIDTH_PERCENTAGE * MainClass.WIDTH);
    private static final int BOSS_HEALTHBAR_CONTENT_HEIGHT = 20;
    private static final int BOSS_HEALTHBAR_BOTTOM_SPACE = 5;
    private static final int BOSS_HEALTHBAR_HEIGHT = BOSS_HEALTHBAR_CONTENT_HEIGHT + BOSS_HEALTHBAR_BOTTOM_SPACE;

    private static final int BOSS_HEALTHBAR_MARGIN = 10;

    private static final Color BOSS_HEALTHBAR_COLOR = new Color(0xFDD835);

    private static final int BOSS_HEALTHBAR_XPOS = (int) ((1f - BOSS_HEALTHBAR_WIDTH_PERCENTAGE)/2f*MainClass.WIDTH);
    private static final int BOSS_HEALTHBAR_YPOS = BOSS_HEALTHBAR_MARGIN;

    private static final float BOSS_HEALTHPOINTS_FONT_FACTOR = 1.5f;

    private static final int BOSS_HEALTHPOINTS_YPOS = BOSS_HEALTHBAR_YPOS + BOSS_HEALTHBAR_CONTENT_HEIGHT + BOSS_HEALTHBAR_BOTTOM_SPACE + BOSS_HEALTHBAR_MARGIN;

    private int bossHealthbarContentWidth;

    private String bossHealthPointsString;
    private int bossHealPointsXPos;
    private TrueTypeFont bossHealthPointsFont;

    private boolean isBarDisplayed;

    public BossHealthBar() {
        this.isBarDisplayed = false;

        FontRenderer.getPixelFontRenderer().setPxSize((int) (BOSS_HEALTHPOINTS_FONT_FACTOR*BOSS_HEALTHBAR_HEIGHT));
        this.bossHealthPointsFont = FontRenderer.getPixelFont();

        // MUST BE DONE AFTER THE FONT
        this.setBossHealthbarWidth(0, 1);
        this.setHealthPointsString(0, 0);
    }

    public void render(Graphics g) {
        if(this.isBarDisplayed) {
            super.render(
                    g,
                    BOSS_HEALTHBAR_XPOS,
                    BOSS_HEALTHBAR_YPOS,
                    BOSS_HEALTHBAR_WIDTH,
                    BOSS_HEALTHBAR_HEIGHT,
                    bossHealthbarContentWidth,
                    BOSS_HEALTHBAR_CONTENT_HEIGHT,
                    BOSS_HEALTHBAR_COLOR
            );

            g.setFont(this.bossHealthPointsFont);
            g.drawString(bossHealthPointsString, bossHealPointsXPos, BOSS_HEALTHPOINTS_YPOS);
        }
    }

    @Override
    public void onUpdate(LivingBeing being) {
        System.out.println("boss hurt : " + being.getClass().getName());
        if(being.getCurrentHealthPoints() > 0) {
            this.isBarDisplayed = true;

            this.setHealthPointsString(being.getCurrentHealthPoints(), being.getMaxHealthPoints());
            this.setBossHealthbarWidth(being.getCurrentHealthPoints(), being.getMaxHealthPoints());
        } else {
            this.isBarDisplayed = false;
        }
    }

    private void setBossHealthbarWidth(int currentHealthPoints, int maxHealthPoints) {
        this.bossHealthbarContentWidth = (int) ((float) currentHealthPoints / (float) maxHealthPoints * BOSS_HEALTHBAR_WIDTH);
    }

    private void setHealthPointsString(int currentHealthPoints, int maxHealthPoints) {
        this.bossHealthPointsString = "" + currentHealthPoints + "/" + maxHealthPoints;

        this.bossHealPointsXPos = (MainClass.WIDTH - bossHealthPointsFont.getWidth(this.bossHealthPointsString)) / 2;
    }
}
