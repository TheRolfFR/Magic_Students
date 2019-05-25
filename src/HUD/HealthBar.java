package HUD;

import Entities.LivingBeing;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import static java.lang.Math.round;

public class HealthBar {

    private static final int x = 10;
    private static final int y = 10;
    private static final int width=50;
    private static final int height = 10;

    private LivingBeing being;

    public HealthBar(LivingBeing being) {
        this.being = being;
    }

    public void render(Graphics g){
        g.setColor(new Color(255,0,0));
        g.fillRect(this.x, this.y, round(being.getCurrentHealthPoints()/(float) being.getMaxHealthPoints() * this.width), this.height);
    }
}
