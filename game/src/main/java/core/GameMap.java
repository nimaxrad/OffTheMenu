package core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import tile.Tiles;
import app.Spawns;


/**
 * @author nathanomana
 * @author Joshua Tan
 *
 *         Holds the tile data for a game level and provides
 *         read-only queries for tile types and map dimensions.
 *         Added in a separate class to increase modularity for future maps.
 */
public class GameMap {

    // Differentiate the maps by tile types (walls, tiles, spawns)
    private final int[][] mapWalls;
    private final int[][] mapTiles;
    private final int[][] mapSpawns;

    // Priv constructor for factory method that can create specialized map types
    /**
     * Private constructor for the factory method, allowing initialization of
     * specialized map layers.
     *
     * @param mapWalls  2D array representing collision walls
     * @param mapTiles  2D array representing ground tiles
     * @param mapSpawns 2D array representing entity spawn points
     */
    private GameMap(int[][] mapWalls, int[][] mapTiles, int[][] mapSpawns) {
        this.mapWalls = mapWalls;
        this.mapTiles = mapTiles;
        this.mapSpawns = mapSpawns;
    }

    // Enum getters

    /**
     * Gets the ground tile at the specified coordinates.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return the corresponding Tiles enum representing the ground
     */
    public Tiles getTile(int x, int y) {
        return Tiles.fromInt(mapTiles[y][x]);
    }

    /**
     * Gets the wall tile at the specified coordinates.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return the corresponding Tiles enum representing the wall
     */
    public Tiles getWall(int x, int y) {
        return Tiles.fromInt(mapWalls[y][x]);
    }

    /**
     * Gets the spawn point data at the specified coordinates.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return the corresponding Spawns enum representing the spawn type
     */
    public Spawns getSpawn(int x, int y) {
        return Spawns.fromInt(mapSpawns[y][x]);
    }

    /**
     * Checks if a specified tile is within the map boundaries and is not a wall.
     *
     * @param tileX the x-coordinate to check
     * @param tileY the y-coordinate to check
     * @return true if the tile is within bounds and walkable, false otherwise
     */
    public boolean isWalkable(int tileX, int tileY) {
        return tileX >= 0 && tileX < getWidth()
                && tileY >= 0 && tileY < getHeight()
                && mapWalls[tileY][tileX] == 0;
    }

    // Dimension getters (updated for factory method)

    /**
     * Gets the width of the game map.
     *
     * @return the map width in tiles
     */
    public int getWidth() {
        return mapWalls[0].length;
    }

    /**
     * Gets the height of the game map.
     *
     * @return the map height in tiles
     */
    public int getHeight() {
        return mapWalls.length;
    }

    /**
     * Default factory method to create the standard level map (100x60).
     * Loads the map data for walls, tiles, and spawns from predefined text files.
     *
     * @return a new GameMap initialized with default layer data
     */
    public static GameMap createDefaultMap() {
        return createMap(MapConfig.defaultMap());
    }

    /**
     * Creates a game map from the provided map configuration.
     *
     * @param config map layer paths and dimensions
     * @return a new GameMap initialized with configured layer data
     */
    public static GameMap createMap(MapConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("config cannot be null");
        }

        int[][] mapWalls = loadLayer(config.getWallsPath(), config.getHeight(), config.getWidth());
        int[][] mapTiles = loadLayer(config.getTilesPath(), config.getHeight(), config.getWidth());
        int[][] mapSpawns = loadLayer(config.getSpawnsPath(), config.getHeight(), config.getWidth());

        return new GameMap(mapWalls, mapTiles, mapSpawns);
    }

    /**
     * Loads a layer of a map from a text file.
     *
     * @param path   to the text file holding the map
     * @param height of the map
     * @param width  of the map
     * @return
     */
    private static int[][] loadLayer(String path, int height, int width) {
        int[][] map = new int[height][width];

        try (InputStream is = GameMap.class.getResourceAsStream(path)) {
            if (is == null) {
                throw new IllegalArgumentException("Missing resource");
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                for (int y = 0; y < height; y++) {
                    String line = reader.readLine();
                    if (line == null) {
                        throw new IllegalArgumentException("Bad row count");
                    }

                    String[] parts = line.trim().split("\\s+");
                    if (parts.length != width) {
                        throw new IllegalArgumentException("Bad column count");
                    }

                    for (int x = 0; x < width; x++) {
                        map[y][x] = Integer.parseInt(parts[x]);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load map layer: " + path, e);
        }

        return map;
    }

}
