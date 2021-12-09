
import java.io.Serializable;

/**
 * A class for holding the information and behaviour specific to the player.
 *
 * @author
 */
public class Player extends Unit implements Serializable {

    // Some constants for representing the direction of player movement.
    private final String NORTH = "w";
    private final String WEST = "a";
    private final String SOUTH = "s";
    private final String EAST = "d";

    // Some variables specific to the player.
    private int level = 1;
    private boolean isLevelUp = false;

    public Player(int positionX, int positionY, String name) {
        super(positionX, positionY, name);
        setSymbol(name.substring(0,1).toUpperCase());
        setDamage(level + 1);
        setHealth(level * 3 + 17);
    }

    /**
     * Display the status of the player.
     */
    public void status() {

        System.out.println(getName() + " (Lv. " + level + ")");
        System.out.println("Damage: " + getDamage());
        System.out.println("Health: " + getCurrentHealth() + "/" + getMaximumHealth());

    }

    /**
     * Update the player's position according to the direction of movement entered by the user.
     * @param direction the moving direction of the player entered by the user.
     * @param map the map object of the game world.
     */
    public void updatePosition(String direction, Map map) {

        // Record the position before updating and use it when the terrain is not traversable.
        int positionXBeforeUpdate = getPositionX();
        int positionYBeforeUpdate = getPositionY();

        // Update the position of the player after checking the moving direction and the game world boundary.
        if (direction.equalsIgnoreCase(NORTH) && getPositionY() > 0 ) {
            setPositionY(positionYBeforeUpdate - 1);

        } else if (direction.equalsIgnoreCase(EAST) && getPositionX() < map.getWidth() -1 ) {
            setPositionX(positionXBeforeUpdate + 1);

        } else if (direction.equalsIgnoreCase(SOUTH) && getPositionY() < map.getHeight() - 1) {
            setPositionY(positionYBeforeUpdate + 1);

        } else if (direction.equalsIgnoreCase(WEST) && getPositionX() > 0) {
            setPositionX(positionXBeforeUpdate - 1);
        }

        // If the terrain is not traversable, remain in the position before updating.
        if (!map.isTraversable(getPositionY(), getPositionX())) {
            setPositionX(positionXBeforeUpdate);
            setPositionY(positionYBeforeUpdate);
        }
    }

    public void setLevel(int level) {
        this.level = level;
        setDamage(level + 1);
        setHealth(level * 3 + 17);
    }

    public void setLevelUp(boolean isLevelUp) {
        this.isLevelUp = isLevelUp;
    }

    public int getLevel() {
        return level;
    }

    public boolean isLevelUp() {
        return isLevelUp;
    }

}
