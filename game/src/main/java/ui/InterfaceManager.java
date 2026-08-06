package ui;
import java.awt.Graphics2D;

import core.GamePanel;
import app.StateManager;

/*

Nima: refactoring
duplicate code, too many calls to ui[gp.stateM.getCurrentState().getValue()]

private Interface getCurrentUI() {
    return ui[gp.stateM.getCurrentState().getValue()];
}

Then I replaced repeated lines with:

getCurrentUI()

 */
/**
 * Manages the UI depending on the current state of the game.
 * Contains methods to control different aspects of the UI.
 *
 * @author Ken Tran
 */
public class InterfaceManager {
    GamePanel gp;
    Interface ui[];

    boolean fullScreen;
    // Refactored by adding these values to fix magic numbers issue. Adds clarity to code

    private static final int PLAY_INDEX = 0;
    private static final int PAUSE_INDEX = 1;
    private static final int WIN_INDEX = 2;
    private static final int LOSE_INDEX = 3;
    private static final int TITLE_INDEX = 4;
    /**
     * Createds an array and fills it with each game screen.
     *
     * @param gp GamePanel object that is used to run the game
     */
    public InterfaceManager(GamePanel gp) {
        this.gp = gp;

        ui = new Interface[6];
        ui[PLAY_INDEX] = new PlayScreen(gp);
        ui[PAUSE_INDEX] = new PauseScreen(gp);
        ui[WIN_INDEX] = new WinScreen(gp);
        ui[LOSE_INDEX] = new LoseScreen(gp);
        ui[TITLE_INDEX] = new TitleScreen(gp);

//        ui[0] = new PlayScreen(gp);
//        ui[1] = new PauseScreen(gp);
//        ui[2] = new WinScreen(gp);
//        ui[3] = new LoseScreen(gp);
//        ui[4] = new TitleScreen(gp);
    }

    /**
     * Draws the user interface of the current game state onto the player's screen.
     *
     * @param g2 main graphics object used by gamePanel to draw the maps sprites and tiles
     */
    public void draw(Graphics2D g2) {
        if (gp.stateM.getCurrentState() == StateManager.gameState.PAUSE) {
            ui[PLAY_INDEX].draw(g2);
            ui[PAUSE_INDEX].draw(g2);
        } else {
            getCurrentUI().draw(g2);
        }
    }

    /**
     * Moves the user's selector up on the screen.
     */
    public void moveSelectorUp() {
        getCurrentUI().moveSelectorUp();
    }

    /**
     * Moves the user's selector down on the screen.
     */
    public void moveSelectorDown() {
        getCurrentUI().moveSelectorDown();
    }

    /**
     * @return the position of the selector
     */
    public int getSelectorPosition() {
        return getCurrentUI().getSelectorPosition();
    }

    /**
     * Resets the postion of the selector to zero
     */
    public void resetSelectorPosition() {
        getCurrentUI().resetSelectorPosition();
    }

    /**
     * @return the status of full screen
     */
    public boolean getFullScreen() {
        return fullScreen;
    }

    /**
     * Saves the status of full screen.
     * Writes the status to the configuration file.
     *
     * @param fullScreen a boolean for the status of full screen
     */
    public void setFullScreen(boolean fullScreen) {
        this.fullScreen = fullScreen;
    }

    /**
     * Resets the player's messages on the PlayScreen.
     */
    public void resetPlayScreen() {
        PlayScreen playScreen = (PlayScreen) ui[PLAY_INDEX];
        playScreen.resetMessage();
    }

    /**
     * Draws the provided message onto the user's screen.
     * Only runs if the player is in the game state play; otherwise does nothing.
     *
     * @param message the message to be displayed on the screen
     */
    public void showMessage(String message) {
        if (gp.stateM.getCurrentState() == StateManager.gameState.PLAY) {
            PlayScreen playScreen = (PlayScreen) getCurrentUI();
            playScreen.showMessage(message);
        }
    }

    /**
     * If player enters win or lose screen, show new high score message if player's score is higher than the saved score.
     *
     * @param showHighScore boolean value for whether if player's score is a new high score
     */
    public void showNewHighScore(boolean showHighScore) {
        StateManager.gameState currState = gp.stateM.getCurrentState();
        if (currState == StateManager.gameState.WIN) {
            WinScreen winScreen = (WinScreen) ui[currState.getValue()];
            winScreen.showNewHighScore(showHighScore);
        }
        else if (currState == StateManager.gameState.LOSE) {
            LoseScreen loseScreen = (LoseScreen) ui[currState.getValue()];
            loseScreen.showNewHighScore(showHighScore);
        }
    }

    /**
     * @return the player's play time from the play screen
     */
    public int getPlayTime() {
        PlayScreen playScreen = (PlayScreen) ui[PLAY_INDEX];
        return playScreen.getPlayTime();
    }
    private Interface getCurrentUI() {
        return ui[gp.stateM.getCurrentState().getValue()];
    }

}