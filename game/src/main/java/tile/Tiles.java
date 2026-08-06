package tile;
import java.awt.image.BufferedImage;

/**
 * @author Ken Tran
 * This class store tile sprites as a number to help make the map.
 * The first argument is the number the tile is stored
 * The second argument is the name of the sprite
 * The third argument is if entities can collide with it or not.
 */
public enum Tiles {
    ENVIRONMENT_001(0, "tile_environment_001.png", false),
    ENVIRONMENT_002(1, "tile_environment_002.png", false),
    ENVIRONMENT_003(2, "tile_environment_003.png", false),
    ENVIRONMENT_004(3, "tile_environment_004.png", false),
    ENVIRONMENT_005(4, "tile_environment_005.png", false),
    ENVIRONMENT_006(5, "tile_environment_006.png", false),
    ENVIRONMENT_007(6, "tile_environment_007.png", false),
    ENVIRONMENT_008(7, "tile_environment_008.png", false),
    ENVIRONMENT_009(8, "tile_environment_009.png", false),
    ENVIRONMENT_010(9, "tile_environment_010.png", false),
    ENVIRONMENT_011(10, "tile_environment_011.png", false),
    ENVIRONMENT_012(11, "tile_environment_012.png", false),
    ENVIRONMENT_013(12, "tile_environment_013.png", false),
    ENVIRONMENT_014(13, "tile_environment_014.png", false),
    ENVIRONMENT_015(14, "tile_environment_015.png", false),
    ENVIRONMENT_016(15, "tile_environment_016.png", false),
    ENVIRONMENT_017(16, "tile_environment_017.png", false),
    ENVIRONMENT_018(17, "tile_environment_018.png", false),
    ENVIRONMENT_019(18, "tile_environment_019.png", false),
    ENVIRONMENT_020(19, "tile_environment_020.png", false),
    ENVIRONMENT_021(20, "tile_environment_021.png", false),
    ENVIRONMENT_022(21, "tile_environment_022.png", false),
    FENCE_000(22, "tile_fence_000.png", true),
    FENCE_001(23, "tile_fence_001.png", true),
    FENCE_002(24, "tile_fence_002.png", true),
    FENCE_003(25, "tile_fence_003.png", true),
    FENCE_004(26, "tile_fence_004.png", true),
    FENCE_005(27, "tile_fence_005.png", true),
    FENCE_006(28, "tile_fence_006.png", true),
    FENCE_007(29, "tile_fence_007.png", true),
    FENCE_008(30, "tile_fence_008.png", true),
    FENCE_009(31, "tile_fence_009.png", true),
    FENCE_010(32, "tile_fence_010.png", true),
    FENCE_011(33, "tile_fence_011.png", true),
    FENCE_012(34, "tile_fence_012.png", true),
    FENCE_013(35, "tile_fence_013.png", true),
    FENCE_014(36, "tile_fence_014.png", true),
    FENCE_015(37, "tile_fence_015.png", true),
    HAY(38, "tile_hay.png", true),
    PLANT_000(39, "tile_plant_000.png", true),
    PLANT_001(40, "tile_plant_001.png", true),
    PLANT_002(41, "tile_plant_002.png", true),
    TRAP_ELECTRIC_FENCE(42, "trap_electric_fence.png", false),
    BARNHOUSE_CLOSED(43, "barnhouseclosed.png", true),
    BARNHOUSE_OPEN(44, "barnhouse.png", true),
    HOUSE(45, "structure_house.png", true),
    WINTERHOUSE(46, "winterrhouse.png", true),



    FENCEWINTER_000(47, "fencewiner000.png", true),
    FENCEWINTER_001(48, "fencewiner001.png", true),
    FENCEWINTER_002(49, "fencewiner002.png", true),
    FENCEWINTER_003(50, "fencewiner003.png", true),
    FENCEWINTER_004(51, "fencewiner004.png", true),
    FENCEWINTER_005(52, "fencewiner005.png", true),
    FENCEWINTER_006(53, "fencewiner006.png", true),
    FENCEWINTER_007(54, "fencewiner007.png", true),
    FENCEWINTER_008(55, "fencewiner008.png", true),
    FENCEWINTER_009(56, "fencewiner009.png", true),
    FENCEWINTER_010(57, "fencewiner010.png", true),
    FENCEWINTER_011(58, "fencewiner011.png", true),
    FENCEWINTER_012(59, "fencewiner012.png", true),
    FENCEWINTER_013(60, "fencewiner013.png", true),
    FENCEWINTER_014(61, "fencewiner014.png", true),
    FENCEWINTER_015(62, "fencewiner015.png", true),

    WINTERTILE_000(63, "wintertile000.png", false),
    WINTERTILE_001(64, "wintertile001.png", false),
    WINTERTILE_002(65, "wintertile002.png", false),
    WINTERTILE_003(66, "wintertile003.png", false),
    WINTERTILE_004(67, "wintertile004.png", false),
    WINTERTILE_005(68, "wintertile005.png", false),
    WINTERTILE_006(69, "wintertile006.png", false),
    WINTERTILE_007(70, "wintertile007.png", false),
    WINTERTILE_008(71, "wintertile008.png", false),
    WINTERTILE_009(72, "wintertile009.png", false),
    WINTERTILE_010(73, "wintertile010.png", false),
    WINTERTILE_011(74, "wintertile011.png", false),
    WINTERTILE_012(75, "wintertile012.png", false),
    WINTERTILE_013(76, "wintertile013.png", false),
    WINTERTILE_014(77, "wintertile014.png", false),
    WINTERTILE_015(78, "wintertile015.png", false),
    WINTERTILE_016(79, "wintertile016.png", false),
    WINTERTILE_017(80, "wintertile017.png", false),
    WINTERTILE_018(81, "wintertile018.png", false),
    WINTERTILE_019(82, "wintertile019.png", false),
    WINTERTILE_020(83, "wintertile020.png", false),
    WINTERTILE_021(84, "wintertile021.png", false),
    WINTERTILE_022(85, "wintertile022.png", false),
    WINTERTILE_023(86, "wintertile023.png", false),
    WINTERTILE_024(87, "wintertile024.png", false),

    WINTERTREE_1(88, "treewinter1.png", true),
    WINTERTREE_2(89, "treewinter2.png", true),
    WINTERBARNHOUSE_OPEN(90, "winterbarnhouse.png", true),
    WINTERBARNHOUSE_CLOSED(91, "winterbarnhouseclosed.png", true);







    private final int id;
    private final String imageName;
    public boolean collision;
    public BufferedImage image;

    Tiles(int id, String imageName, boolean collision) {
        this.id = id;
        this.imageName = imageName;
        this.collision = collision;
    }

    public int getId() {
        return id;
    }

    public String getImageName() {
        return imageName;
    }

    public boolean hasCollision() {
        return collision;
    }

    public static Tiles fromInt(int id) {
        for (Tiles tile : values()) {
            if (tile.id == id) {
                return tile;
            }
        }
        throw new IllegalArgumentException("Unknown tile id: " + id);
    }
}