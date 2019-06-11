package Managers;

import Entities.LivingBeings.Player;
import HUD.HealthBars.BossHealthBar;
import HUD.HealthBars.PlayerHealthBar;
import org.newdawn.slick.Graphics;

public class HUDManager {

    protected PlayerHealthBar playerHealthBar;
    protected BossHealthBar bossHealthBar;

    public PlayerHealthBar getPlayerHealthBar() {
        return playerHealthBar;
    }

    public BossHealthBar getBossHealthBar() {
        return bossHealthBar;
    }

    public void setBossHealthBar(BossHealthBar bossHealthBar) {
        this.bossHealthBar = bossHealthBar;
    }

    public HUDManager(Player player) {
        this.playerHealthBar = new PlayerHealthBar(player);
        this.bossHealthBar = null;
    }

    public void render(Graphics g) {
        this.playerHealthBar.render(g);

        if(this.bossHealthBar != null) {
            this.bossHealthBar.render(g);
        }
    }
}
