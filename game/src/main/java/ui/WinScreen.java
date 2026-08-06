package ui;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

import core.GamePanel;

/**
 * Used to draw the win screen.
 *
 * @author Ken Tran
 */
public class WinScreen extends Interface {

    BufferedImage background;

    /**
     * Calls the UI constructor.
     * Sets the total number of options to two.
     * Loads the background image.
     *
     * @param gpGamePanel object that is used to run the game
     */

    protected WinScreen(GamePanel gp) {
        super(gp);

        totalOptions = 2;

        try {
            background = ImageIO.read(getClass().getResourceAsStream("/background/winbg.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Draws the win game screen for the player with a menu.
     * Menu contains buttons for retrying, returning to main menu and closing the game.
     * A selector icon is used to show the user what they have currently selected.
     * Also displays the player's current score and high score.
     *
     * @param g2 the main graphics object that is used to draw the UI to the screen
     */

    public void draw(Graphics2D g2) {
        int screenW = g2.getClipBounds() != null ? g2.getClipBounds().width : GamePanel.SCREEN_WIDTH;
        int screenH = g2.getClipBounds() != null ? g2.getClipBounds().height : GamePanel.SCREEN_HEIGHT;

        // draw background
        g2.drawImage(background, 0, 0, screenW, screenH, null);

        // title text
        g2.setFont(pressStart.deriveFont(Font.PLAIN, 40f));
        g2.setColor(Color.WHITE);

        String winText = "YOU WIN!";
        g2.drawString(winText, getHorizontalCenter(winText, g2, screenW), GamePanel.TILE_SIZE * 3);

        // score and high score
        g2.setFont(pressStart.deriveFont(Font.PLAIN, 20f));

        String score = "SCORE: " + gp.player.getScore();
        g2.drawString(score, getHorizontalCenter(score, g2, screenW), GamePanel.TILE_SIZE * 5);

        String highScore = "BEST: " + gp.player.getHighScore();
        // highlight high score in yellow if player beat it
        if (gp.player.getScore() >= gp.player.getHighScore() && gp.player.getScore() > 0) {
            g2.setColor(Color.YELLOW);
            String newBest = "NEW BEST!";
            g2.drawString(newBest, getHorizontalCenter(newBest, g2, screenW), GamePanel.TILE_SIZE * 6);
            g2.setColor(Color.WHITE);
        }
        g2.drawString(highScore, getHorizontalCenter(highScore, g2, screenW), GamePanel.TILE_SIZE * 6 + 30);

        // menu
        g2.setFont(pressStart.deriveFont(Font.PLAIN, 30f));

        String retry = "REPLAY";
        String menu = "MAIN MENU";
        String quit = "QUIT";

        int y1 = GamePanel.TILE_SIZE * 8;
        int y2 = GamePanel.TILE_SIZE * 10;
        int y3 = GamePanel.TILE_SIZE * 12;

        g2.setColor(Color.WHITE);

        g2.drawString(retry, getHorizontalCenter(retry, g2, screenW), y1);
        if (selectPosition == 0) {
            g2.drawString(">", getHorizontalCenter(retry, g2, screenW) - GamePanel.TILE_SIZE, y1);
        }

        g2.drawString(menu, getHorizontalCenter(menu, g2, screenW), y2);
        if (selectPosition == 1) {
            g2.drawString(">", getHorizontalCenter(menu, g2, screenW) - GamePanel.TILE_SIZE, y2);
        }

        g2.drawString(quit, getHorizontalCenter(quit, g2, screenW), y3);
        if (selectPosition == 2) {
            g2.drawString(">", getHorizontalCenter(quit, g2, screenW) - GamePanel.TILE_SIZE, y3);
        }
    }

    public void showNewHighScore(boolean show) {
        // TODO: display high score indicator
    }
}