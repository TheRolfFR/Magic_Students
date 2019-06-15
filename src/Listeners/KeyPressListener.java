package Listeners;

import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;

public interface KeyPressListener extends KeyListener {

    @Override
    default void setInput(Input input) {

    }

    @Override
    default boolean isAcceptingInput() {
        return true;
    }

    @Override
    default void inputEnded() {

    }

    @Override
    default void inputStarted() {

    }

    @Override
    default void keyPressed(int i, char c) {

    }

    @Override
    default void keyReleased(int i, char c) {

    }
}
