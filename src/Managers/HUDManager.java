package Managers;

import Entities.LivingBeings.IHurtListener;
import Entities.LivingBeings.LivingBeing;
import Entities.LivingBeings.Player;
import HUD.HealthBars.BossHealthBar;
import HUD.HealthBars.PlayerHealthBar;
import Main.MainClass;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class HUDManager implements IHurtListener {

    private static final String HURT_EFFECT_IMG_PATH = "img/cameraHurtEffect.png";
    private static final int HURT_EFFECT_DURATION = 500;
    private static final float HURT_B = 4/ (float) HURT_EFFECT_DURATION;
    private static final float HURT_A = -HURT_B/ (float) HURT_EFFECT_DURATION;

    private boolean isHurtEffectActivated;
    private int hurtEffectTimer;

    private Image hurtEffect;

    private PlayerHealthBar playerHealthBar;
    private BossHealthBar bossHealthBar;

    public BossHealthBar getBossHealthBar() {
        return bossHealthBar;
    }

    public HUDManager(Player player, EnemiesManager enemiesManager) {
        this.playerHealthBar = new PlayerHealthBar(player);
        this.bossHealthBar = new BossHealthBar();

        try {
            this.hurtEffect = new Image(HURT_EFFECT_IMG_PATH).getScaledCopy(MainClass.WIDTH, MainClass.HEIGHT);
        } catch (SlickException e) {
            e.printStackTrace();
            System.exit(1);
        }

        this.isHurtEffectActivated = false;
        this.hurtEffectTimer = 0;
    }

    public void update(int deltaTime) {
        if(this.isHurtEffectActivated) {
            if(this.hurtEffectTimer < HURT_EFFECT_DURATION) {
                this.hurtEffectTimer = Math.min(this.hurtEffectTimer + deltaTime, HURT_EFFECT_DURATION);
            } else {
                this.isHurtEffectActivated = false;
            }
        }
    }

    public void render(Graphics g) {
        this.playerHealthBar.render(g);

        if(this.bossHealthBar != null) {
            this.bossHealthBar.render(g);
        }

        if(this.isHurtEffectActivated) {
            float opacity = HURT_A * this.hurtEffectTimer*this.hurtEffectTimer + HURT_B * this.hurtEffectTimer;

            g.drawImage(this.hurtEffect, 0, 0, new Color(255, 255, 255, opacity));
        }
    }

    @Override
    public void onHurt(LivingBeing being) {
        this.isHurtEffectActivated = true;
        this.hurtEffectTimer = 0;
    }
}
