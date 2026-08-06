package object;
import java.io.IOException;

import javax.imageio.ImageIO;

import app.SetAnimationLoader;
import audio.SoundEffects;
import entity.PlayerPig;



/**
 * Pigs that the main character has to rescue
 * @author nimarad
 * @author Ken Tran
 */
public class OBJ_Pig extends SuperObject {
    private static java.awt.image.BufferedImage pigImage;
    private static boolean imageLoaded = false;

    SoundEffects sound;
    private int score;
    private boolean active;

    /**
     * Creates a new key object and loads it sprite.
     * Also links sound singleton to this object.
     */
    public OBJ_Pig() {
        collision = true;   // player should be able to step on it
        score = 50;
        active = true;
        try {
            saveAnimation = new SetAnimationLoader("/ingameStats/7.png", 17);
        } catch(IOException e)
        {
            e.printStackTrace();
        }

        name = "Pig";
        if (!imageLoaded) {
            try {
                pigImage = ImageIO.read(getClass().getResourceAsStream("/object/object_pig_rescue.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            imageLoaded = true;
        }
        image = pigImage;


        sound = SoundEffects.getInstance();
    }

    /**
     * Creates a pigNPC at a specific world position.
     *
     * @param worldX x position in world coordinates
     * @param worldY y position in world coordinates
     */
    public OBJ_Pig(int worldX, int worldY) {
        this();
        this.worldX = worldX;
        this.worldY = worldY;
    }

    /**
     * Adds 100 point for player when they pick up a pig
     * Plays a sound confirming it
     * @param player
     */
    public void pickUp(PlayerPig player){
        if (player == null) {
            return;
        }

        if (!player.hasMetRescueGoal()) {
            player.rescuePig();
        }

        player.addScore(100);
        sound.play(0);
        this.remove();
    }


    /**
     * Removes item after being picked up
     */
    public void remove(){
        worldX = -9999;
        worldY = -9999;
        image = null;
    }

}
