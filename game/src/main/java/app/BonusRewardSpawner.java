package app;

import java.util.List;
import java.util.Random;

import core.GameMap;
import entity.Farmer;
import entity.PlayerPig;
import object.OBJ_Pig;
import object.OBJ_Trap;
import object.OBJ_Wheat;

/**
 * Handles random bonus reward spawning and expiration for a level.
 *
 * @author Joshua Tan
 */
public class BonusRewardSpawner {
    private static final double BONUS_REWARD_SPAWN_CHANCE = 0.01;
    private static final int MAX_BONUS_REWARDS = 5;
    private static final int MAX_SPAWN_ATTEMPTS = 20;

    private final List<OBJ_Wheat> bonusRewards;
    private final List<Farmer> farmers;
    private final List<OBJ_Trap> traps;
    private final List<OBJ_Pig> pigNPCs;
    private Random random;

    /**
     * Creates a spawner that uses the level's entity lists to avoid occupied tiles.
     *
     * @param bonusRewards active bonus reward list
     * @param farmers      active farmer list
     * @param traps        active trap list
     * @param pigNPCs      active pig NPC list
     */
    public BonusRewardSpawner(List<OBJ_Wheat> bonusRewards, List<Farmer> farmers,
                              List<OBJ_Trap> traps, List<OBJ_Pig> pigNPCs) {
        if (bonusRewards == null || farmers == null || traps == null || pigNPCs == null) {
            throw new IllegalArgumentException("bonus reward lists cannot be null");
        }

        this.bonusRewards = bonusRewards;
        this.farmers = farmers;
        this.traps = traps;
        this.pigNPCs = pigNPCs;
        this.random = new Random();
    }

    /**
     * Updates active reward timers and removes expired rewards.
     */
    public void updateRewards() {
        for (int i = bonusRewards.size() - 1; i >= 0; i--) {
            OBJ_Wheat reward = bonusRewards.get(i);
            reward.update();
            if (reward.getRemainingTicks() <= 0) {
                reward.removeWheat();
                bonusRewards.remove(i);
            }
        }
    }

    /**
     * Attempts to spawn a bonus reward at a random valid location.
     *
     * @param map    the map for this level
     * @param player the player to avoid spawning on
     */
    public void trySpawnBonusReward(GameMap map, PlayerPig player) {
        if (bonusRewards.size() >= MAX_BONUS_REWARDS) {
            return;
        }

        if (random.nextDouble() > BONUS_REWARD_SPAWN_CHANCE) {
            return;
        }

        for (int attempts = 0; attempts < MAX_SPAWN_ATTEMPTS; attempts++) {
            int x = random.nextInt(map.getWidth());
            int y = random.nextInt(map.getHeight());

            if (canSpawnBonusRewardAt(map, player, x, y)) {
                bonusRewards.add(new OBJ_Wheat(x, y));
                return;
            }
        }
    }

    /**
     * Checks whether a bonus reward can be spawned at a given tile.
     *
     * @param map    the map for this level
     * @param player the player to avoid spawning on
     * @param x      the x-coordinate to check
     * @param y      the y-coordinate to check
     * @return true if a reward can spawn there, false otherwise
     */
    public boolean canSpawnBonusRewardAt(GameMap map, PlayerPig player, int x, int y) {
        if (!map.isWalkable(x, y)) {
            return false;
        }

        if (player != null && player.getX() == x && player.getY() == y) {
            return false;
        }

        for (Farmer farmer : farmers) {
            if (farmer.getX() == x && farmer.getY() == y) {
                return false;
            }
        }

        for (OBJ_Trap trap : traps) {
            if (trap.getX() == x && trap.getY() == y) {
                return false;
            }
        }

        for (OBJ_Pig pig : pigNPCs) {
            if (pig.getX() == x && pig.getY() == y) {
                return false;
            }
        }

        for (OBJ_Wheat wheat : bonusRewards) {
            if (wheat.getX() == x && wheat.getY() == y) {
                return false;
            }
        }

        return true;
    }

    void setRandom(Random random) {
        if (random == null) {
            throw new IllegalArgumentException("random cannot be null");
        }
        this.random = random;
    }
}
