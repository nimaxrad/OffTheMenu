package core;

/**
 * Stores the resource paths and dimensions needed to load a map.
 *
 * @author Joshua Tan
 */
public class MapConfig {
    // Base map
    private static final String DEFAULT_WALLS_PATH = "/maps/mapWalls.txt";
    private static final String DEFAULT_TILES_PATH = "/maps/mapTiles.txt";
    private static final String DEFAULT_SPAWNS_PATH = "/maps/mapSpawns.txt";

//    // Winter map
//    private static final String DEFAULT_WALLS_PATH = "/maps/mapWallsWinter.txt";
//    private static final String DEFAULT_TILES_PATH = "/maps/mapTilesWinter.txt";
//    private static final String DEFAULT_SPAWNS_PATH = "/maps/mapSpawnsWinter.txt";

    // Demo map
//     private static final String DEFAULT_WALLS_PATH = "/maps/demoMapWalls.txt";
//     private static final String DEFAULT_TILES_PATH = "/maps/demoMapTiles.txt";
//     private static final String DEFAULT_SPAWNS_PATH = "/maps/demoMapSpawns.txt";

    private static final int DEFAULT_HEIGHT = 60;
    private static final int DEFAULT_WIDTH = 100;

    private final String wallsPath;
    private final String tilesPath;
    private final String spawnsPath;
    private final int height;
    private final int width;

    /**
     * Creates a map configuration.
     *
     * @param wallsPath  resource path for the wall layer
     * @param tilesPath  resource path for the tile layer
     * @param spawnsPath resource path for the spawn layer
     * @param height     map height in tiles
     * @param width      map width in tiles
     */
    public MapConfig(String wallsPath, String tilesPath, String spawnsPath, int height, int width) {
        if (wallsPath == null || tilesPath == null || spawnsPath == null) {
            throw new IllegalArgumentException("map layer paths cannot be null");
        }
        if (height <= 0 || width <= 0) {
            throw new IllegalArgumentException("map dimensions must be positive");
        }

        this.wallsPath = wallsPath;
        this.tilesPath = tilesPath;
        this.spawnsPath = spawnsPath;
        this.height = height;
        this.width = width;
    }

    /**
     * Returns the standard map configuration used by the game.
     *
     * @return default map configuration
     */
    public static MapConfig defaultMap() {
        return new MapConfig(DEFAULT_WALLS_PATH, DEFAULT_TILES_PATH, DEFAULT_SPAWNS_PATH,
                DEFAULT_HEIGHT, DEFAULT_WIDTH);
    }

    public String getWallsPath() {
        return wallsPath;
    }

    public String getTilesPath() {
        return tilesPath;
    }

    public String getSpawnsPath() {
        return spawnsPath;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
