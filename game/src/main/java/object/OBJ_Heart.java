package object;
import java.awt.image.BufferedImage;

import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Heart object that is used to display player health onto the screen.
 * Heart sprite changes depending on the player current health points.
 *
 * @author Ken Tran
 */
public class OBJ_Heart extends SuperObject{
    BufferedImage heart, emptyHeart;

    /**
     * Creates a new heart object and loads its sprite.
     * Default sprite is set to full heart.
     */
    public OBJ_Heart() {

        name = "Heart";
        try {
            heart = ImageIO.read(getClass().getResourceAsStream("/ingameStats/ui_heart_full.png"));
            emptyHeart = ImageIO.read(getClass().getResourceAsStream("/ingameStats/ui_heart_empty.png"));

        }catch(IOException e) {
            e.printStackTrace();
        }
        image = heart;
    }

    /**
     * Changes the sprite of this object to a full heart.
     */
    public void fullHeart() {
        image = heart;
    }

    /**
     * Changes the sprite of this object to an empty heart.
     */
    public void emptyHeart() {
        image = emptyHeart;
    }


}