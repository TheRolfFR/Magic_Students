package HUD.HealthBars;

import Listeners.LivingBeingHurtListener;
import Listeners.LivingBeingMoveListener;
import Entities.LivingBeings.LivingBeing;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

import static java.lang.Math.max;
import static java.lang.Math.round;

public class WorldHealthBar implements LivingBeingHurtListener, LivingBeingMoveListener {
    private static final int HEALTHBAR_WIDTH = 50;
    private static final int HEALTHBAR_HEIGHT = 10;

    private static final float HEALTHBAR_BACKGROUND_OPACITY = 0.4f;
    private static final Color HEALTHBAR_BACKGROUND_COLOR = new Color(0, 0, 0, HEALTHBAR_BACKGROUND_OPACITY);

    private static final Color HEALTHBAR_COLOR = new Color(0xd32f2f);

    private static final Vector2f HEALTHBAR_OFFSET = new Vector2f(HEALTHBAR_WIDTH, HEALTHBAR_HEIGHT).scale(0.5f);

    private static final int HEALTHBAR_MARGIN = 10;
    private static final Vector2f HEALTHBAR_MARGIN_OFFSET = new Vector2f(0, HEALTHBAR_MARGIN);

    private int healthBarWidth;
    private Vector2f tileSizeYOffset;
    private Vector2f location;

    public WorldHealthBar(LivingBeing being) {
        this.healthBarWidth = (int) ((float) being.getCurrentHealthPoints() / (float) being.getMaxHealthPoints() * HEALTHBAR_WIDTH);

        this.tileSizeYOffset = new Vector2f(0, being.getTileSize().getY()/2f);
        this.setLocation(being.getCenter());
        being.addHurtListener(this);
    }

    public void render(Graphics g){
        if(this.healthBarWidth != HEALTHBAR_WIDTH) {

            g.setColor(HEALTHBAR_BACKGROUND_COLOR);
            g.fillRect(location.getX(), location.getY(), HEALTHBAR_WIDTH, HEALTHBAR_HEIGHT);

            g.setColor(HEALTHBAR_COLOR);
            g.fillRect(location.getX(), location.getY(), round(this.healthBarWidth), HEALTHBAR_HEIGHT);
        }
    }

    @Override
    public void onHurt(LivingBeing being) {
        this.healthBarWidth = (int) ((float) being.getCurrentHealthPoints() / (float) being.getMaxHealthPoints() * HEALTHBAR_WIDTH);
    }

    @Override
    public void onMove(LivingBeing being) {
        setLocation(being.getCenter());
    }

    private void setLocation(Vector2f center) {
        this.location = center.sub(HEALTHBAR_OFFSET).add(HEALTHBAR_MARGIN_OFFSET).add(this.tileSizeYOffset);
    }
}
