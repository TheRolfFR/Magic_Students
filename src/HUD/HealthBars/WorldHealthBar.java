package HUD.HealthBars;

import Entities.LivingBeings.LivingBeing;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

import static java.lang.Math.max;
import static java.lang.Math.round;

public class WorldHealthBar {
    private static final int HEALTHBAR_WIDTH = 50;
    private static final int HEALTHBAR_HEIGHT = 10;

    private static final float HEALTHBAR_BACKGROUND_OPACITY = 0.4f;
    private static final Color HEALTHBAR_BACKGROUND_COLOR = new Color(0, 0, 0, HEALTHBAR_BACKGROUND_OPACITY);

    private static final Color HEALTHBAR_COLOR = new Color(0xd32f2f);

    private static final Vector2f HEALTHBAR_OFFSET = new Vector2f(HEALTHBAR_WIDTH, HEALTHBAR_HEIGHT).scale(0.5f);

    private static final int HEALTHBAR_MARGIN = 10;

    private LivingBeing being;

    public WorldHealthBar(LivingBeing being) {
        this.being = being;
    }

    public void render(Graphics g){
        Vector2f location = being.getCenter().sub(HEALTHBAR_OFFSET).add(new Vector2f(0, HEALTHBAR_MARGIN)).add(new Vector2f(0, being.getTileSize().getY()/2));

        g.setColor(HEALTHBAR_COLOR);
        g.fillRect(location.getX(), location.getY(), HEALTHBAR_WIDTH, HEALTHBAR_HEIGHT);
        g.fillRect(location.getX(), location.getY(), round(being.getCurrentHealthPoints()/(float) being.getMaxHealthPoints() * HEALTHBAR_WIDTH), HEALTHBAR_HEIGHT);
    }
}
