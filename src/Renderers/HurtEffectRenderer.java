package Renderers;

import Listeners.LivingBeingHealthListener;
import Entities.LivingBeings.LivingBeing;
import Main.MainClass;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class HurtEffectRenderer implements LivingBeingHealthListener {

    private static final String HURT_EFFECT_IMG_PATH = "img/cameraHurtEffect.png";
    private static final int HURT_EFFECT_DURATION = 500;
    private static final float HURT_B = 4/ (float) HURT_EFFECT_DURATION;
    private static final float HURT_A = -HURT_B/ (float) HURT_EFFECT_DURATION;

    private boolean isHurtEffectActivated;
    private int hurtEffectTimer;

    private Image hurtEffect;

    public HurtEffectRenderer() {
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
        if(this.isHurtEffectActivated) {
            float opacity = HURT_A * this.hurtEffectTimer*this.hurtEffectTimer + HURT_B * this.hurtEffectTimer; // a*x^2 + b*x

            g.drawImage(this.hurtEffect, 0, 0, new Color(255, 255, 255, opacity));
        }
    }

    @Override
    public void onHurt(LivingBeing being) {
        if(!isHurtEffectActivated) {
            this.isHurtEffectActivated = true;
            this.hurtEffectTimer = 0;
        }
    }
}
