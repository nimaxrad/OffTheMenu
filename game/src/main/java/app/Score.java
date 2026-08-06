package core;

/**
 * Manages the player's scoring state, including the current session score
 * and the all-time high score.
 *
 * @author Ken Tran
 */
public class Score {
    private int currentScore;
    private int highScore;

    /**
     * Initializes a new Score object with both current and high scores set to zero.
     */
    public Score() {
        this.currentScore = 0;
        this.highScore = 0;
    }

    /**
     * Adds points to the current score and updates the high score if
     * the new total exceeds it.
     *
     * @param points The number of points to add to the score.
     */
    public void addScore(int points) {
        this.currentScore += points;
        if (this.currentScore > this.highScore) {
            this.highScore = this.currentScore;
        }
    }

    /**
     * Retrieves the score for the current session.
     *
     * @return The current point total.
     */
    public int getCurrentScore() {
        return currentScore;
    }

    /**
     * Retrieves the highest score achieved during this object's lifecycle.
     *
     * @return The highest point total recorded.
     */
    public int getHighScore() {
        return highScore;
    }
}