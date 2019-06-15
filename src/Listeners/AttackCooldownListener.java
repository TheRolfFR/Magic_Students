package Listeners;

public interface AttackCooldownListener {
    void onCooldownStart();

    void onCooldownUpdate(float currentValue, float maxValue);

    void onCooldownEnd();
}
