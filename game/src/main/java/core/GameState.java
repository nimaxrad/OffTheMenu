package core;

/**
 * @author nathanomana
 *         Tracks the current state of the game: mode, score, hearts, and pig
 *         rescue progress.
 *         All state changes happen through dedicated methods to keep game logic
 *         cohesive.
 */

import app.Mode;

public class GameState {

    // Fields
    private Mode mode;
    private Hearts hearts;
    private Score score;
    private PigRescueProgress pigRescueProgress;

    // Constructor

    /**
     * Initializes a new GameState instance with default values.
     * Sets mode to START, hearts to 3, scores to 0, and clears pig rescue progress.
     */
    public GameState() {
        this.mode = Mode.START;
        this.hearts = new Hearts(3);
        this.score = new Score();
        this.pigRescueProgress = new PigRescueProgress();
    }

    // Mode Management

    /**
     * Sets the active game mode.
     *
     * @param m the selected Mode (e.g., START, PLAYING, PAUSE, WON, LOST)
     */
    public void setMode(Mode m) {
        this.mode = m;
    }

    /**
     * Gets the current game mode.
     *
     * @return the active Mode
     */
    public Mode getMode() {
        return mode;
    }

    // Hearts

    /**
     * Deducts one heart from the player's health.
     * This method is generally called when the player takes damage.
     * Will not reduce hearts below zero.
     */
    public void loseHeart() {
        hearts.loseHeart();
    }

    /**
     * Checks whether the game is over due to the player losing all their hearts.
     *
     * @return true if hearts are 0 or below, false otherwise
     */
    public boolean isGameOver() {
        return hearts.isGameOver();
    }

    /**
     * Gets the player's current health (hearts).
     *
     * @return the number of remaining hearts
     */
    public int getHearts() {
        return hearts.getCount();
    }

    // Score

    /**
     * Adds the specified amount of points to the player's current score.
     * Automatically updates the high score if the current score surpasses it.
     *
     * @param points the amount to add to the score
     */
    public void addScore(int points) {
        score.addScore(points);
    }

    /**
     * Gets the player's current session score.
     *
     * @return the current score
     */
    public int getScore() {
        return score.getCurrentScore();
    }

    /**
     * Gets the player's highest achieved score across sessions.
     *
     * @return the high score
     */
    public int getHighScore() {
        return score.getHighScore();
    }

    // Pig Rescue Progress

    /**
     * Sets the target number of pigs the player must rescue to complete the level.
     *
     * @param goal the required number of pigs
     */
    public void setPigsGoal(int goal) {
        pigRescueProgress.setGoal(goal);
    }

    /**
     * Increments the number of rescued pigs by one.
     */
    public void rescuePig() {
        pigRescueProgress.rescuePig();
    }

    /**
     * Checks if the player has met the rescue goal required to unlock the map exit.
     *
     * @return true if rescued pigs is greater than or equal to the goal
     */
    public boolean hasMetPigGoal() {
        return pigRescueProgress.hasMetGoal();
    }

    /**
     * Gets the number of pigs the player has safely rescued.
     *
     * @return the count of rescued pigs
     */
    public int getPigsRescued() {
        return pigRescueProgress.getRescued();
    }

    /**
     * Gets the total target number of pigs needed to be rescued.
     *
     * @return the target pig rescue goal
     */
    public int getPigsGoal() {
        return pigRescueProgress.getGoal();
    }
}
