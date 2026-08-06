package core;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import app.SetAnimationLoader;
import entity.Farmer;
import input.PlayInput;
import object.OBJ_Pig;
import object.OBJ_Trap;
import object.OBJ_Wheat;
import tile.Tiles;
import app.Position;
import app.Direction;

/**
 * @author nathanomana
 *         Handles the rendering operations for the GamePanel.
 *         Decoupled from the main GamePanel class to improve cohesion
 *         and separate the concerns of game loop management from drawing.
 */
public class GameRenderer {
    private final GamePanel panel;

    /**
     * Initializes the GameRenderer with a reference to the main GamePanel.
     *
     * @param panel the GamePanel containing game components and state
     */
    public GameRenderer(GamePanel panel) {

        this.panel = panel;
    }

    /**
     * Renders all active entities within the current level.
     * Includes NPCs, traps, bonus rewards, and enemies.
     *
     * @param g2 the Graphics2D context to draw onto
     */
    public void drawEntities(Graphics2D g2) {
        if (panel.level == null)
            return;

        Position playerPos = panel.getEngine().getPlayerPosition();

        for (OBJ_Pig pig : panel.level.getPigNPCs()) {
            pig.draw(g2, panel);
        }
        for (OBJ_Trap trap : panel.level.getTraps()) {
            trap.draw(g2, panel);
        }
        for (OBJ_Wheat wheat : panel.level.getBonusRewards()) {
            wheat.draw(g2, panel);
        }
        for (Farmer farmer : panel.level.getFarmers()) {
            farmer.draw(g2, panel, playerPos);
        }

    }

    /**
     * Draws the ground and wall tiles that make up the game world map.
     * Optimizes rendering by only drawing tiles visible within the camera's
     * viewport.
     *
     * @param g2  the Graphics2D context to draw onto
     * @param cam the current camera offset position
     */
    public void drawMap(Graphics2D g2, Position cam) {
        GameMap map = panel.getEngine().getMap();

        int renderPadding = 2;
        int startCol = Math.max(0, (cam.getX() / GamePanel.TILE_SIZE) - renderPadding);
        int startRow = Math.max(0, (cam.getY() / GamePanel.TILE_SIZE) - renderPadding);
        int endCol = Math.min(startCol + GamePanel.SCREEN_COLS + 1 + (renderPadding * 2),
                panel.getEngine().getMapWidth());
        int endRow = Math.min(startRow + GamePanel.SCREEN_ROWS + 1 + (renderPadding * 2),
                panel.getEngine().getMapHeight());

        for (int row = startRow; row < endRow; row++) {
            for (int col = startCol; col < endCol; col++) {
                int x = col * GamePanel.TILE_SIZE;
                int y = row * GamePanel.TILE_SIZE;

                g2.drawImage(panel.getGrassTile(), x, y, GamePanel.TILE_SIZE, GamePanel.TILE_SIZE, panel);

                Tiles groundTile = map.getTile(col, row);
                if (groundTile != null && groundTile.image != null) {
                    drawMapTile(g2, groundTile, x, y);
                }

                Tiles wallTile = map.getWall(col, row);
                boolean hasVisualWallTile = groundTile != null && groundTile.hasCollision();
                if (wallTile != null && wallTile.getId() != 0 && !hasVisualWallTile) {
                    boolean coveredByLargeStructure = isCoveredByLargeStructure(map, col, row);
                    if (!coveredByLargeStructure) {
                        Image img = Tiles.FENCE_000.image;
                        if (img != null) {
                            g2.drawImage(img, x, y, GamePanel.TILE_SIZE, GamePanel.TILE_SIZE, panel);
                        }
                    }
                }
            }
        }
    }

    /**
     * Handles updating player sprite cycling and rendering the player character's
     * appropriate directional sprite.
     *
     * @param g2                 the Graphics2D context to draw onto
     * @param ticksSinceLastMove ticks passed since the player last triggered
     *                           movement
     */
    public void drawPlayer(Graphics2D g2, int ticksSinceLastMove) {
        int x = panel.player.renderX;
        int y = panel.player.renderY;

        PlayInput playInput = (PlayInput) panel.inputM.getPlayInput();
        if (playInput.up || playInput.down || playInput.left || playInput.right) {
            if (playInput.up)
                panel.player.setDirection(Direction.UP);
            else if (playInput.down)
                panel.player.setDirection(Direction.DOWN);
            else if (playInput.left)
                panel.player.setDirection(Direction.LEFT);
            else if (playInput.right)
                panel.player.setDirection(Direction.RIGHT);

            if (ticksSinceLastMove == 0 && panel.player.getQueuedMove() == null) {
                panel.player.advanceSpriteFrame();
            }
        }

        BufferedImage image = panel.player.getCurrentSprite();
        if (image != null) {
            g2.drawImage(image, x, y, GamePanel.TILE_SIZE, GamePanel.TILE_SIZE, null);
        } else {
            g2.setColor(Color.WHITE);
            g2.fillRect(x, y, GamePanel.TILE_SIZE, GamePanel.TILE_SIZE);
        }
    }

    /**
     * Draws an individual map tile. If the structure is larger than a standard 1x1
     * tile,
     * it correctly offsets the drawing position so it anchors from the
     * bottom-right.
     *
     * @param g2   the Graphics2D context
     * @param tile the Tile enum value representing the tile visual
     * @param x    the x-coordinate to draw at
     * @param y    the y-coordinate to draw at
     */
    private void drawMapTile(Graphics2D g2, Tiles tile, int x, int y) {
        BufferedImage tileImage = getTileImageToDraw(tile);

        if (isLargeStructureTile(tile)) {
            int drawX = x - (tileImage.getWidth() - GamePanel.TILE_SIZE);
            int drawY = y - (tileImage.getHeight() - GamePanel.TILE_SIZE);
            g2.drawImage(tileImage, drawX, drawY, panel);
            return;
        }

        g2.drawImage(tileImage, x, y, GamePanel.TILE_SIZE, GamePanel.TILE_SIZE, panel);
    }

    private BufferedImage getTileImageToDraw(Tiles tile) {
        if (tile == Tiles.BARNHOUSE_CLOSED
                && panel.player != null
                && panel.player.hasMetRescueGoal()
                && Tiles.BARNHOUSE_OPEN.image != null) {
            return Tiles.BARNHOUSE_OPEN.image;
        }

        return tile.image;
    }

    private boolean isCoveredByLargeStructure(GameMap map, int col, int row) {
        for (int anchorRow = row; anchorRow < Math.min(row + 8, map.getHeight()); anchorRow++) {
            for (int anchorCol = col; anchorCol < Math.min(col + 7, map.getWidth()); anchorCol++) {
                Tiles anchorTile = map.getTile(anchorCol, anchorRow);

                if (anchorTile == null || anchorTile.image == null || !isLargeStructureTile(anchorTile)) {
                    continue;
                }

                int widthInTiles = (int) Math.ceil((double) anchorTile.image.getWidth() / GamePanel.TILE_SIZE);
                int heightInTiles = (int) Math.ceil((double) anchorTile.image.getHeight() / GamePanel.TILE_SIZE);
                int leftCol = anchorCol - widthInTiles + 1;
                int topRow = anchorRow - heightInTiles + 1;

                if (col >= leftCol && col <= anchorCol && row >= topRow && row <= anchorRow) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Checks if a tile is considered a large multi-tile structure.
     *
     * @param tile the Tile to evaluate
     * @return true if the tile is a large structure (e.g., Barnhouse), false
     *         otherwise
     */
    private boolean isLargeStructureTile(Tiles tile) {
        return tile == Tiles.BARNHOUSE_CLOSED
                || tile == Tiles.BARNHOUSE_OPEN
                || tile == Tiles.HOUSE
                || tile == Tiles.HAY
                || tile == Tiles.PLANT_000
                || tile == Tiles.PLANT_001
                || tile == Tiles.PLANT_002
                || tile == Tiles.WINTERTREE_1
                || tile == Tiles.WINTERTREE_2;
    }
}
