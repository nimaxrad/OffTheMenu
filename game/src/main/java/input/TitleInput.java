package input;
import java.awt.event.KeyEvent;

import core.GamePanel;
import app.StateManager;
import app.Mode;


/**
 * Handles input from the player's keyboard in the Title state.
 *
 * @author Ken Tran
 */
public class TitleInput extends InputHandler {

    /**
     * Calls the InputHandler constructor.
     *
     * @param gp GamePanel object that is used to run the game
     */
    protected TitleInput(GamePanel gp) {
        super(gp);
    }

    /**
     * Called every time a key is pressed in the Title state.
     * Different actions are bound to different keys.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        switch(keyCode) {
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
                    if (position == 0){ // Play
                        gp.stateM.setCurrentState(StateManager.gameState.PLAY);
                        // Also sync Nathan's engine mode
                        gp.getEngine().getState().setMode(Mode.PLAYING);
                    }
                    else if (position == 1) { // Quit
                        System.exit(0);
                    }
                }
                enter = true;
                break;
        }
    }

}