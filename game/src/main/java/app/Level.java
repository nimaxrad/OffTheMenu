package app;

import java.util.ArrayList;
import java.util.List;

import app.StateManager.gameState;
import entity.Farmer;
import entity.PlayerPig;
import object.OBJ_Pig;
import object.OBJ_Trap;
import object.OBJ_Wheat;

import core.GameEngine;
import core.GamePanel;
import core.GameMap;
import core.GameState;
/**
 * Keeps track of the player, enemies, traps, and NPCs in one level.
 * Populates all spawn points at the start of the game and updates
 * all entities every tick.
 *
 * @author Joshua Tan
 * @version 1.0
 */
public class Level {

    private PlayerPig player;
    private List<Farmer> farmers;
    private List<OBJ_Trap> traps;
    private List<OBJ_Pig> pigNPCs;
    private List<OBJ_Wheat> bonusRewards;
    private BonusRewardSpawner bonusRewardSpawner;
    private CollisionHandler collisionHandler;

    private GamePanel panel;
    private GameEngine engine;

    /**
     * Constructs an empty level with no spawned entities yet.
     */
    public Level(PlayerPig player) {
        this.player = player;
        this.farmers = new ArrayList<>();
        this.traps = new ArrayList<>();
        this.pigNPCs = new ArrayList<>();
        this.bonusRewards = new ArrayList<>();
        this.bonusRewardSpawner = new BonusRewardSpawner(bonusRewards, farmers, traps, pigNPCs);
        this.collisionHandler = new CollisionHandler(farmers, traps, pigNPCs, bonusRewards);
    }

    /**
     * Spawns all entities by scanning the entire spawn layer of the map.
     *
     * Each tile in the spawn grid is converted from its integer id into a
     * {@link Spawns} enum, then delegated to a spawn factory.
     *
     * @param map   the map containing spawn data
     * @param panel the game panel, passed in if spawned objects need access to it
     */
    public void spawnEntities(GameMap map, GamePanel panel) {
        if (map == null) {
            throw new IllegalArgumentException("map cannot be null");
        }
        if (panel == null) {
            throw new IllegalArgumentException("panel cannot be null");
        }
        this.panel = panel;
        this.engine = panel.getEngine();

        // Optional: clear old entities before respawning
        farmers.clear();
        traps.clear();
        pigNPCs.clear();
        bonusRewards.clear();

        SpawnFactory spawnFactory = new SpawnFactory(player, this.engine, farmers, traps, pigNPCs);

        for (int y = 0; y < map.getHeight(); y++) {
            for (int x = 0; x < map.getWidth(); x++) {
                Spawns spawnType = map.getSpawn(x, y);
                spawnFactory.spawn(spawnType, x, y);
            }
        }
    }

    /**
     * Updates all entities in the level once per game tick.
     *
     * @param engine game engine reference used by moving/updating entities
     */
    public void update(GameEngine engine, GameMap map) {
        if (player != null) {
            player.setPosition(engine.getPlayerX(), engine.getPlayerY());
            player.update();
        }

        for (Farmer farmer : farmers) {
            farmer.update(engine);
        }

        bonusRewardSpawner.updateRewards();

        collisionHandler.checkCollisions(player);
        syncPlayerHud();
        engine.setExitUnlocked(player != null && player.hasMetRescueGoal());

        if (player != null && player.isDead()) {
            panel.stateM.setCurrentState(StateManager.gameState.LOSE);
            return;
        }

        if (player != null && player.hasMetRescueGoal() && isAtExit()) {
            panel.stateM.setCurrentState(StateManager.gameState.WIN);
            return;
        }

        bonusRewardSpawner.trySpawnBonusReward(map, player);
    }

    private void syncPlayerHud() {
        if (player != null) {
            panel.remainingPig = player.getPigsRescued();
        } else {
            panel.remainingPig = 0;
        }
    }

    private boolean isAtExit() {
        int playerX = player.getX();
        int playerY = player.getY();

        return engine.isExitTile(playerX, playerY);
    }

    /**
     * Returns the player in this level.
     *
     * @return the player, or null if none has been spawned
     */
    public PlayerPig getPlayerPig() {
        return player;
    }

    /**
     * Returns the farmer enemies in this level.
     *
     * @return the farmer list
     */
    public List<Farmer> getFarmers() {
        return farmers;
    }

    /**
     * Returns the pig NPCs in this level.
     *
     * @return the pig NPC list
     */
    public List<OBJ_Pig> getPigNPCs() {
        return pigNPCs;
    }

    /**
     * Returns the traps in this level.
     *
     * @return the trap list
     */
    public List<OBJ_Trap> getTraps() {
        return traps;
    }

    /**
     * Returns the bonus rewards in this level.
     *
     * @return the bonus rewards list
     */
    public List<OBJ_Wheat> getBonusRewards() {
        return bonusRewards;
    }

    BonusRewardSpawner getBonusRewardSpawner() {
        return bonusRewardSpawner;
    }
}
