
/**
 * An abstract class for holding the common information and behaviour of the entities in the game world.
 *
 * @author
 */
public abstract class Entity {

    // Some variables for representing the position of entity.
    private int positionX;
    private int positionY;

    // A variable for representing the symbol of entity.
    private String symbol;

    public Entity(int positionX, int positionY){
        this.positionX = positionX;
        this.positionY = positionY;
    }

    /**
     * Render the entity's symbol at a specific position in the game world.
     * @param positionY the entity's positionY.
     * @param positionX the entity's positionX.
     */
    public boolean display(int positionY, int positionX) {

        if (this.positionY == positionY && this.positionX == positionX){
            System.out.print(symbol);
            return true;
        }
        return false;
    }

    /**
     * Check if an entity encounters another entity.
     */
    public boolean isEncounterOther(Entity entity) {

        if (this.positionX == entity.getPositionX() && this.positionY == entity.getPositionY()) {
            return true;
        }
        return false;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public String getSymbol() {
        return symbol;
    }
}


