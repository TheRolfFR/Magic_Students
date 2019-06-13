package Managers;

import Entities.LivingBeings.Player;
import HUD.HealthBars.BossHealthBar;
import HUD.HealthBars.PlayerHealthBar;
import org.newdawn.slick.Graphics;

public class HUDManager {

    protected PlayerHealthBar playerHealthBar;
    protected BossHealthBar bossHealthBar;

    public HUDManager(Player player, EnemiesManager enemiesManager) {
        this.playerHealthBar = new PlayerHealthBar(player);
        this.bossHealthBar = new BossHealthBar(enemiesManager);
    }

    public void render(Graphics g) {
        this.playerHealthBar.render(g);

        if(this.bossHealthBar != null) {
            this.bossHealthBar.render(g);
        }
    }
}
