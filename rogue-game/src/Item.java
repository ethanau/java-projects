
/**
 * A class for holding the information and function specific to the item.
 *
 * @author
 */
public class Item extends Entity {

    // Some constants for representing the symbol of different items.
    private final String HEALING_ITEM = "+";
    private final String DAMAGE_PERK = "^";
    private final String WARP_STONE = "@";

    public Item(int positionX, int positionY, String symbol) {
        super(positionX, positionY);
        setSymbol(symbol);
    }

    /**
     * Item is picked up and used by the player.
     * @param player the player object.
     */
    public void itemUsed(Player player) {

        // If a healing item is used by the player, heal the player to full health.
        if (getSymbol().equals(HEALING_ITEM)) {

            player.healed();
            System.out.println("Healed!");

        // If a damage perk is used by the player, increase the player's damage by one.
        } else if (getSymbol().equals(DAMAGE_PERK)) {

            player.setDamage(player.getDamage() + 1);
            System.out.println("Attack up!");

        // If a warp stone is used by the player, increase the player's level by one, and back to the main menu.
        } else if (getSymbol().equals(WARP_STONE)){

            player.setLevel(player.getLevel() + 1);
            player.setLevelUp(true);
            System.out.println("World complete! (You leveled up!)");
        }
    }
}
