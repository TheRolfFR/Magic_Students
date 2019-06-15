package HUD;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class AttackVisual {
    public static final int ICON_SIZE = 60;
    private static final float ICON_BACKGROUND_OPACITY = 0.4f;
    private static final Color ICON_BACKGROUND_COLOR = new Color(0, 0, 0, ICON_BACKGROUND_OPACITY);
    private Image icon;

    public AttackVisual(String imgPath) {
        try {
            this.icon = new Image(imgPath, false, Image.FILTER_NEAREST).getScaledCopy(ICON_SIZE, ICON_SIZE);
        } catch (SlickException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void render(Graphics g, int x, int y) {
        g.setColor(ICON_BACKGROUND_COLOR);
        g.fillRect(x, y, ICON_SIZE, ICON_SIZE);

        g.drawImage(icon, x, y);
    }
}
