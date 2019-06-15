package Managers;

import HUD.AttackVisual;
import Main.MainClass;
import org.newdawn.slick.Graphics;

import java.util.ArrayList;

public class AttackVisualsManager {
    private static final int ATTACK_VISUALS_MARGIN = 10;
    private static final int ATTACK_VISUALS_XOFFSET = AttackVisual.ICON_SIZE + ATTACK_VISUALS_MARGIN;
    private static final int ATTACK_VISUALS_YPOS = MainClass.HEIGHT - ATTACK_VISUALS_XOFFSET;

    private static ArrayList<AttackVisual> visuals = null;

    private static int attackVisualsXPos;

    public static void addVisual(AttackVisual visual) {
        initializeList();

        visuals.add(visual);
        attackVisualsXPos -= ATTACK_VISUALS_XOFFSET;
    }

    private static void initializeList() {
        if(visuals == null) {
            visuals = new ArrayList<>();
            attackVisualsXPos = MainClass.WIDTH;
        }
    }

    public AttackVisualsManager() {
        initializeList();
    }

    public void render(Graphics g) {
        int xPos = attackVisualsXPos;
        for(AttackVisual visual : visuals) {
            visual.render(g, xPos, ATTACK_VISUALS_YPOS);
            xPos += ATTACK_VISUALS_XOFFSET;
        }
    }
}
