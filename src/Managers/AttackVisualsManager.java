package Managers;

import HUD.AttackVisual;
import Main.MainClass;
import org.newdawn.slick.Graphics;

import java.util.ArrayList;

/**
 * Data manager for the atack visuals (cooldown)
 */
public class AttackVisualsManager {
    private static final int ATTACK_VISUALS_MARGIN = 10;
    private static final int ATTACK_VISUALS_OFFSET = AttackVisual.ICON_SIZE + ATTACK_VISUALS_MARGIN;
    private static final int ATTACK_VISUALS_YPOS = MainClass.HEIGHT - ATTACK_VISUALS_OFFSET;

    private static ArrayList<AttackVisual> visuals = null;

    private static int attackVisualsXPos;

    /**
     * Add a visual to the list
     * @param visual the visual to add
     */
    public static void addVisual(AttackVisual visual) {
        initializeList();

        visuals.add(visual);
        attackVisualsXPos -= ATTACK_VISUALS_OFFSET; // reduce the intial x position
    }

    /**
     * Initialize list and x position
     */
    private static void initializeList() {
        if (visuals == null) {
            visuals = new ArrayList<>();
            attackVisualsXPos = MainClass.WIDTH;
        }
    }

    /**
     * Default constructor : intialize list
     * @see AttackVisualsManager#initializeList()
     */
    public AttackVisualsManager() {
        initializeList();
    }

    /**
     * In game rendering
     * @param g the graphics to draw on
     */
    public void render(Graphics g) {
        int xPos = attackVisualsXPos;
        for (AttackVisual visual : visuals) { // render each visual
            visual.render(g, xPos, ATTACK_VISUALS_YPOS);
            xPos += ATTACK_VISUALS_OFFSET;
        }
    }
}
