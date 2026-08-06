package app;

import java.util.List;

import entity.Farmer;
import entity.PlayerPig;
import object.OBJ_Pig;
import object.OBJ_Trap;
import object.OBJ_Wheat;

/**
 * Handles player collisions with level entities and interactable objects.
 *
 * @author Joshua Tan
 */
public class CollisionHandler {
    private final List<Farmer> farmers;
    private final List<OBJ_Trap> traps;
    private final List<OBJ_Pig> pigNPCs;
    private final List<OBJ_Wheat> bonusRewards;

    /**
     * Creates a handler that checks collisions against the level entity lists.
     *
     * @param farmers      active farmer list
     * @param traps        active trap list
     * @param pigNPCs      active pig NPC list
     * @param bonusRewards active bonus reward list
     */
    public CollisionHandler(List<Farmer> farmers, List<OBJ_Trap> traps,
                            List<OBJ_Pig> pigNPCs, List<OBJ_Wheat> bonusRewards) {
        if (farmers == null || traps == null || pigNPCs == null || bonusRewards == null) {
            throw new IllegalArgumentException("collision target lists cannot be null");
        }

        this.farmers = farmers;
        this.traps = traps;
        this.pigNPCs = pigNPCs;
        this.bonusRewards = bonusRewards;
    }

    /**
     * Checks player interaction/collision with traps, NPCs, collectibles,
     * or other interactables.
     *
     * @param player the player to check against
     */
    public void checkCollisions(PlayerPig player) {
        if (player == null) {
            return;
        }

        int playerX = player.getX();
        int playerY = player.getY();

        checkFarmerCollisions(player, playerX, playerY);
        if (player.isDead()) {
            return;
        }

        checkTrapCollisions(player, playerX, playerY);
        if (player.isDead()) {
            return;
        }

        checkPigNPCCollisions(player, playerX, playerY);
        checkWheatCollisions(player, playerX, playerY);
    }

    private void checkFarmerCollisions(PlayerPig player, int playerX, int playerY) {
        for (Farmer farmer : farmers) {
            if (farmer.getX() == playerX && farmer.getY() == playerY) {
                farmer.triggerHazard(player);
                return;
            }
        }
    }

    private void checkTrapCollisions(PlayerPig player, int playerX, int playerY) {
        for (int i = traps.size() - 1; i >= 0; i--) {
            OBJ_Trap trap = traps.get(i);
            if (trap.getX() == playerX && trap.getY() == playerY) {
                trap.triggerHazard(player);
                if (!trap.isActive()) {
                    traps.remove(i);
                }
                return;
            }
        }
    }

    private void checkPigNPCCollisions(PlayerPig player, int playerX, int playerY) {
        for (int i = pigNPCs.size() - 1; i >= 0; i--) {
            OBJ_Pig pig = pigNPCs.get(i);
            if (pig.getX() == playerX && pig.getY() == playerY) {
                pig.pickUp(player);
                pigNPCs.remove(i);
                return;
            }
        }
    }

    private void checkWheatCollisions(PlayerPig player, int playerX, int playerY) {
        for (int i = bonusRewards.size() - 1; i >= 0; i--) {
            OBJ_Wheat wheat = bonusRewards.get(i);
            if (wheat.getX() == playerX && wheat.getY() == playerY) {
                wheat.pickUp(player);
                bonusRewards.remove(i);
                return;
            }
        }
    }
}
