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

public class HUDManager {

    private static final int HUD_MARGIN = 10;
    private static final int DIFFICULTY_FONT_SIZE_PX = 22;
    private static final Color DIFFCULTY_COLOR = new Color(0x7F000000);
    private static final String DIFFICULTY_PRESTRING = "Difficuty : ";
    private Font difficultyFont;

    private PlayerHealthBar playerHealthBar;
    private BossHealthBar bossHealthBar;
    private HurtEffectRenderer hurtEffectRenderer;

    public HUDManager(Player player, EnemiesManager enemiesManager) {
        this.bossHealthBar = new BossHealthBar();
        enemiesManager.setBossHealthBar(bossHealthBar);

        this.playerHealthBar = new PlayerHealthBar(player);
        player.addHealthListener(this.playerHealthBar);
        this.hurtEffectRenderer = new HurtEffectRenderer();
        player.addHealthListener(this.hurtEffectRenderer);

        FontRenderer.getPixelFontRenderer().setPxSize(DIFFICULTY_FONT_SIZE_PX);
        this.difficultyFont = FontRenderer.getPixelFont();
    }

    public void update(int deltaTime) {
        this.hurtEffectRenderer.update(deltaTime);
    }

    public void render(Graphics g) {
        this.playerHealthBar.render(g);

        if (this.bossHealthBar != null) {
            this.bossHealthBar.render(g);
        }

        this.hurtEffectRenderer.render(g);

        g.setColor(DIFFCULTY_COLOR);
        g.setFont(difficultyFont);

        String s = DIFFICULTY_PRESTRING + GameStats.getInstance().getDifficulty();
        g.drawString(s, MainClass.instanceGameContainer.getWidth() - HUD_MARGIN - difficultyFont.getWidth(s), HUD_MARGIN);
    }
}
