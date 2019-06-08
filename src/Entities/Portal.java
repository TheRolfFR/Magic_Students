package Entities;

import Renderer.ItemRenderer;
import org.newdawn.slick.Graphics;

import java.util.ArrayList;

public class Portal extends Entity {
    private int width;
    private int height;
    private boolean visible;

    private ItemRenderer renderer;

    public static ArrayList<Portal> portals = new ArrayList<>();

    public Portal(float x, float y, int width, int height, int radius) {
        super(x, y, width, height, radius);
        this.width = width;
        this.height = height;
        this.visible = false;

        this.renderer = null;
    }

    public boolean isVisible() { return this.visible; }
    public void setVisible(boolean visible) { this.visible = visible; }

    @Override
    public void move() {}

    @Override
    public void render(Graphics g) {
        if (this.renderer != null) {
            this.renderer.render(g, (int) this.getPosition().getX(), (int) this.getPosition().getY());
        }
        super.render(g);
    }
}
