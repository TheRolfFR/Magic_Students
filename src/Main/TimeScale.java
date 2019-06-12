package Main;

public class TimeScale {
    private float timeScale;
    private float deltaTime;

    private static final TimeScale inGameTimeScale = new TimeScale(1f);
    private static final TimeScale guiTimeScale = new TimeScale(1f);

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

    public void setTimeScale(float timeScale) {
        this.timeScale = Math.max(0f, Math.min(1f, timeScale));
    }

    public static void resume() { inGameTimeScale.timeScale = 1f; }

    public TimeScale(float timeScale) {
        this.timeScale = timeScale;
    }
}
