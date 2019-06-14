package HUD.HealthBars;

import Entities.LivingBeings.IHurtListener;
import Entities.LivingBeings.LivingBeing;
import Entities.LivingBeings.Player;
import Main.MainClass;
import Renderers.FontRenderer;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;

public class PlayerHealthBar extends UIHealthBar implements IHurtListener {

    private static final int PLAYER_HEALTHBAR_WIDTH = 100;
    private static final int PLAYER_HEALTHBAR_CONTENT_HEIGHT = PLAYER_HEALTHBAR_WIDTH / 10;
    private static final int PLAYER_HEALTHBAR_BOTTOM_SPACE = PLAYER_HEALTHBAR_CONTENT_HEIGHT / 3;
    private static final int PLAYER_HEALTHBAR_HEIGHT = PLAYER_HEALTHBAR_CONTENT_HEIGHT + PLAYER_HEALTHBAR_BOTTOM_SPACE;
    private static final int PLAYER_HEALTHBAR_MARGIN = 10;
    private static final Color PLAYER_HEALTHBAR_COLOR = new Color(0xd32f2f);

    private static final int PLAYER_HEALTHBAR_XPOS = PLAYER_HEALTHBAR_MARGIN;
    private static final int PLAYER_HEALTHBAR_YPOS = MainClass.HEIGHT - PLAYER_HEALTHBAR_MARGIN - PLAYER_HEALTHBAR_BOTTOM_SPACE - PLAYER_HEALTHBAR_CONTENT_HEIGHT;

    private static final float PLAYER_HEALTHPOINTS_FONT_SCALE = 2f;

    private int playerHealthbarContentWidth;

    private String playerHealthpointsString;
    private TrueTypeFont playerHealthpointsFont;
    private int playerHealthpointsYPos;

    public PlayerHealthBar(Player player) {
        player.addHurtListener(this);
        this.onHurt(player);

        FontRenderer.getPixelFontRenderer().setPxSize((int) (PLAYER_HEALTHBAR_HEIGHT * PLAYER_HEALTHPOINTS_FONT_SCALE));
        this.playerHealthpointsFont = FontRenderer.getPixelFont();
        this.playerHealthpointsYPos = PLAYER_HEALTHBAR_YPOS - PLAYER_HEALTHBAR_MARGIN - FontRenderer.getPixelFont().getHeight(this.playerHealthpointsString);
    }

    public void render(Graphics g) {
        super.render(
                g,
                PLAYER_HEALTHBAR_XPOS,
                PLAYER_HEALTHBAR_YPOS,
                PLAYER_HEALTHBAR_WIDTH,
                PLAYER_HEALTHBAR_HEIGHT,
                playerHealthbarContentWidth,
                PLAYER_HEALTHBAR_CONTENT_HEIGHT,
                PLAYER_HEALTHBAR_COLOR
        );

        g.setColor(Color.black);
        g.setFont(this.playerHealthpointsFont);
        g.drawString(this.playerHealthpointsString, PLAYER_HEALTHBAR_XPOS, playerHealthpointsYPos);
    }

    @Override
    public void onHurt(LivingBeing being) {
        this.playerHealthbarContentWidth = (int) ((float) being.getCurrentHealthPoints() / (float) being.getMaxHealthPoints() * PLAYER_HEALTHBAR_WIDTH);
        this.playerHealthpointsString = "" + being.getCurrentHealthPoints() + "/" + being.getMaxHealthPoints();
    }
}
