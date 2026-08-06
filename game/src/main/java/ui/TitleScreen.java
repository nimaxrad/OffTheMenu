package ui;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

import core.GamePanel;

/**
 * Used to draw the title screen.
 *
 * @author Ken Tran
 */
public class TitleScreen extends Interface {

    BufferedImage background;

    /**
     * Calls the UI constructor.
     * Sets the total number of options to three.
     * Loads the background image.
     *
     * @param gp GamePanel object that is used to run the game
     */
    protected TitleScreen(GamePanel gp) {
        super(gp);

        totalOptions = 1;

        try {
            background = ImageIO.read(getClass().getResourceAsStream("/background/mainmenubg.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {
        int screenW = g2.getClipBounds() != null ? g2.getClipBounds().width : GamePanel.SCREEN_WIDTH;
        int screenH = g2.getClipBounds() != null ? g2.getClipBounds().height : GamePanel.SCREEN_HEIGHT;

        // draw background scaled to fit without stretching
        if (background != null) {
            int bgWidth = background.getWidth();
            int bgHeight = background.getHeight();

            float scaleX = (float) screenW / bgWidth;
            float scaleY = (float) screenH / bgHeight;
            float finalScale = Math.min(scaleX, scaleY);

            int drawWidth = (int) (bgWidth * finalScale);
            int drawHeight = (int) (bgHeight * finalScale);

            int drawX = (screenW - drawWidth) / 2;
            int drawY = (screenH - drawHeight) / 2;

            g2.drawImage(background, drawX, drawY, drawWidth, drawHeight, null);
        }

        // title
        g2.setFont(pressStart.deriveFont(Font.PLAIN, 40f));
        g2.setColor(Color.WHITE);

        String title = "OFF THE MENU";
        g2.drawString(title, getHorizontalCenter(title, g2, screenW), GamePanel.TILE_SIZE * 3);

        // high score
        g2.setFont(pressStart.deriveFont(Font.PLAIN, 20f));
        String highScore = "BEST: " + gp.player.getHighScore();
        g2.drawString(highScore, getHorizontalCenter(highScore, g2, screenW), GamePanel.TILE_SIZE * 5);

        // menu font
        g2.setFont(pressStart.deriveFont(Font.PLAIN, 30f));

        String play = "PLAY";
        String quit = "QUIT";

        int y1 = GamePanel.TILE_SIZE * 10;
        int y2 = GamePanel.TILE_SIZE * 11;

        // PLAY
        g2.drawString(play, getHorizontalCenter(play, g2, screenW), y1);
        if (selectPosition == 0) {
            g2.drawString(">", getHorizontalCenter(play, g2, screenW) - (GamePanel.TILE_SIZE + 10), y1);
        }

        // QUIT
        g2.drawString(quit, getHorizontalCenter(quit, g2, screenW), y2);
        if (selectPosition == 1) {
            g2.drawString(">", getHorizontalCenter(quit, g2, screenW) - (GamePanel.TILE_SIZE + 10), y2);
        }
    }
}