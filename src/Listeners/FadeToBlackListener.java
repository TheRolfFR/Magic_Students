package Listeners;

/**
 * listener triggered on {@link HUD.FadeToBlack#update(int) FadeToBlack update}
 */
public interface FadeToBlackListener {
    /**
     * triggered when the transition is total black
     */
    void atHalf();

    /**
     * triggered when the transition is totally transparent
     */
    void atEnd();
}
