package HUD;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import static java.lang.Math.round;

public class HealthBar {

    private static final int x = 10;
    private static final int y = 10;
    private static final int width=50;
    private static final int height = 10;

    public void render(Graphics g, int health){
        g.setColor(new Color(255,0,0));
        g.fillRect(this.x, this.y, round(health/100.0 * this.width), this.height);
    }
}
