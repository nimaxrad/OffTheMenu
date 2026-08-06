package object;
import java.io.IOException;

import javax.imageio.ImageIO;

import app.SetAnimationLoader;
import core.GamePanel;
import audio.SoundEffects;
import entity.PlayerPig;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;


/**
 * @author nimarad
 * Electric trap object.
 * Damages or punishes the player when touched.
 * removed afrer activation
 */
public class OBJ_Trap extends SuperObject {
    private static java.awt.image.BufferedImage trapImage;
    private static boolean imageLoaded = false;

    SoundEffects sound;
    private int scorePenalty;
    private boolean active;

    /**
     * Creates an electric trap object.
     */
    public OBJ_Trap() {
        name = "ElectricTrap";
        collision = true;   // player should be able to step on it
        scorePenalty = 25;
        active = true;

        if (!imageLoaded) {
            try {
                trapImage = ImageIO.read(getClass().getResourceAsStream("/tiles/trap_electric_fence.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            imageLoaded = true;
        }
        image = trapImage;

        sound = SoundEffects.getInstance();
        try {
            Animation = new SetAnimationLoader("/ingameStats/trap.png", 10);
        } catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Creates an electric trap at a specific world position.
     *
     * @param worldX x position in world coordinates
     * @param worldY y position in world coordinates
     */
    public OBJ_Trap(int worldX, int worldY) {
        this();
        this.worldX = worldX;
        this.worldY = worldY;
    }

    public void triggerHazard(PlayerPig player){
        if (player == null || !active) {
            return;
        }

        if (!player.isDead()) {
            player.loseHeart();
            player.deductScore(50);
            sound.play(1);
            sound.play(2);
        }
        removeTrap();

    }
    /**
     * Returns the trap's score penalty.
     *
     * @return score deducted when triggered
     */
    public int getScorePenalty() {
        return scorePenalty;
    }

    /**
     * Sets the trap's score penalty.
     *
     * @param scorePenalty new penalty amount
     */
    public void setScorePenalty(int scorePenalty) {
        this.scorePenalty = scorePenalty;
    }

    /**
     * Returns whether this trap is still active.
     *
     * @return true if active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Deactivates the trap. deletes after stepped on
     */
    public void deactivate() {
        active = false;
    }

    /**
     * Activates the trap.
     */
    public void activate() {
        active = true;
    }

    /**
     * Called every frame if needed.
     * You can leave this empty for static traps.
     *
     * @param gp GamePanel object that is used to run the game
     */
    @Override
    public void update(GamePanel gp) {
        // Static trap for now, so no special per-frame logic needed.
        sound.play(1);
        sound.play(2);
    }

    /**
     * Removes the trap from the game.
     * This hides it and moves it off the map.
     */
    public void removeTrap() {
        active = false;
        worldX = -9999;
        worldY = -9999;
        image = null;
    }

    /**
     * Applies the trap effect to the player.
     *
     * @param player object
     */

}

