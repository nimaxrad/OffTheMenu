package entity;
import app.Direction;
import app.Position;
import tile.TileMap;

/**
 * Represents an entity that can attempt to move one tile per tick.
 *
 * @author Joshua Tan
 * @version 1.0
 */
public abstract class MovableEntity extends Entity {
    protected MovableEntity(Position startPos) {
        super(startPos);
    }

    /**
     * Attempts to move exactly 1 tile in the given direction.
     *
     * @param d Direction to move towards
     * @param map TileMap the entity is on
     * @return true if valid and the position is changed; false otherwise
     */
    public boolean tryMove(Direction d, TileMap map) {
        if (d == null) {
            throw new IllegalArgumentException("Direction cannot be null");
        }
        if (map == null) {
            throw new IllegalArgumentException("TileMap cannot be null");
        }

        if (d == Direction.IDLE) {
            return false;
        }

        Position next = nextPos(d);

        if (!map.isInBounds(next) || map.isWall(next)) {
            return false;
        }

        setPos(next);
        return true;
    }

    /**
     * Computes the next position from the current pos + direction
     *
     * @param d Direction to move
     * @return the next Position
     */
    protected Position nextPos(Direction d) {
        int x = pos.getX();
        int y = pos.getY();

        switch (d) {
            case UP:    return new Position(x, y - 1);
            case DOWN:  return new Position(x, y + 1);
            case LEFT:  return new Position(x - 1, y);
            case RIGHT: return new Position(x + 1, y);
            default:    return new Position(x, y);
        }
    }
}