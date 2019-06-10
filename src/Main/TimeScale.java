package Main;

public class TimeScale {
    private float timeScale;
    private float deltaTime;

    public static final TimeScale inGameTimeScale = new TimeScale(1f);
    public static final TimeScale guiTimeScale = new TimeScale(1f);

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

    public TimeScale(float timeScale) {
        this.timeScale = timeScale;
    }
}
