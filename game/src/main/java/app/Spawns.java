package app;
/**
 * Defines the types of entities or objects that can be spawned in the game.
 * Each spawn type is mapped to a unique integer identifier.
 * @author Joshua Tan
 */
public enum Spawns {
    NOTHING(0),
    PLAYER(1),
    PIGNPC(2),
    TRAP(3),
    FARMER(4);

    private int id;

    /**
     * Internal constructor for enum constants.
     * @param id The integer ID to assign to the spawn type.
     */
    Spawns(int id) {
        this.id = id;
    }

    /**
     * Returns the {@link Spawns} enum constant corresponding to the given ID.
     *
     * @param id The integer ID of the spawn type.
     * @return The corresponding Spawns enum constant.
     * @throws IllegalArgumentException if the ID does not match any known spawn type.
     */
    public static Spawns fromInt(int id) {
        for (Spawns spawn: values()) {
            if (spawn.id == id) {
                return spawn;
            }
        }
        throw new IllegalArgumentException("Unknown spawn id: " + id);
    }}
