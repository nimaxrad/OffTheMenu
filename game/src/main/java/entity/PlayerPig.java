package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import app.Direction;
import audio.SoundEffects;

/**
 * Class for the Main Character.
 * Responsibilities:
 * - Queue and process movement.
 * - Track gameplay statistics such as score (points).
 * - Track number of pigs rescued.
 * - Apply rewards and penalties from interacting with tiles such as food or traps.
 *
 */
public class PlayerPig {
    private int playerX;
    private int playerY;

    private final int tileSize; // decoupled from GamePanel

    SoundEffects sound;

    /** Next movement direction requested by the player */
    private Direction queuedMove;

    /** Remaining lives of the player */
    private int hearts;

    /** Current score in the level or run */
    public int score;

    /** Highest score achieved across runs */
    public int highScore;

    /** Number of pigs rescued so far */
    private int pigsRescued;

    /** Number of pigs required to win / finish the level */
    private int pigsGoal;

    BufferedImage up1, up2, up3;
    BufferedImage down1, down2, down3;
    BufferedImage left1, left2, left3;
    BufferedImage right1, right2, right3;

    String direction = "down";
    int spriteCounter = 0;
    int spriteNum = 1;

    public int screenX = 0;
    public int screenY = 0;
    public int renderX = -1;
    public int renderY = -1;

    /**
     * Creates a PlayerPig with initial gameplay values.
     *
     * @param hearts   initial number of hearts
     * @param pigsGoal number of pigs required to complete level
     * @param tileSize size of a tile (was GamePanel.TILE_SIZE)
     */
    public PlayerPig(int hearts, int pigsGoal, int tileSize) {
        playerX = 3;
        playerY = 3;
        this.hearts = hearts;
        this.pigsGoal = pigsGoal;
        this.tileSize = tileSize;

        this.score = 0;
        this.highScore = 0;
        this.pigsRescued = 0;

        getPlayerImage();
    }

    private void getPlayerImage() {
        try {
            up1 = ImageIO.read(getClass().getResourceAsStream("/pigMC/entity_pig_mc_walk_up_01.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/pigMC/entity_pig_mc_walk_up_02.png"));
            up3 = ImageIO.read(getClass().getResourceAsStream("/pigMC/entity_pig_mc_walk_up_03.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/pigMC/entity_pig_mc_walk_down_01.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/pigMC/entity_pig_mc_walk_down_02.png"));
            down3 = ImageIO.read(getClass().getResourceAsStream("/pigMC/entity_pig_mc_walk_down_03.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/pigMC/entity_pig_mc_walk_left_01.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/pigMC/entity_pig_mc_walk_left_02.png"));
            left3 = ImageIO.read(getClass().getResourceAsStream("/pigMC/entity_pig_mc_walk_left_03.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/pigMC/entity_pig_mc_walk_right_01.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/pigMC/entity_pig_mc_walk_right_02.png"));
            right3 = ImageIO.read(getClass().getResourceAsStream("/pigMC/entity_pig_mc_walk_right_03.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void queueMove(Direction d) {
        this.queuedMove = d;
    }

    public void clearQueuedMove() {
        this.queuedMove = null;
        this.spriteCounter = 0;
    }

    public Direction getQueuedMove() {
        return queuedMove;
    }

    public void addScore(int amount) {
        if (amount <= 0)
            return;

        score += amount;

        if (score > highScore) {
            highScore = score;
        }
    }

    public void deductScore(int amount) {
        if (amount <= 0)
            return;

        score -= amount;

        if (score < 0) {
            score = 0;
        }
    }

    public void loseHeart() {
        if (hearts > 0) {
            hearts--;
        }
    }

    private void updatePlayerSprite() {
        spriteCounter++;

        if (spriteCounter > 4) {
            spriteNum = spriteNum == 1 ? 2 : spriteNum == 2 ? 3 : 1;
            spriteCounter = 0;
        }
    }

    public void advanceSpriteFrame() {
        spriteNum = spriteNum == 1 ? 2 : spriteNum == 2 ? 3 : 1;
    }

    public void setDirection(Direction direction) {
        if (direction == null) return;

        switch (direction) {
            case UP -> this.direction = "up";
            case DOWN -> this.direction = "down";
            case LEFT -> this.direction = "left";
            case RIGHT -> this.direction = "right";
        }
    }

    public BufferedImage getCurrentSprite() {
        return switch (direction) {
            case "up" -> spriteNum == 1 ? up1 : spriteNum == 2 ? up2 : up3;
            case "left" -> spriteNum == 1 ? left1 : spriteNum == 2 ? left2 : left3;
            case "right" -> spriteNum == 1 ? right1 : spriteNum == 2 ? right2 : right3;
            default -> spriteNum == 1 ? down1 : spriteNum == 2 ? down2 : down3;
        };
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = getCurrentSprite();
        g2.drawImage(image, screenX, screenY, tileSize, tileSize, null);
    }

    public void gainHeart() {
        hearts++;
    }

    public void rescuePig() {
        pigsRescued++;
    }

    public boolean hasMetRescueGoal() {
        return pigsRescued >= pigsGoal;
    }

    public boolean isDead() {
        return hearts <= 0;
    }

    public int getHearts() {
        return hearts;
    }

    public int getScore() {
        return score;
    }

    public int getHighScore() {
        return highScore;
    }

    public int getPigsRescued() {
        return pigsRescued;
    }

    public int getPigsGoal() {
        return pigsGoal;
    }

    public int getX() {
        return playerX;
    }

    public int getY() {
        return playerY;
    }

    public void setPosition(int newX, int newY) {
        if (renderX == -1 || renderY == -1 || Math.abs(this.playerX - newX) > 1 || Math.abs(this.playerY - newY) > 1) {
            this.renderX = newX * tileSize;
            this.renderY = newY * tileSize;
        }
        this.playerX = newX;
        this.playerY = newY;
    }

    public void resetLevelState(int hearts) {
        this.playerX = 0;
        this.playerY = 0;
        this.hearts = hearts;
        this.score = 0;
        this.pigsRescued = 0;
        this.queuedMove = null;
    }

    public void update() {
        int targetX = playerX * tileSize;
        int targetY = playerY * tileSize;
        int speed = tileSize / 8; // decoupled from GamePanel

        if (renderX < targetX) renderX = Math.min(renderX + speed, targetX);
        else if (renderX > targetX) renderX = Math.max(renderX - speed, targetX);

        if (renderY < targetY) renderY = Math.min(renderY + speed, targetY);
        else if (renderY > targetY) renderY = Math.max(renderY - speed, targetY);

        if (queuedMove != null) {
            switch (queuedMove) {
                case UP -> direction = "up";
                case DOWN -> direction = "down";
                case LEFT -> direction = "left";
                case RIGHT -> direction = "right";
            }
            updatePlayerSprite();
        }
    }

    public void setDefaultValues() {
        resetLevelState(3);
    }

    public int health = 3;

}
