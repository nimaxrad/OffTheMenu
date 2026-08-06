package object;
import java.awt.image.BufferedImage;

import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Pig icon object that is used to display remaining pigs
 *
 * @author Ken Tran
 */
public class OBJ_PigIcon extends SuperObject{
    BufferedImage pig, emptyPig;

    /**
     * Creates a new pig icon object and loads its sprite.
     * Default sprite is set to empty.
     */
    public OBJ_PigIcon() {

        name = "PigIcon";
        try {
            pig = ImageIO.read(getClass().getResourceAsStream("/ingameStats/ui_pig_empty.png"));
            emptyPig = ImageIO.read(getClass().getResourceAsStream("/ingameStats/ui_pig_captured.png"));

        }catch(IOException e) {
            e.printStackTrace();
        }
        image = emptyPig;
    }

    /**
     * Changes the sprite of this object to a pig.
     */
    public void pig() {
        image = pig;
    }

    /**
     * Changes the sprite of this object to an empty pig.
     */
    public void emptyPig() {
        image = emptyPig;
    }


}