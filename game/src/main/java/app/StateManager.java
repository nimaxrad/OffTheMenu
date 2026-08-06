package app;
import java.awt.Graphics2D;

import audio.Music;
import audio.SoundEffects;

import core.GamePanel;
/**
 * Manages and controls the differing game states of our game.
 * Contains methods used to change the user's current state and draw or update objects and entities depending on the current state.

 *
 * @author Ken Tran
 */
public class StateManager {
    private StateTransitionHandler handler; //Nima: added this

    // Game States
    public enum gameState { // removed
        PLAY(0),
        PAUSE(1),
        WIN(2),
        LOSE(3),
        TITLE(4);

        private int value;

        /**
         * Constructs a new gameState enum and associates it with a value
         *
         * @param value of the gameState
         */
        private gameState(int value) {
            this.value = value;
        }

        /**
         * @return the numerical value of the gameState
         */
        public int getValue() {
            return value;
        }
    }

    gameState currState, prevState;

    Music music;
    SoundEffects sound;
    GamePanel gp;


    /**
     * Constructs a new StateManager object and links the Music and Settings singletons to this class.
     * Sets the current game state to title.
     * Plays the default background track.
     *
     * @param gp GamePanel object that is used to run the game
     */
    public StateManager(GamePanel gp) {

        this.gp = gp;
        music = Music.getInstance();
        sound = SoundEffects.getInstance();
        currState = gameState.TITLE;
        music.play(0);
        handler = new StateTransitionHandler(gp, music, sound); // Nima: added this
    }

    /**
     * Sets the current state to the new state.
     * Saves the previous state.
     * Resets the position of the selector.
     * Changes music depending on the game state.
     * Also changes the current input handler.
     *
     * @param state the new state the user will be entering
     */
    public void setCurrentState(gameState state) { //Converted switch statement into a separate class
        prevState = currState;
        currState = state;

        gp.inputM.changeInput(state);
        syncEngineMode(state);

        handler.handle(state, prevState);
        //Nima:
        //We’ll move the state-specific behavior out of StateManager into a separate class that handles transitions.

//        prevState = currState;
//        currState = state;
//
//        gp.inputM.changeInput(state);
//        syncEngineMode(state);
//
//        switch(state) {
//            case WIN:
//                music.stop();
//                sound.play(3);
//                // Calculate score based on play time
//                int score = calculateScore();
//
//                gp.player.score = score;
//                break;
//
//            case LOSE:
//                music.stop();
//                sound.play(4);
//                boolean showHighScore;
//                if (gp.player.score > 0) {
//                    showHighScore = true;
//                }
//                else {
//                    showHighScore = false;
//                }
//                gp.uiM.showNewHighScore(showHighScore);
//                break;
//            case PLAY:
//                if (prevState != gameState.PAUSE) {
//                    resetLevelForNewRun();
//                }
//                music.stop();
//                music.play(0);
//            case PAUSE:
//                break;
//            default:
//                gp.uiM.resetPlayScreen();
//                gp.player.setDefaultValues();
//                music.play(0);
//
//        }
//
//        gp.uiM.resetSelectorPosition();
    }

    /**
     * Resets the game's map, player and ui to default values.
     * Changes game state and input to play.
     */
    public void retryGame() {
        prevState = currState;
        currState = gameState.PLAY;
        syncEngineMode(currState);

        gp.inputM.changeInput(currState);
        resetLevelForNewRun();
        music.play(0);

    }

    /**
     * @return the user's current game state
     */
    public gameState getCurrentState() {
        return currState;
    }

    /**
     * @return the user's previous game state
     */
    public gameState getPreviousState() {
        return prevState;
    }

    /**
     * Returns the user back to their previous game state.
     */
    public void revertPreviousState() {
        gameState temp = currState;
        currState = prevState;
        prevState = temp;

        gp.inputM.changeInput(currState);
        syncEngineMode(currState);
    }

    /**
     * Draws the player and tile sprites onto the screen if the user is in gameState play or pause.
     * Always draws the user interface.
     *
     * @param g2 main graphics object used by gamePanel to draw the maps sprites and tiles
     */
    public void draw(Graphics2D g2) {
        // TODO: draw map tiles and player sprite here
        // if (currState == gameState.PLAY || currState == gameState.PAUSE) {
        //     gp.mapM.draw(g2);
        //     gp.player.draw(g2);
        // }

        gp.uiM.draw(g2);
    }

    /**
     * Calls the update method for player and the other entities on the map.
     * Only updates during gameplay; otherwise does nothing.
     */
    public void update() {
        if (currState == gameState.PLAY) {
            gp.player.update();
        }

    }

    /**
     * Calculate the current score depending on the playTime
     * @return score
     */

    public int calculateScore() {
        int score = gp.player.score;
        double scoreMultiplier;

        int playTime = gp.uiM.getPlayTime();

        if (playTime <= 900) { // if play time is under 15 minutes, give score
            scoreMultiplier = (double) 2 - ((double) playTime/900);
        }
        else {
            scoreMultiplier = 1;
        }

        score *= scoreMultiplier;

        return score;
    }

    private void syncEngineMode(gameState state) {
        Mode mode;

        switch (state) {
            case PLAY:
                mode = Mode.PLAYING;
                break;
            case PAUSE:
                mode = Mode.PAUSE;
                break;
            case WIN:
                mode = Mode.WON;
                break;
            case LOSE:
                mode = Mode.LOST;
                break;
            case TITLE:
            default:
                mode = Mode.START;
                break;
        }

        gp.getEngine().getState().setMode(mode);
    }


    private void resetLevelForNewRun() {
        gp.getEngine().setElapsedTicks(0);
        gp.uiM.resetPlayScreen();
        gp.player.setDefaultValues();

        if (gp.level != null) {
            gp.level.spawnEntities(gp.getEngine().getMap(), gp);
        }
    }
}
