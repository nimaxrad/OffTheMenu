package object;

/**
 * Manages the spawning of game objects.
 *
 * @author Ken Tran
 */
public class ObjectMaker {

    /**
     * Creates a different object depending on the provided index.
     * Used by other classes to spawn objects on the map.
     * Index 0 should not be used as it denotes locations in the object map without any objects.
     *
     * @param index of the object
     * @return the object at the specified index
     */
    public static SuperObject createObject(int index) {
        SuperObject newObj;

        switch(index) {
            // 0 means no object
            case 1:
                newObj = new OBJ_Wheat();
                break;
            case 2:
                newObj = new OBJ_Pig();
                break;
            case 3:
                newObj = new OBJ_Barnhouse();
                break;
            case 4:
                newObj = new OBJ_Trap();
                break;
            default:
                return null;
        }

        return newObj;
    }
}