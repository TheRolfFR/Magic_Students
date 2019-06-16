package Managers;

import Entities.LivingBeings.Player;
import HUD.HealthBars.BossHealthBar;
import HUD.HealthBars.PlayerHealthBar;
import Renderers.HurtEffectRenderer;
import org.newdawn.slick.Graphics;

public class HUDManager {

    private PlayerHealthBar playerHealthBar;
    private BossHealthBar bossHealthBar;
    private HurtEffectRenderer hurtEffectRenderer;

    public HUDManager(Player player, EnemiesManager enemiesManager) {
        this.bossHealthBar = new BossHealthBar();
        enemiesManager.setBossHealthBar(bossHealthBar);

        this.playerHealthBar = new PlayerHealthBar(player);
        player.addHurtListener(this.playerHealthBar);
        this.hurtEffectRenderer = new HurtEffectRenderer();
        player.addHurtListener(this.hurtEffectRenderer);
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
    }
}
