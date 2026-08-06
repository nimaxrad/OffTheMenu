package core;
import app.Position;
import app.Direction;
import app.Mode;

/**
 * @author nathanomana
 *         The game engine connects the game loop logic and systems together.
 *         Acts as the central coordinator which receives input, updates
 *         entities,
 *         and exposes read-only state for the renderer.
 */

import app.Direction;
import app.Position;
import app.Mode;

public class GameEngine {
    private static final int EXIT_MIN_X = 92;
    private static final int EXIT_MAX_X = 94;
    private static final int EXIT_MIN_Y = 7;
    private static final int EXIT_MAX_Y = 8;

    // Configuration
    private final int ticksPerSecond;

    // Game Systems
    private final GameState state;
    private final GameMap map;

    // Player
    private final Position playerPosition;
    private Direction queuedMove;
    private int elapsedTicks;
    private boolean exitUnlocked;

    // Constructor

    /**
     * Initializes a new GameEngine instance.
     * Sets up the initial game state, loads the default map,
     * and positions the player at their starting coordinates.
     */
    public GameEngine() {
        this(MapConfig.defaultMap());
    }

    /**
     * Initializes a new GameEngine instance with a configured map.
     *
     * @param mapConfig map configuration to load
     */
    public GameEngine(MapConfig mapConfig) {
        this.ticksPerSecond = 60;
        this.state = new GameState();
        this.map = GameMap.createMap(mapConfig);
        this.queuedMove = Direction.IDLE;
        this.playerPosition = new Position(50, 30);
        this.elapsedTicks = 0;
        this.exitUnlocked = false;
    }

    // Game Loop

    /**
     * Advances the game logic by a single tick.
     * Updates elapsed time and processes any pending player movements,
     * provided the game state is currently set to PLAYING.
     */
    public void tick() {
        if (state.getMode() != Mode.PLAYING) {
            return;
        }

        elapsedTicks++;

        if (queuedMove != Direction.IDLE) {
            tryMove(queuedMove);
            queuedMove = Direction.IDLE;
        }
    }

    /**
     * Queues a movement direction to be processed on the next engine tick.
     * Movement is only accepted if the current game state is PLAYING.
     *
     * @param d the direction the player has requested to move
     */
    public void handleInput(Direction d) {
        if (state.getMode() == Mode.PLAYING) {
            queuedMove = d;
        }
    }

    /**
     * Attempts to move the player one tile in the specified direction.
     * Validates the new position against the map's collision data before moving.
     *
     * @param direction the direction to attempt to move the player
     */
    private void tryMove(Direction direction) {
        int newX = playerPosition.getX();
        int newY = playerPosition.getY();

        switch (direction) {
            case UP:
                newY--;
                break;
            case DOWN:
                newY++;
                break;
            case LEFT:
                newX--;
                break;
            case RIGHT:
                newX++;
                break;
            default:
                return;
        }

        if (map.isWalkable(newX, newY) || canEnterExit(newX, newY)) {
            playerPosition.setX(newX);
            playerPosition.setY(newY);
        }
    }

    // Map Queries (from GameMap)

    /**
     * Checks if a specific map tile acts as a collision wall.
     *
     * @param x the x-coordinate of the tile
     * @param y the y-coordinate of the tile
     * @return true if the tile is a wall, false otherwise
     */
    public boolean isWall(int x, int y) {
        return !map.isWalkable(x, y);
    }

    /**
     * Gets the total width of the game map in tiles.
     *
     * @return the map width
     */
    public int getMapWidth() {
        return map.getWidth();
    }

    /**
     * Gets the total height of the game map in tiles.
     *
     * @return the map height
     */
    public int getMapHeight() {
        return map.getHeight();
    }

    /**
     * Gets the active GameMap instance.
     *
     * @return the current GameMap
     */
    public GameMap getMap() {
        return map;
    }

    // Getters

    /**
     * Returns a safe copy of the player's current position to prevent external
     * mutation.
     *
     * @return a new Position object representing the player's coordinates
     */
    public Position getPlayerPosition() {
        return new Position(playerPosition);
    }

    /**
     * Gets the current state of the game, including score, health, and current
     * mode.
     *
     * @return the GameState instance
     */
    public GameState getState() {
        return state;
    }

    /**
     * Gets the target number of updates per second for the engine loop.
     *
     * @return the ticks per second
     */
    public int getTicksPerSecond() {
        return ticksPerSecond;
    }

    /**
     * Gets the total number of update ticks elapsed since the engine started.
     *
     * @return the number of elapsed ticks
     */
    public int getElapsedTicks() {
        return elapsedTicks;
    }

    /**
     * Sets the total number of elapsed ticks.
     *
     * @param elapsedTicks the new value for elapsed ticks
     */
    public void setElapsedTicks(int elapsedTicks) {
        this.elapsedTicks = elapsedTicks;
    }

    /**
     * Gets the movement direction currently queued for the next tick.
     *
     * @return the queued Direction
     */
    public Direction getQueuedMove() {
        return queuedMove;
    }

    /**
     * Gets the player's current x-coordinate.
     *
     * @return the player's x-coordinate
     */
    public int getPlayerX() {
        return playerPosition.getX();
    }

    /**
     * Gets the player's current y-coordinate.
     *
     * @return the player's y-coordinate
     */
    public int getPlayerY() {
        return playerPosition.getY();
    }

    /**
     * Checks if the specified coordinates are walkable on the current map.
     *
     * @param x the x-coordinate to check
     * @param y the y-coordinate to check
     * @return true if the tile is walkable, false otherwise
     */
    public boolean isWalkable(int x, int y) {
        return map.isWalkable(x, y);
    }

    /**
     * Enables or disables entering the barn exit tiles.
     *
     * @param exitUnlocked true when the rescue goal has been met
     */
    public void setExitUnlocked(boolean exitUnlocked) {
        this.exitUnlocked = exitUnlocked;
    }

    /**
     * Checks if a tile is part of the barn exit.
     *
     * @param x the x-coordinate to check
     * @param y the y-coordinate to check
     * @return true if the tile is inside the barn exit rectangle
     */
    public boolean isExitTile(int x, int y) {
        return x >= EXIT_MIN_X && x <= EXIT_MAX_X
                && y >= EXIT_MIN_Y && y <= EXIT_MAX_Y;
    }

    private boolean canEnterExit(int x, int y) {
        return exitUnlocked && isExitTile(x, y);
    }

    /**
     * Instantly teleports or sets the player to the specified coordinates.
     *
     * @param x the new x-coordinate
     * @param y the new y-coordinate
     */
    public void setPlayerPosition(int x, int y) {
        playerPosition.setX(x);
        playerPosition.setY(y);
    }
}
