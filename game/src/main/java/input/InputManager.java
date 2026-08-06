package input;
import core.GamePanel;
import app.StateManager;

/**
 * Manages the input handlers for each game state.
 *
 * @author Ken Tran
 */
public class InputManager {
    GamePanel gp;
    InputHandler[] inputs;

    InputHandler currInput, prevInput;

    /**
     * Constructs a new InputManager object and initializes an array with each input handler position corresponding with the value of its game state.
     * Sets default input handler to TitleInput.
     *
     * @param gp GamePanel object that is used to run the game
     */
    public InputManager(GamePanel gp) {
        this.gp = gp;

        inputs = new InputHandler[7];
        inputs[0] = new PlayInput(gp);
        inputs[1] = new PauseInput(gp);
        inputs[2] = new WinLoseInput(gp); // Win is 2
        inputs[3] = inputs[2]; //Lose is 3
        inputs[4] = new TitleInput(gp);

        currInput = inputs[4];
    }

    /**
     * Saves previous game state and sets key listener in GamePanel to the current one.
     *
     * @param state the new game state
     */
    public void changeInput(StateManager.gameState state) {
        prevInput = currInput;
        currInput = inputs[state.getValue()];
        currInput.resetKeyPress();

        gp.removeKeyListener(prevInput);
        gp.addKeyListener(currInput);
    }

    /**
     * @return the current input handler
     */
    public InputHandler getCurrentInput() {
        return currInput;
    }

    /**
     * @return the input handler for Play state
     */
    public InputHandler getPlayInput() {
        return (InputHandler) inputs[0];
    }
}