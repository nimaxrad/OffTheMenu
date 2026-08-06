package core;

/**
 * Tracks the progress of rescuing pigs, including the current count
 * and the target goal.
 * @author Ken Tran
 */
public class PigRescueProgress {
    /** The current number of rescued pigs. */
    private int rescued;

    /** The target number of pigs to rescue. */
    private int goal;

    /**
     * Constructs a new PigRescueProgress object with 0 pigs rescued
     * and a goal of 0.
     */
    public PigRescueProgress() {
        this.rescued = 0;
        this.goal = 0;
    }

    /**
     * Sets the rescue goal.
     *
     * @param goal the target number of pigs to rescue
     */
    public void setGoal(int goal) {
        this.goal = goal;
    }

    /**
     * Gets the target rescue goal.
     *
     * @return the goal number of pigs
     */
    public int getGoal() {
        return goal;
    }

    /**
     * Increments the count of rescued pigs by one.
     */
    public void rescuePig() {
        rescued++;
    }

    /**
     * Checks if the rescue goal has been met or exceeded.
     *
     * @return true if rescued pigs is greater than or equal to the goal,
     *         false otherwise
     */
    public boolean hasMetGoal() {
        return rescued >= goal;
    }

    /**
     * Gets the current number of rescued pigs.
     *
     * @return the number of rescued pigs
     */
    public int getRescued() {
        return rescued;
    }
}
