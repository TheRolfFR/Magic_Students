package Managers;

import Entities.LivingBeings.Player;
import HUD.HealthBars.BossHealthBar;
import HUD.HealthBars.PlayerHealthBar;
import Main.GameStats;
import Main.MainClass;
import Renderers.FontRenderer;
import Renderers.HurtEffectRenderer;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;

/**
 * Game HUD Manager : player health bar, boss shealth bar, hurt effect and difficulty
 */
public class HUDManager {

    private static final int HUD_MARGIN = 10;
    private static final int DIFFICULTY_FONT_SIZE_PX = 22;
    private static final Color DIFFCULTY_COLOR = new Color(0x7F000000);
    private static final String DIFFICULTY_PRESTRING = "Difficuty : ";
    private Font difficultyFont;

    private PlayerHealthBar playerHealthBar;
    private BossHealthBar bossHealthBar;
    private HurtEffectRenderer hurtEffectRenderer;

    /**
     * Default renderer
     * @param player player related
     * @param enemiesManager enemiesManager related
     */
    public HUDManager(Player player, EnemiesManager enemiesManager) {
        // initialization of the boss health bar
        this.bossHealthBar = new BossHealthBar();
        enemiesManager.setBossHealthBar(bossHealthBar);

        // initialization of the player health bar
        this.playerHealthBar = new PlayerHealthBar(player);
        player.addHealthListener(this.playerHealthBar);

        // initialization of the hurt effect
        this.hurtEffectRenderer = new HurtEffectRenderer();
        player.addHealthListener(this.hurtEffectRenderer);

        // initialization of the diffulty font
        FontRenderer.getPixelFontRenderer().setPxSize(DIFFICULTY_FONT_SIZE_PX);
        this.difficultyFont = FontRenderer.getPixelFont();
    }

    /**
     * In game upate : updates the hurt effect
     * @param deltaTime duration in ms of the last frame
     * @see HurtEffectRenderer#update(int)
     */
    public void update(int deltaTime) {
        this.hurtEffectRenderer.update(deltaTime);
    }

    /**
     * In game rendering
     * @param g the graphics to draw on
     */
    public void render(Graphics g) {
        // render the player health bar
        this.playerHealthBar.render(g);

        // render the boss health bar
        this.bossHealthBar.render(g);

        // render the difficulty
        g.setColor(DIFFCULTY_COLOR);
        g.setFont(difficultyFont);
        String s = DIFFICULTY_PRESTRING + GameStats.getInstance().getDifficulty();
        g.drawString(s, MainClass.instanceGameContainer.getWidth() - HUD_MARGIN - difficultyFont.getWidth(s), HUD_MARGIN);

        // render the hurt effect
        this.hurtEffectRenderer.render(g);
    }
}
