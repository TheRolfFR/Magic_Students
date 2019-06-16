package HUD;

import Entities.LivingBeings.LivingBeing;
import Listeners.LivingBeingHealthListener;
import Main.GameStats;
import Main.MainClass;
import Main.TimeScale;
import Renderers.FontRenderer;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;

public class EndScreen implements LivingBeingHealthListener {

    private static final Color BACKGROUND_COLOR = new Color(0x222222);

    private static final String GAME_OVER_TEXT = "Game Over".toUpperCase();

    private static final Color GAME_OVER_TEXT_COLOR = Color.white;
    private static final Color STATS_TEXT_COLOR = GAME_OVER_TEXT_COLOR.darker(.25f);

    private static final int STATS_FONT_SIZE_PX = 30;
    private static final int GAME_OVER_FONT_SIZE_PX = (int) (1.5f * STATS_FONT_SIZE_PX);
    private static final int TEXTS_MARGIN = 10;

    private static EndScreen ourInstance = new EndScreen();

    private Font gameOverFont;
    private int gameOverXOffset;
    private int gameOverYOffset;

    private Font statsFont;
    private int statsYOffset;

    private boolean isActive;
    private int opacityCounter;

    private String[] stats;

    public static EndScreen getInstance() {
        return ourInstance;
    }

    private String getStats() {
        return GameStats.getInstance().toString();
    }

    private int getStatsHeight() {
        int total = 0;
        for(String stat : stats) {
            total += statsFont.getHeight(stat);
        }

        return total;
    }

    private void renderStats(Graphics g) {
        int yOffset = statsYOffset;
        int xOffset;
        for(String stat : stats) {
            xOffset = (MainClass.instanceGameContainer.getWidth() - statsFont.getWidth(stat)) / 2;
            g.drawString(stat, xOffset, yOffset);
            yOffset += statsFont.getHeight(stat);
        }
    }

    private EndScreen() {
        FontRenderer.getPixelFontRenderer().setPxSize(STATS_FONT_SIZE_PX);
        statsFont = FontRenderer.getPixelFont();

        FontRenderer.getPixelFontRenderer().setPxSize(GAME_OVER_FONT_SIZE_PX);
        gameOverFont = FontRenderer.getPixelFont();
    }

    public void Render(Graphics g) {
        if(isActive) {
            g.setColor(BACKGROUND_COLOR);
            g.fillRect(0, 0, MainClass.instanceGameContainer.getWidth(), MainClass.instanceGameContainer.getHeight());

            g.setColor(GAME_OVER_TEXT_COLOR);
            g.setFont(gameOverFont);
            g.drawString(GAME_OVER_TEXT, gameOverXOffset, gameOverYOffset);

            g.setColor(STATS_TEXT_COLOR);
            g.setFont(statsFont);
            this.renderStats(g);
        }
    }

    private void resetEndScreen() {
        if(!isActive) {
            stats = GameStats.getInstance().getStrings();

            gameOverYOffset = (MainClass.instanceGameContainer.getHeight() - (gameOverFont.getHeight(GAME_OVER_TEXT) + TEXTS_MARGIN + this.getStatsHeight()))/2;
            gameOverXOffset = (MainClass.instanceGameContainer.getWidth() - gameOverFont.getWidth(GAME_OVER_TEXT))/2;

            statsYOffset = gameOverYOffset + gameOverFont.getHeight(GAME_OVER_TEXT) + TEXTS_MARGIN;

            isActive = true;
            opacityCounter = 0;
        }
    }

    @Override
    public void onDeath(LivingBeing being) {
        this.resetEndScreenAndPause();
    }

    private void resetEndScreenAndPause() {
        MainClass.setGamePaused();
        this.resetEndScreen();
    }
}
