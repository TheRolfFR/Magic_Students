package Entities.LivingBeings.Monsters;

import Listeners.SummonListener;

import java.util.ArrayList;

public interface IBoss {

    ArrayList<SummonListener> summonListeners = new ArrayList<>();

    int getCurrentHealthPoints();
    int getMaxHealthPoints();

    default void addSummonListener(SummonListener listener) {
        summonListeners.add(listener);
    }

    default void triggerListener(Monster monster) {
        for(SummonListener listener : summonListeners) {
            listener.onSummon(monster);
        }
    }
}
