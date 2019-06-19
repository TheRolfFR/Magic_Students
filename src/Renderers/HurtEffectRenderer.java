package Renderers;

import Entities.LivingBeings.LivingBeing;
import Listeners.LivingBeingHealthListener;
import Main.MainClass;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * Visual effect to indicate to the user that the player was hurt
 */
public class HurtEffectRenderer implements LivingBeingHealthListener {

    private static final String HURT_EFFECT_IMG_PATH = "img/cameraHurtEffect.png";
    private static final int HURT_EFFECT_DURATION = 500;
    private static final float HURT_B = 4/ (float) HURT_EFFECT_DURATION;
    private static final float HURT_A = -HURT_B/ (float) HURT_EFFECT_DURATION;

    private boolean isHurtEffectActivated;
    private int hurtEffectTimer; // update timer for opacity

    private Image hurtEffect; // image of the hurt effect

    /**
     * Default constructor : loads the hurt effect image scaled to the size of the windows
     */
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

    /**
     * Updates this.hurtEffectTimer between 0 and HURT_EFFECT_DURATION
     * @param deltaTime duration of the last frame
     */
    public void update(int deltaTime) {
        if (this.isHurtEffectActivated) {
            if (this.hurtEffectTimer < HURT_EFFECT_DURATION) {
                this.hurtEffectTimer = Math.min(this.hurtEffectTimer + deltaTime, HURT_EFFECT_DURATION);
            } else {
                this.isHurtEffectActivated = false;
            }
        }
    }

    /**
     * In game redering
     * @param g the graphics to draw on
     */
    public void render(Graphics g) {
        if (this.isHurtEffectActivated) {
            float opacity = HURT_A * this.hurtEffectTimer*this.hurtEffectTimer + HURT_B * this.hurtEffectTimer; // a*x^2 + b*x

            g.drawImage(this.hurtEffect, 0, 0, new Color(255, 255, 255, opacity));
        }
    }

    /**
     * Activate the hurt effect and reset the timer if not already visible
     * @param being the living being hurt
     * @param amount the amount of damage received
     */
    @Override
    public void onHurt(LivingBeing being, int amount) {
        if (!isHurtEffectActivated) {
            this.isHurtEffectActivated = true;
            this.hurtEffectTimer = 0;
        }
    }
}
