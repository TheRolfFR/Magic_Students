package Main;

public class TimeScale {
    private float timeScale;
    private float deltaTime;

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
