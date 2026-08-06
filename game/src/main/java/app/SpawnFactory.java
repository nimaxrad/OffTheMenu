package app;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import core.GameEngine;
import entity.Farmer;
import entity.PlayerPig;
import object.OBJ_Pig;
import object.OBJ_Trap;

/**
 * Creates and places spawnable level entities from spawn layer data.
 *
 * @author Joshua Tan
 */
public class SpawnFactory {
    private static final int FARMER_DETECTION_RADIUS = 7;

    private final PlayerPig player;
    private final GameEngine engine;
    private final List<Farmer> farmers;
    private final List<OBJ_Trap> traps;
    private final List<OBJ_Pig> pigNPCs;
    private final Map<Spawns, SpawnAction> spawnActions;

    /**
     * Creates a factory that can populate the level entity lists.
     *
     * @param player  player entity to place when a player spawn is found
     * @param engine  game engine that stores the active player position
     * @param farmers farmer list owned by the level
     * @param traps   trap list owned by the level
     * @param pigNPCs pig NPC list owned by the level
     */
    public SpawnFactory(PlayerPig player, GameEngine engine, List<Farmer> farmers,
                        List<OBJ_Trap> traps, List<OBJ_Pig> pigNPCs) {
        if (player == null) {
            throw new IllegalArgumentException("player cannot be null");
        }
        if (engine == null) {
            throw new IllegalArgumentException("engine cannot be null");
        }
        if (farmers == null || traps == null || pigNPCs == null) {
            throw new IllegalArgumentException("spawn target lists cannot be null");
        }

        this.player = player;
        this.engine = engine;
        this.farmers = farmers;
        this.traps = traps;
        this.pigNPCs = pigNPCs;
        this.spawnActions = new EnumMap<>(Spawns.class);

        registerSpawnActions();
    }

    /**
     * Places one spawn at the given tile position.
     *
     * @param spawnType type of spawn to place
     * @param x         tile x-coordinate
     * @param y         tile y-coordinate
     */
    public void spawn(Spawns spawnType, int x, int y) {
        SpawnAction action = spawnActions.get(spawnType);

        if (action == null) {
            throw new IllegalStateException("Unhandled spawn type at (" + x + ", " + y + "): " + spawnType);
        }

        action.spawn(x, y);
    }

    private void registerSpawnActions() {
        spawnActions.put(Spawns.NOTHING, (x, y) -> {
        });
        spawnActions.put(Spawns.PLAYER, this::spawnPlayer);
        spawnActions.put(Spawns.PIGNPC, (x, y) -> pigNPCs.add(new OBJ_Pig(x, y)));
        spawnActions.put(Spawns.TRAP, (x, y) -> traps.add(new OBJ_Trap(x, y)));
        spawnActions.put(Spawns.FARMER, (x, y) -> farmers.add(new Farmer(x, y, FARMER_DETECTION_RADIUS)));
    }

    private void spawnPlayer(int x, int y) {
        player.setPosition(x, y);
        engine.setPlayerPosition(x, y);
    }

    private interface SpawnAction {
        void spawn(int x, int y);
    }
}
