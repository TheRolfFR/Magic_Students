package Main;

import Listeners.PortalsManagerListener;
import Managers.PortalsManager;

public class TimeScale implements PortalsManagerListener {
    private float timeScale;
    private float deltaTime;

    private static final TimeScale inGameTimeScale = new TimeScale(1f, new PortalsManagerListener() {
        @Override
        public void onEngage(PortalsManager portalsManager) {
            inGameTimeScale.setTimeScale(0f);
        }
    });
    private static final TimeScale guiTimeScale = new TimeScale(1f);

    private PortalsManagerListener listener;

    public void setListener(PortalsManagerListener listener) {
        this.listener = listener;
    }

    public static TimeScale getInGameTimeScale() {
        return inGameTimeScale;
    }

    public static TimeScale getGuiTimeScale() {
        return guiTimeScale;
    }

    public float getTimeScale() {
        return timeScale;
    }

    public float getDeltaTime() {
        return deltaTime;
    }

    public void setDeltaTime(int delta) {
        this.deltaTime = (float)delta / 1000f;
    }

    void setTimeScale(float timeScale) {
        this.timeScale = Math.max(0f, Math.min(1f, timeScale));
    }

    public static void resume() { inGameTimeScale.timeScale = 1f; }

    public TimeScale(float timeScale) {
        this.timeScale = timeScale;
        this.listener = null;
    }

    private TimeScale(float timeScale, PortalsManagerListener listener) {
        this.timeScale = timeScale;
        this.listener = listener;
    }

    @Override
    public void onEngage(PortalsManager portalsManager) {
        if (this.listener != null) {
            this.listener.onEngage(portalsManager);
        }
    }
}
