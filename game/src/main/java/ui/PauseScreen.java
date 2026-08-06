package ui;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import core.GamePanel;


/**
 * Used to draw the pause screen.
 *
 * @author Ken Tran
 */
public class PauseScreen extends Interface{

    /**
     * Sets the total number of options to 2
     *
     * @param gp GamePanel object that is used to run the game
     */
    protected PauseScreen(GamePanel gp) {
        super(gp);

        totalOptions = 2;
    }

    /**
     * Display pause menu onto the player's screen.
     * Menu contains buttons for resuming the game, returning to main menu, going to settings screen and closing the game.
     * A selector icon is used to show the user what option they currently have selected.
     *
     * @param g2 the main graphics object that is used to draw the UI onto the screen
     */
    public void draw(Graphics2D g2) {
        int screenW = g2.getClipBounds() != null ? g2.getClipBounds().width : GamePanel.SCREEN_WIDTH;
        int screenH = g2.getClipBounds() != null ? g2.getClipBounds().height : GamePanel.SCREEN_HEIGHT;

        // Making the screen darker
        g2.setColor(new Color(0, 0, 0, 100));
        g2.fillRect(0, 0, screenW, screenH);

        g2.setFont(pressStart.deriveFont(Font.PLAIN, 20f));
        g2.setColor(Color.WHITE);

        String text = "PAUSED";
        g2.drawString(text, getHorizontalCenter(text, g2, screenW), GamePanel.TILE_SIZE * 3);

        g2.setFont(pressStart.deriveFont(Font.PLAIN, 40f));

        String resume = "RESUME";
        String menu = "MAIN MENU";
        String quit = "QUIT";

        // RESUME
        g2.drawString(resume, getHorizontalCenter(resume, g2, screenW), GamePanel.TILE_SIZE * 6);
        if (selectPosition == 0) {
            g2.drawString(">", getHorizontalCenter(resume, g2, screenW) - GamePanel.TILE_SIZE, GamePanel.TILE_SIZE * 6);
        }

        // MAIN MENU
        g2.drawString(menu, getHorizontalCenter(menu, g2, screenW), GamePanel.TILE_SIZE * 8);
        if (selectPosition == 1) {
            g2.drawString(">", getHorizontalCenter(menu, g2, screenW) - GamePanel.TILE_SIZE, GamePanel.TILE_SIZE * 8);
        }

        // QUIT
        g2.drawString(quit, getHorizontalCenter(quit, g2, screenW), GamePanel.TILE_SIZE * 10);
        if (selectPosition == 2) {
            g2.drawString(">", getHorizontalCenter(quit, g2, screenW) - GamePanel.TILE_SIZE, GamePanel.TILE_SIZE * 10);
        }
    }
}