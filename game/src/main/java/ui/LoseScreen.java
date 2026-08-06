package ui;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

import core.GamePanel;

/**
 * Used to draw the game over screen.
 *
 * @author Ken Tran
 */
public class LoseScreen extends Interface {

    BufferedImage background;

    /**
     * Calls the UI constructor.
     * Loads the background image.
     *
     * @param gp GamePanel object that is used to run the game
     */

    protected LoseScreen(GamePanel gp) {
        super(gp);

        totalOptions = 2;

        try {
            background = ImageIO.read(getClass().getResourceAsStream("/background/losebg.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Draws the game over screen for the player with a menu.
     * Contains buttons for retrying, returning to main menu and closing the game.

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

        String loseText = "YOU LOSE!";
        g2.drawString(loseText, getHorizontalCenter(loseText, g2, screenW), GamePanel.TILE_SIZE * 3);

        // score and high score
        g2.setFont(pressStart.deriveFont(Font.PLAIN, 20f));

        String score = "SCORE: " + gp.player.getScore();
        g2.drawString(score, getHorizontalCenter(score, g2, screenW), GamePanel.TILE_SIZE * 5);

        String highScore = "BEST: " + gp.player.getHighScore();
        g2.drawString(highScore, getHorizontalCenter(highScore, g2, screenW), GamePanel.TILE_SIZE * 6);

        // menu
        g2.setFont(pressStart.deriveFont(Font.PLAIN, 30f));
        g2.setColor(Color.WHITE);

        String retry = "RETRY";
        String menu = "MAIN MENU";
        String quit = "QUIT";

        int y1 = GamePanel.TILE_SIZE * 8;
        int y2 = GamePanel.TILE_SIZE * 10;
        int y3 = GamePanel.TILE_SIZE * 12;

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