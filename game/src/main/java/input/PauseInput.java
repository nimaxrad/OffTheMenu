package input;
import java.awt.event.KeyEvent;

import core.GamePanel;
import app.StateManager;


/**
 * Handles input from the player's keyboard in the Pause state.
 *
 * @author Ken Tran
 */
public class PauseInput extends InputHandler {

    /**
     * Calls the InputHandler constructor.
     *
     * @param gp GamePanel object that is used to run the game
     */
    protected PauseInput(GamePanel gp) {
        super(gp);
    }

    /**
     * Called every time a key is pressed in the Pause state.
     * Different actions are bound to different keys.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        switch(keyCode) {
            // Unpause
            case KeyEvent.VK_ESCAPE:
                if (!paused) {
                    sound.play(6);
                    gp.stateM.setCurrentState(StateManager.gameState.PLAY);
                }
                paused = true;
                break;

                // Select
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                if (!select) {
                    sound.play(5);
                    gp.uiM.moveSelectorUp();
                }
                select = true;
                break;
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                if (!select) {
                    sound.play(5);
                    gp.uiM.moveSelectorDown();
                }
                select = true;
                break;

            // Enter
            case KeyEvent.VK_ENTER:
                if (!enter) {
                    int position = gp.uiM.getSelectorPosition();
                    sound.play(6);
                    if (position == 0) { // resume
                        gp.stateM.setCurrentState(StateManager.gameState.PLAY);
                    }
                    else if (position == 1) { // Main Menu
                        gp.stateM.setCurrentState(StateManager.gameState.TITLE);
                    }
                    else if (position == 2) { // Quit
                        System.exit(0);
                    }
                }
                enter = true;
                break;
        }
    }

}