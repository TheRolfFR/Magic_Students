package Listeners;

public interface AttackCooldownListener {
    void onCooldownStart();

    void onCooldownUpdate(int currentValue, int maxValue);

    void onCooldownEnd();
}
