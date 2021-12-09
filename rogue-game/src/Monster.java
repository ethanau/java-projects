
/**
 * A class for holding the information and behaviour specific to the monster.
 *
 * @author
 */
public class Monster extends Unit{

    public Monster(int positionX, int positionY, String name, int maximumHealth, int damage) {
        super(positionX, positionY, name);
        setSymbol(name.substring(0,1).toLowerCase());
        setHealth(maximumHealth);
        setDamage(damage);
    }

    /**
     * Update the monster's position according to the position of the player.
     * @param playerLastPositionX the positionX of the player.
     * @param playerLastPositionY the positionY of the player.
     * @param map the map object of the game world.
     */
    public void updatePosition(int playerLastPositionX, int playerLastPositionY, Map map) {

        // Record the position before updating and use it when the terrain is not traversable.
        int positionXBeforeUpdate = getPositionX();
        int positionYBeforeUpdate = getPositionY();

        // Update the position of the monster after checking the position of the player and the game world boundary.
        if (Math.abs(positionXBeforeUpdate - playerLastPositionX) < 3 &&
                Math.abs(positionYBeforeUpdate - playerLastPositionY) < 3) {

            if (positionXBeforeUpdate > playerLastPositionX) {
                setPositionX(positionXBeforeUpdate - 1);

            } else if (positionXBeforeUpdate < playerLastPositionX) {
                setPositionX(positionXBeforeUpdate + 1);
            }

            // If the terrain is not traversable in the east or west direction,
            // remain in the positionX before updating.
            if (!map.isTraversable(getPositionY(), getPositionX()) || positionXBeforeUpdate == playerLastPositionX) {
                setPositionX(positionXBeforeUpdate);

                if (positionYBeforeUpdate > playerLastPositionY) {
                    setPositionY(positionYBeforeUpdate - 1);

                } else if (positionYBeforeUpdate < playerLastPositionY) {
                    setPositionY(positionYBeforeUpdate + 1);
                }

                // If the terrain is also not traversable in the north or south direction,
                // remain in the positionY before updating.
                if (!map.isTraversable(getPositionY(), getPositionX())) {
                    setPositionY(positionYBeforeUpdate);
                }
            }
        }
    }
}
