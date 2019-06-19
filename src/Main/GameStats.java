package Main;

import Entities.LivingBeings.LivingBeing;
import Listeners.AttackListener;
import Listeners.LivingBeingHealthListener;

public class GameStats implements LivingBeingHealthListener, AttackListener {
    private static GameStats ourInstance = new GameStats();

    public static void setShowDebugRect(boolean state) {
        getInstance().showDebugRect = state;
    }

    public static boolean isShowDebugRect() {
        return getInstance().showDebugRect;
    }

    public static GameStats getInstance() {
        return ourInstance;
    }

    private boolean showDebugRect;
    private int difficulty;
    private long totalDamagesDone;
    private long totalDamagesReceived;
    private long totalHealReceived;

    private GameStats() {
        this.showDebugRect = false;
        this.difficulty = 1;
        this.totalDamagesDone = 0;
        this.totalDamagesReceived = 0;
        this.totalDamagesDone = 0;
    }

    @Override
    public String toString() {
        String toReturn = "--- Game Stats ---\n";
        toReturn += "Reached difficulty : " + difficulty + "\n";
        toReturn += "Total Damages Received : " + totalDamagesReceived + "\n";
        toReturn += "Total Damages Done : " + totalDamagesDone + "\n";
        toReturn += "Total Heal Received : " + totalHealReceived + "\n";
        return toReturn;
    }

    public String[] getStrings() {
        return this.toString().split("\n");
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void nextDifficulty() {
        difficulty++;
    }

    @Override
    public void onHeal(LivingBeing being, int amount) {
        this.totalHealReceived += amount;
    }

    @Override
    public void onHurt(LivingBeing being, int amount) {
        this.totalDamagesReceived += amount;
    }

    @Override
    public void onAttack(int amount) {
        this.totalDamagesDone += amount;
    }
}
