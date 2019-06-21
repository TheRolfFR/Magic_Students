package Listeners;

import Entities.LivingBeings.Monsters.Monster;

/**
 * Listener triggered when a boss is summoning a minion
 */
public interface SummonListener {
    /**
     * a minion is summoned
     * @param monster the summoned monster
     */
    void onSummon(Monster monster);
}
