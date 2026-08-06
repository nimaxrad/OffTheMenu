package ui;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import core.GamePanel;
import object.OBJ_Heart;
import object.OBJ_PigIcon;

/**
 * Used to draw the user interface during gameplay onto the screen.
 * Contains methods to update different elements of the UI.
 *
 * @author Ken Tran
 */
public class PlayScreen extends Interface {
    OBJ_Heart playerHP[] = new OBJ_Heart[3];
    OBJ_PigIcon remainingPigs[] = new OBJ_PigIcon[3];

    double messageCounter = 0;
    boolean messageOn = false;
    String message = "";

    /**
     * Calls the Interface constructor.
     * Loads heart and pig sprites
     *
     * @param gp GamePanel object that is used to run the game
     */
    protected PlayScreen(GamePanel gp) {
        super(gp);

        playerHP[0] = new OBJ_Heart();
        playerHP[1] = new OBJ_Heart();
        playerHP[2] = new OBJ_Heart();
        remainingPigs[0] = new OBJ_PigIcon();
        remainingPigs[1] = new OBJ_PigIcon();
        remainingPigs[2] = new OBJ_PigIcon();
    }

    /**
     * Displays the player's health points on the screen.
     * Hearts are either empty or full depending on how many health points the
     * player has.
     *
     * @param g2 the main graphics object that is used to draw the hearts onto the
     *           screen
     */
    private void drawHeartAndPig(Graphics2D g2) {
        int startX = GamePanel.TILE_SIZE / 2;
        int y = GamePanel.TILE_SIZE / 2;

        // Hearts
        for (int i = 0; i < 3; i++) {
            if (gp.player.getHearts() > i) {
                playerHP[i].fullHeart();
            } else {
                playerHP[i].emptyHeart();
            }
            int heartWidth = GamePanel.TILE_SIZE;
            int heartHeight = heartWidth * 285 / 310;

            g2.drawImage(playerHP[i].image, startX + i * GamePanel.TILE_SIZE, y, heartWidth, heartHeight, null);
        }

        // Pigs
        int rescuedPigs = gp.player.getPigsRescued();
        int pigStartX = startX + 3 * GamePanel.TILE_SIZE + GamePanel.TILE_SIZE / 2;
        for (int i = 0; i < 3; i++) {
            if (rescuedPigs > i) {
                remainingPigs[i].pig();
            } else {
                remainingPigs[i].emptyPig();
            }
            g2.drawImage(remainingPigs[i].image, pigStartX + i * GamePanel.TILE_SIZE, y, GamePanel.TILE_SIZE, GamePanel.TILE_SIZE, null);
        }
    }

    /**
     * Displays a message on the screen.
     *
     * @param text the message that is to be displayed
     */
    public void showMessage(String text) {
        if (messageOn) {
            messageCounter = 0;
        }
        message = text;
        messageOn = true;
    }

    /**
     * Reset the timer to zero.
     */
    public void resetTimer() {
        gp.getEngine().setElapsedTicks(0);
    }

    /**
     * Turns off messages and deletes the saved messsage.
     */
    public void resetMessage() {
        message = "";
        messageOn = false;
    }

    /**
     * Creates a timer that displays minutes and seconds.
     *
     * @return a string with the players current time
     */
    private String drawTimer() {
        String timer = "";
        int playTime = getPlayTime();

        int minutes = playTime / 60;
        int seconds = playTime % 60;

        if (minutes < 10) {
            timer += "0" + minutes;
        } else {
            timer += minutes;
        }

        timer += ":";

        if (seconds < 10) {
            timer += "0" + seconds;
        } else {
            timer += seconds;
        }

        return timer;
    }

    /**
     * Draws the game UI while the user is playing the game.
     * Shows current health, level name, score and time.
     * Messages may appear on the screen to indicate to the player what is happening
     * such as when a player interacts with an object or entity.
     *
     * @param g2 the main graphics object that is used to draw the UI onto the
     *           screen
     */
    public void draw(Graphics2D g2) {
        int screenW = g2.getClipBounds() != null ? g2.getClipBounds().width : GamePanel.SCREEN_WIDTH;
        int screenH = g2.getClipBounds() != null ? g2.getClipBounds().height : GamePanel.SCREEN_HEIGHT;

        g2.setColor(Color.WHITE);
        g2.setFont(pressStart.deriveFont(Font.PLAIN, 16f));

        // Health and pig icons (top left)
        drawHeartAndPig(g2);

        // SCORE (top middle-left)
        String score = "SCORE: " + gp.player.getScore();
        g2.drawString(score, screenW / 2 - GamePanel.TILE_SIZE * 4, 32);

        // HIGH SCORE (top middle-right)
        String highScore = "BEST: " + gp.player.getHighScore();
        g2.drawString(highScore, screenW / 2 + GamePanel.TILE_SIZE, 32);

        // TIME (top right)
        String time = "TIME: " + drawTimer();
        g2.drawString(time, screenW - GamePanel.TILE_SIZE * 6, 32);

        // Message
        if (messageOn) {
            g2.setFont(g2.getFont().deriveFont(21f));
            g2.drawString(message,
                    screenW / 2 - 50,
                    screenH / 2 - GamePanel.TILE_SIZE);

            messageCounter += (double) 1 / gp.getEngine().getTicksPerSecond();
            if (messageCounter > 2) {
                messageCounter = 0;
                messageOn = false;
            }
        }
    }

    /**
     * @return the player's play time in seconds
     */
    public int getPlayTime() {
        return gp.getEngine().getElapsedTicks() / gp.getEngine().getTicksPerSecond();
    }
}
