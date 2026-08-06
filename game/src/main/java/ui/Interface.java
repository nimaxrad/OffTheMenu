package ui;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.InputStream;

import core.GamePanel;

import java.io.IOException;



/**
 * Contains the base attributes and methods for each game state
 *
 * @author Ken Tran
 */
public abstract class Interface {
    GamePanel gp;
    Font pressStart;

    int selectPosition = 0;
    int totalOptions;

    /**
     * Creates a UI object and links the Settings singleton to this class.
     * Loads the font for the game.
     *
     * @param gp GamePanel object that is used to run the game
     */
    protected Interface(GamePanel gp) {
        this.gp = gp;

        try {
            InputStream input = getClass().getResourceAsStream("/font/pixelfont.otf");
            pressStart = Font.createFont(Font.TRUETYPE_FONT, input);
        }
        catch (FontFormatException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Centers a string of text in the middle of the provided screen width.
     *
     * @param text the text that is to be centered
     * @param g2 the main graphics object that is used to draw the text onto the screen
     * @param screenWidth the current width of the screen
     * @return coordinate of text written
     */
    protected int getHorizontalCenter(String text, Graphics2D g2, int screenWidth) {
        int textLength = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = screenWidth/2 - textLength/2; // Print message on center of screen
        return x;
    }

    /**
     * Moves the selector up on the screen.
     * Prevents the user from moving the selector off the screen
     */
    public void moveSelectorUp() {
        selectPosition--;
        if (selectPosition < 0) {
            selectPosition = totalOptions;
        }
    }

    /**
     * Increases the selector position.
     * Prevents the user from moving the selector off the screen
     */
    public void moveSelectorDown() {
        selectPosition++;
        if (selectPosition > totalOptions) {
            selectPosition = 0;
        }
    }

    /**
     * @return selector position
     */
    public int getSelectorPosition() {
        return selectPosition;
    }

    /**
     * Sets selector position back to zero.
     * Should be used everytime the game state is changed.
     */
    public void resetSelectorPosition() {
        selectPosition = 0;
    }

    public void draw(Graphics2D g2) {}
}