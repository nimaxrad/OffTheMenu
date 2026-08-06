package core;

/**
 * @author nathanomana
 *         Class that tracks the players heart count and sets
 *         the game over state when the player runs out of hearts.
 */
public class Hearts {
    private int count;

    public Hearts(int initialCount) {
        this.count = initialCount;
    }

    public void loseHeart() {
        if (count > 0) {
            count--;
        }
    }

    public boolean isGameOver() {
        return count <= 0;
    }

    public int getCount() {
        return count;
    }
}
