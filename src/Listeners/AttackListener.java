package Listeners;

public interface AttackListener {

    default void onAttack(int amount) {

    }

    default void onCooldownStart() {

    }

    default void onCooldownUpdate(float currentValue, float maxValue) {

    }

    default void onCooldownEnd() {

    }
}
