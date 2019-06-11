package Managers;

import HUD.HealthBar;
import HUD.PlayerHealthBar;
import org.newdawn.slick.Graphics;

import java.util.List;

public class HUDManager {

    protected PlayerHealthBar playerHealthBar;
    protected BossHealthBar bossHealthBar;

    public PlayerHealthBar getPlayerHealthBar() {
        return playerHealthBar;
    }

    public BossHealthBar getBossHealthBar() {
        return bossHealthBar;
    }

    public HUDManager() {
        this.playerHealthBar = new PlayerHealthBar(1);
    }

    public void render(Graphics g) {
        this.playerHealthBar.render(g);
    }
}
