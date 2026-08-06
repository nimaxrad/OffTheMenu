package object;
import java.io.IOException;
import java.awt.Graphics2D;
import javax.imageio.ImageIO;

import core.GamePanel;
import audio.SoundEffects;
import entity.PlayerPig;

import java.util.Random;


/**
 * Wheat object that player collects to earn points
 * Respawns in a different location after a certain amount of time
 *
 * @author KenTran
 */
public class OBJ_Wheat extends SuperObject {
    private static java.awt.image.BufferedImage wheatImage;
    private static boolean imageLoaded = false;

    SoundEffects sound;
    int remainingTicks;
    Random randGen = new Random();
    double expireTime;

    /**
     * Creates a new wheat object and loads its sprite.
     * Links Sound singleton to this class;
     */
    public OBJ_Wheat() {
        name = "Wheat";
        remainingTicks = 300;

        if (!imageLoaded) {
            try {
                wheatImage = ImageIO.read(getClass().getResourceAsStream("/object/object_wheat.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            imageLoaded = true;
        }
        image = wheatImage;
//        collision = true;

        sound = SoundEffects.getInstance();

        expireTime = (double) (30 + randGen.nextInt(60));
    }

    /**
     * Creates a wheat object at a specific world position.
     *
     * @param worldX x position in world coordinates
     * @param worldY y position in world coordinates
     */
    public OBJ_Wheat(int worldX, int worldY) {
        this();
        this.worldX = worldX;
        this.worldY = worldY;
    }

    /**
     * Adds score when collected and plays a sound
     *
     */
    public void update() {
        remainingTicks--;
        //player.score += 100;
        if(remainingTicks <= 0){
            removeWheat();
        }
    }
    public void pickUp(PlayerPig player){
        if (player == null) {
            return;
        }

        player.addScore(100);
        sound.play(0);

        this.removeWheat();
    }

    public void removeWheat(){
        this.worldX = -9999;
        this.worldY = -9999;
        this.image = null;
        this.remainingTicks = 0;

    }



    public int getRemainingTicks(){
        return remainingTicks;
    }

}
