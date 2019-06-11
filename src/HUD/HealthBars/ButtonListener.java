package HUD.HealthBars;

import Main.MainClass;
import org.newdawn.slick.Input;
import org.newdawn.slick.MouseListener;

public interface ButtonListener extends MouseListener {
    @Override
    public default void mouseWheelMoved(int i) {

    }

    @Override
    public default void mousePressed(int i, int i1, int i2) {

    }

    @Override
    public default void mouseReleased(int i, int i1, int i2) {

    }

    @Override
    public default void mouseMoved(int i, int i1, int i2, int i3) {

    }

    @Override
    public default void mouseDragged(int i, int i1, int i2, int i3) {

    }

    @Override
    public default void setInput(Input input) {

    }

    @Override
    public default boolean isAcceptingInput() {
        return false;
    }

    @Override
    public default void inputEnded() {

    }

    @Override
    public default void inputStarted() {

    }
}
