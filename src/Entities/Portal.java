package Entities;

import java.util.ArrayList;

public class Portal extends Entity {
    private int width;
    private int height;
    private boolean visible;

    public static ArrayList<Portal> portals = new ArrayList<>();

    public Portal(float x, float y, int width, int height, int radius) {
        super(x, y, radius);
        this.width = width;
        this.height = height;
        this.visible = false;
    }

    public boolean isVisible() { return this.visible; }
    public void setVisible(boolean visible) { this.visible = visible; }

    @Override
    public void move() {}
}
