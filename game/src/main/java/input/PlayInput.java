package input;
import java.awt.event.KeyEvent;

import core.GamePanel;
import app.StateManager;

/**
 * Handles input from the player's keyboard on the Play state.
 *
 * @author Ken Tran
 */
public class PlayInput extends InputHandler {
    // Konami code sequence
    int[] konamiCode = {
            KeyEvent.VK_UP,
            KeyEvent.VK_UP,
            KeyEvent.VK_DOWN,
            KeyEvent.VK_DOWN,
            KeyEvent.VK_LEFT,
            KeyEvent.VK_RIGHT,
            KeyEvent.VK_LEFT,
            KeyEvent.VK_RIGHT,
            KeyEvent.VK_B,
            KeyEvent.VK_A,
            KeyEvent.VK_ENTER
    };
    int konamiCount = 0;
    public boolean konamiActivate = false;

    /**
     * Calls the InputHandler constructor.
     *
     * @param gp GamePanel object that is used to run the game
     */
    protected PlayInput(GamePanel gp) {
        super(gp);
    }

    /**
     * Called every time a key is pressed in the Play state.
     * Different actions are bound to different keys.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        super.keyPressed(e);
        int keyCode = e.getKeyCode();

        switch(keyCode) {
            // Movement
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                up = true;
                break;
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                left = true;
                break;
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                down = true;
                break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                right = true;
                break;

            // Pause
            case KeyEvent.VK_ESCAPE:
                if (!paused) {
                    sound.play(6);
                    gp.stateM.setCurrentState(StateManager.gameState.PAUSE);
                }
                paused = true;
        }
    }

    /**
     * Called every time a key is released in the Play state.
     * Different actions are bound to different keys.
     * Gives player 99 lives when Konami Code is inputted.
     */
    @Override
    public void keyReleased(KeyEvent e) {
        super.keyReleased(e);
        int keyCode = e.getKeyCode();

        switch(keyCode) {
            // Movement
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                up = false;
                break;
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                left = false;
                break;
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                down = false;
                break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                right = false;
                break;


            // Pause Menu functions
            case KeyEvent.VK_ESCAPE:
                paused = false;
                break;
            case KeyEvent.VK_ENTER:
                enter = false;
        }

        // Konami code
        if (!konamiActivate) {
            if (keyCode == konamiCode[konamiCount]) {
                konamiCount++;
            }
            else {
                konamiCount = 0;
            }

            if (konamiCount == 11) {
                System.out.println("Cheats Activated!");
                konamiCount = 0;
                konamiActivate = true;
                gp.player.health += 99;
                sound.play(9);
                gp.uiM.showMessage("+99 HP");
            }
        }
    }

}