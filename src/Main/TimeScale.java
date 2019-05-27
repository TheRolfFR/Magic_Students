package Main;

public class TimeScale {
    private float timeScale;

    public float getTimeScale() {
        return timeScale;
    }

    public void setTimeScale(float timeScale) {
        this.timeScale = Math.max(0f, Math.min(1f, timeScale));
    }

    public TimeScale(float timeScale) {
        this.timeScale = timeScale;
    }
}
