package input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import core.GamePanel;
import audio.Music;
import audio.SoundEffects;

/**
 * Handles keyboard input for the game.
 * Implements {@link KeyListener} to track the state of movement and action keys.
 *
 * @author Ken Tran
 */
public class KeyHandler implements KeyListener {
    Music music;
    SoundEffects sound;
    GamePanel gp;

    /** Movement states for the W, A, S, and D keys. */
    public boolean upPressed, downPressed, leftPressed, rightPressed;
    /** State for the Enter key. */
    public boolean enterPressed;

    /** Flags for selection and menu navigation. */
    public boolean select, enter;
    /** Flag to track if the game is in a paused state. */
    public boolean paused;

    /**
     * Invoked when a key has been typed. Not used in this implementation.
     *
     * @param e The event to be processed.
     */
    @Override
    public void keyTyped(KeyEvent e) {
    }

    /**
     * Invoked when a key is pressed. Sets the corresponding boolean flag to {@code true}
     * based on the key code (W, A, S, D, or ENTER).
     *
     * @param e The event to be processed.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W) {
            upPressed = true;
        }
        if (code == KeyEvent.VK_A) {
            leftPressed = true;
        }
        if (code == KeyEvent.VK_S) {
            downPressed = true;
        }
        if (code == KeyEvent.VK_D) {
            rightPressed = true;
        }
        if (code == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }
    }

    /**
     * Invoked when a key is released. Sets the corresponding boolean flag to {@code false}
     * based on the key code (W, A, S, D, or ENTER).
     *
     * @param e The event to be processed.
     */
    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W) {
            upPressed = false;
        }
        if (code == KeyEvent.VK_A) {
            leftPressed = false;
        }
        if (code == KeyEvent.VK_S) {
            downPressed = false;
        }
        if (code == KeyEvent.VK_D) {
            rightPressed = false;
        }
        if (code == KeyEvent.VK_ENTER) {
            enterPressed = false;
        }
    }
}