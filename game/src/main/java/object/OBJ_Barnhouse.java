package object;
import java.awt.image.BufferedImage;

import java.io.IOException;

import javax.imageio.ImageIO;

import core.GamePanel;

//import GamePanel;

/**
 * Barnhouse for player to escape after rescuing all the pigs
 *
 * @author Ken Tran
 */
public class OBJ_Barnhouse extends SuperObject {
    BufferedImage closedBarn, openedBarn;
    Boolean open = false;

    /**
     * Creates a new barnhouse object and loads its sprites.
     */
    public OBJ_Barnhouse() {
        name = "Barnhouse";
        try {
            closedBarn = ImageIO.read(getClass().getResourceAsStream("/tiles/barnhouse.png"));
            openedBarn = ImageIO.read(getClass().getResourceAsStream("/tiles/barnhousewinter.png"));
        }
        catch(IOException e) {
            e.printStackTrace();
        }
//        collision = true;
        image = closedBarn;
    }

    /**
     * Opens the barn after player rescued all pigs
     */
    public void update(GamePanel gp) {
        if (open == false) {
            open = true;
            image = openedBarn;
        }

    }

}