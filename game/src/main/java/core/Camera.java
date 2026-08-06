package core;

import app.Position;

/**
 * Manages the camera position and offset logic, ensuring the camera
 * focuses on the player while staying within the map boundaries.
 *
 * @author nathanomana
 */
public class Camera {
    private Position offset;

    public Camera() {
        this.offset = new Position(0, 0);
    }

    /**
     * Updates the camera offset based on the player pixel position and map dimensions.
     * 
     * @param targetPixelX   The target X pixel position
     * @param targetPixelY   The target Y pixel position
     * @param mapWidthTiles  The total width of the map in tiles
     * @param mapHeightTiles The total height of the map in tiles
     */
    public void update(int targetPixelX, int targetPixelY, int mapWidthTiles, int mapHeightTiles) {
        int cameraX = targetPixelX - (GamePanel.BASE_WIDTH / 2) + (GamePanel.TILE_SIZE / 2);
        int cameraY = targetPixelY - (GamePanel.BASE_HEIGHT / 2) + (GamePanel.TILE_SIZE / 2);

        int worldWidth = mapWidthTiles * GamePanel.TILE_SIZE;
        int worldHeight = mapHeightTiles * GamePanel.TILE_SIZE;

        cameraX = Math.max(0, Math.min(cameraX, worldWidth - GamePanel.BASE_WIDTH));
        cameraY = Math.max(0, Math.min(cameraY, worldHeight - GamePanel.BASE_HEIGHT));

        this.offset.setX(cameraX);
        this.offset.setY(cameraY);
    }

    /**
     * @return the calculated pixel offset for rendering
     */
    public Position getOffset() {
        return offset;
    }
}
