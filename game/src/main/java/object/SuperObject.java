package object;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import app.SetAnimationLoader;
import core.GamePanel;
import app.*;
/**
 * Abstract class for game objects that contains the objects properties.
 * Data stored in this object are used by other objects.
 *
 * @author Ken Tran
 */
public abstract class SuperObject {

    public BufferedImage image;
    public String name;
    public boolean collision = false;
    public int worldX, worldY;
    //set object hitbox
    public int hitboxDefaultX = 0;
    public int hitboxDefaultY = 0;
    public int index;
    protected SetAnimationLoader saveAnimation;
    protected SetAnimationLoader Animation;

    /**
     * Determines the location of the object on the map.
     *
     * @param g2 the main graphics object used to draw object sprites onto the screen
     * @param gp GramePanel object that is used to run the game
     */
    public void draw(Graphics2D g2, GamePanel gp)
    {
        int drawX = worldX * GamePanel.TILE_SIZE;
        int drawY = worldY * GamePanel.TILE_SIZE;

        g2.drawImage(image, drawX, drawY, GamePanel.TILE_SIZE, GamePanel.TILE_SIZE, null);
        //System.out.println(this.getClass().getName());
        if(this.getClass().getName() == "object.OBJ_Pig"){
            saveAnimation.update();

            g2.drawImage(saveAnimation.getCurrentFrame(), drawX, drawY - gp.TILE_SIZE + 4, gp.TILE_SIZE-2, gp.TILE_SIZE-2, null);

        }
        if(this.getClass().getName() == "object.OBJ_Trap"){
            Animation.update();

            g2.drawImage(Animation.getCurrentFrame(), drawX+6, drawY+6, gp.TILE_SIZE-12, gp.TILE_SIZE-12, null);

        }

    }

    /**
     * Implemented by different objects for different features.
     *
     * @param gp GamePanel object that is used to run the game
     */
    public void update(GamePanel gp) {
    }
    public int getX(){
        return worldX;
    }
    public int getY(){
        return worldY;
    }
}