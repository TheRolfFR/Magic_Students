package HUD;

import Entities.LivingBeing;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import static java.lang.Math.round;

public class HealthBar {
    private int x;
    private int y;
    private static final int width = 50;
    private static final int height = 10;

    private LivingBeing being;

    public HealthBar(LivingBeing being) {
        this.being = being;
        this.x = 80;
        this.y = 10;
    }

    public HealthBar(LivingBeing being, int x, int y){
        this.being = being;
        this.x = x;
        this.y = y;
    }

    public void render(Graphics g){
        g.setColor(new Color(255,0,0));
        g.fillRect(x, y, round(being.getCurrentHealthPoints()/(float) being.getMaxHealthPoints() * width), height);
    }
}
