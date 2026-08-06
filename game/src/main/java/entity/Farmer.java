package entity;
import java.io.IOException;
import javax.imageio.ImageIO;

import app.*;
import core.GameEngine;
import core.GamePanel;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.lang.Math;

/**
 * Moving farmer enemy that wakes up when the player is close
 * moves one tile per tick towards the player
 *
 * @author Joshua Tan
 * @author Ken Tran
 * @version 1.0
 */
public class Farmer {
    private int x, y; // tile position (world coordinate)
    private boolean awake;
    private int detectionRadius;
    private Direction facing;

    private boolean dangerSign; // reaction trigger

    private int renderX = -1;
    private int renderY = -1;

    private static java.awt.image.BufferedImage up1, up2, up3, down1, down2, down3, left1, left2, left3, right1, right2, right3;
    private static BufferedImage reaction;


    private static boolean imagesLoaded = false;
    int spriteCounter = 0;
    int spriteNum = 1;

    private int moveCounter = 0;
    private int moveInterval = 17;

    private SetSpriteLoader loader;
    private BufferedImage[] frames;

    private SetAnimationLoader sleepAnimation;
    private SetAnimationLoader wakeUpAnimation;
    /**
     * Creates a farmer enemy at the given tile position.
     *
     * @param startX starting x tile
     * @param startY starting y tile
     * @param detectionRadius Manhattan-distance wake radius
     */
    public Farmer(int startX, int startY, int detectionRadius) {
        this.x = startX;
        this.y = startY;
        this.detectionRadius = detectionRadius;
        this.awake = false;
        this.facing = Direction.DOWN;
        this.spriteNum = 1;

        if (!imagesLoaded) {
            getFarmerImage();
            imagesLoaded = true;
        }
        try {
            sleepAnimation = new SetAnimationLoader("/ingameStats/021.png", 30);
            wakeUpAnimation = new SetAnimationLoader("/ingameStats/039.png", 30);
//            this.loader = new SetSpriteLoader("/ingameStats/pipo-popupemotes021.png");
//            this.frames = loader.getAllSprites();
//
//            this.animator = new ThreeFrameAnimator(frames, 50);
        } catch(IOException e)
        {
            e.printStackTrace();
        }

    }

    /**
     * Updates the farmer for one game tick.
     * Wakes up if the player is within range, then moves one tile toward them.
     *
     * @param engine the active game engine
     */
    public void update(GameEngine engine) {
        //animator.update();
        sleepAnimation.update();
        wakeUpAnimation.update();

        int playerX = engine.getPlayerX();
        int playerY = engine.getPlayerY();

        int distance = Math.abs(x - playerX) + Math.abs(y - playerY);

        if (!awake) {
            if (distance <= detectionRadius) {
                dangerSign = true;
                awake = true;
            } else {
                dangerSign = false;
                return;
            }
        }

        moveCounter++;
        if (moveCounter >= moveInterval) {
            moveTowardPlayer(engine, playerX, playerY);
            moveCounter = 0;
            // cycle sprite on every move
            if (spriteNum == 1) spriteNum = 2;
            else if (spriteNum == 2) spriteNum = 3;
            else spriteNum = 1;
        }

        // Interpolation
        int targetX = x * GamePanel.TILE_SIZE;
        int targetY = y * GamePanel.TILE_SIZE;

        if (renderX == -1 || renderY == -1 || Math.abs(renderX - targetX) > GamePanel.TILE_SIZE) {
            renderX = targetX;
            renderY = targetY;
        }

        int speed = 2; // TILE_SIZE (32) / 17 interval ~= 2 pixels per tick

        if (renderX < targetX) renderX = Math.min(renderX + speed, targetX);
        else if (renderX > targetX) renderX = Math.max(renderX - speed, targetX);

        if (renderY < targetY) renderY = Math.min(renderY + speed, targetY);
        else if (renderY > targetY) renderY = Math.max(renderY - speed, targetY);
    }

    /**
     * Applies the farmer's hazard effect to the game state.
     * Change this later to match however your team stores health/score/loss.
     *
     * @param player current game state
     */
    public void triggerHazard(PlayerPig player) {
        if (player == null) {
            return;
        }

        while (!player.isDead()) {
            player.loseHeart();
        }
    }

    /**
     * Chooses the valid adjacent move that reduces
     * Manhattan distance to the player the most.
     *
     * @param engine the game engine
     * @param playerX player's x tile
     * @param playerY player's y tile
     */
    private void moveTowardPlayer(GameEngine engine, int playerX, int playerY) {
        Direction bestDirection = Direction.IDLE;
        int bestDistance = Integer.MAX_VALUE;

        Direction[] directions = {
                Direction.UP,
                Direction.LEFT,
                Direction.DOWN,
                Direction.RIGHT
        };

        for (Direction direction : directions) {
            int nextX = x;
            int nextY = y;

            switch (direction) {
                case UP:
                    nextY--;
                    break;
                case DOWN:
                    nextY++;
                    break;
                case LEFT:
                    nextX--;
                    break;
                case RIGHT:
                    nextX++;
                    break;
                default:
                    break;
            }

            if (!engine.isWalkable(nextX, nextY)) {
                continue;
            }

            int newDistance = Math.abs(nextX - playerX) + Math.abs(nextY - playerY);

            if (newDistance < bestDistance) {
                bestDistance = newDistance;
                bestDirection = direction;
            }
        }

        switch (bestDirection) {
            case UP:
                y--;
                facing = Direction.UP;
                break;
            case DOWN:
                y++;
                facing = Direction.DOWN;
                break;
            case LEFT:
                x--;
                facing = Direction.LEFT;
                break;
            case RIGHT:
                x++;
                facing = Direction.RIGHT;
                break;
            default:
                break;
        }
    }

    private void getFarmerImage()
    {
        try
        {
            up1 = ImageIO.read(getClass().getResourceAsStream("/farmer/entity_farmer_walk_up_01.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/farmer/entity_farmer_walk_up_02.png"));
            up3 = ImageIO.read(getClass().getResourceAsStream("/farmer/entity_farmer_walk_up_03.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/farmer/entity_farmer_walk_down_01.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/farmer/entity_farmer_walk_down_02.png"));
            down3 = ImageIO.read(getClass().getResourceAsStream("/farmer/entity_farmer_walk_down_03.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/farmer/entity_farmer_walk_left_01.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/farmer/entity_farmer_walk_left_02.png"));
            left3 = ImageIO.read(getClass().getResourceAsStream("/farmer/entity_farmer_walk_left_03.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/farmer/entity_farmer_walk_right_01.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/farmer/entity_farmer_walk_right_02.png"));
            right3 = ImageIO.read(getClass().getResourceAsStream("/farmer/entity_farmer_walk_right_03.png"));
            reaction = ImageIO.read(getClass().getResourceAsStream("/ingameStats/farmer_reaction.png"));




        } catch(IOException e)
        {
            e.printStackTrace();
        }
    }

//    private void updateSprite() {
//        spriteCounter++;
//
//        if (spriteCounter > 12) {
//            if (spriteNum == 1) spriteNum = 2;
//            else if (spriteNum == 2) spriteNum = 3;
//            else spriteNum = 1;
//            spriteCounter = 0;
//        }
//    }

    public void draw(Graphics2D g2, GamePanel gp, Position playerLocation) {
        BufferedImage image = null;

        switch (facing) {
            case UP:
                if (spriteNum == 1) image = up1;
                else if (spriteNum == 2) image = up2;
                else image = up3;
                break;
            case DOWN:
                if (spriteNum == 1) image = down1;
                else if (spriteNum == 2) image = down2;
                else image = down3;
                break;
            case LEFT:
                if (spriteNum == 1) image = left1;
                else if (spriteNum == 2) image = left2;
                else image = left3;
                break;
            case RIGHT:
                if (spriteNum == 1) image = right1;
                else if (spriteNum == 2) image = right2;
                else image = right3;
                break;
            default:
                image = down1;
                break;
        }


        int drawX = (renderX == -1) ? x * gp.TILE_SIZE : renderX;
        int drawY = (renderY == -1) ? y * gp.TILE_SIZE : renderY;

        g2.drawImage(image, drawX, drawY - gp.TILE_SIZE, gp.TILE_SIZE, gp.TILE_SIZE * 2, null);

        if(reaction != null){
            if(dangerSign) {
                g2.drawImage(wakeUpAnimation.getCurrentFrame(), drawX + 15, drawY - gp.TILE_SIZE - 4, gp.TILE_SIZE-7, gp.TILE_SIZE-7, null);
            }else{
                g2.drawImage(sleepAnimation.getCurrentFrame(), drawX + 15, drawY - gp.TILE_SIZE - 4, gp.TILE_SIZE-7, gp.TILE_SIZE-7, null);
            }
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isAwake() {
        return awake;
    }

    public Direction getFacing() {
        return facing;
    }
}

