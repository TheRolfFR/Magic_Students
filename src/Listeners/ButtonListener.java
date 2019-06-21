package Listeners;

import org.newdawn.slick.Input;
import org.newdawn.slick.MouseListener;

/**
 * Extension of the Slick MouseListener interface with all empty defaults defined
 * @see MouseListener
 */
public interface ButtonListener extends MouseListener {
    @Override
    default void mouseWheelMoved(int i) {

    }

    @Override
    default void mousePressed(int i, int i1, int i2) {

    }

    @Override
    default void mouseReleased(int i, int i1, int i2) {

    }

    @Override
    default void mouseMoved(int i, int i1, int i2, int i3) {

    }

    @Override
    default void mouseDragged(int i, int i1, int i2, int i3) {

    }

    @Override
    default void setInput(Input input) {

    }

    @Override
    default boolean isAcceptingInput() {
        return false;
    }

    @Override
    default void inputEnded() {

    }

    @Override
    default void inputStarted() {

    }
}
