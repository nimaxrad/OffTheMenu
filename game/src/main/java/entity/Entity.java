package entity;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;
import java.util.Map;

import app.Direction;
import app.Position;

import java.util.HashMap;

/**
 * Represents the base type for all objects that exist on the map and have a position.
 *
 * Responsibilities:
 * - Store position and collision data
 * - Manage sprite animations
 * - Provide directional sprite access
 *
 * Refactored to remove Data Clumps by grouping sprite images
 * into a structured map instead of multiple individual fields.
 *
 * @author Joshua Tan
 * @author nimarad
 * @version 1.1
 */
public abstract class Entity {

    /** Position of the entity in the world */
    protected Position pos;

    /**
     * Stores sprites for each direction.
     * Key = direction, Value = array of animation frames
     */
    protected Map<Direction, BufferedImage[]> sprites;

    /** Current direction the entity is facing */
    protected Direction direction;

    /** Counter used for sprite animation timing */
    protected int spriteCounter = 0;

    /** Current sprite frame index (1-based for compatibility) */
    protected int spriteNum = 1;

    /** Collision hitbox */
    public Rectangle hitbox;

    /** Default hitbox X offset */
    public int hitboxDefaultX;

    /** Default hitbox Y offset */
    public int hitboxDefaultY;

    /** Whether collision is currently active */
    public boolean collisionOn;

    /**
     * Constructs an Entity with a starting position.
     *
     * @param startPos initial position (must not be null)
     */
    protected Entity(Position startPos) {
        if (startPos == null) {
            throw new IllegalArgumentException("startPos cannot be null");
        }
        this.pos = startPos;
        this.sprites = new HashMap<>();
    }

    /**
     * Gets the current position of the entity.
     *
     * @return position object
     */
    public Position getPos() {
        return pos;
    }

    /**
     * Sets a new position for the entity.
     *
     * @param newPos new position (must not be null)
     */
    protected void setPos(Position newPos) {
        if (newPos == null) {
            throw new IllegalArgumentException("newPos cannot be null");
        }
        this.pos = newPos;
    }

    /**
     * Sets sprite frames for a specific direction.
     *
     * @param direction the direction (UP, DOWN, LEFT, RIGHT)
     * @param frames array of images representing animation frames
     */
    protected void setSprites(Direction direction, BufferedImage[] frames) {
        sprites.put(direction, frames);
    }

    /**
     * Gets the current sprite image based on direction and animation frame.
     *
     * @return current sprite image, or null if not set
     */
    protected BufferedImage getCurrentSprite() {
        BufferedImage[] frames = sprites.get(direction);

        if (frames == null || frames.length == 0) {
            return null;
        }

        return frames[spriteNum - 1];
    }
}