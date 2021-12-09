
/**
 * An abstract class for holding the common information and behaviour of the player and the monster.
 *
 * @author
 */
public abstract class Unit extends Entity {

    // Some variables for representing the information of the player and the monster.
    private String name = "[None]";
    private int damage;
    private int currentHealth;
    private int maximumHealth;


    public Unit(int positionX, int positionY, String name) {
        super(positionX, positionY);
        this.name = name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    /**
     * Display the status of the player or the monster in main menu.
     */
    public String displayStatusInMenu() {
        return  name + " " + currentHealth + "/" + maximumHealth;
    }

    /**
     * Heal the player or the monster to full maximum health.
     */
    public void healed(){
        currentHealth = maximumHealth;
    }

    /**
     * The player or the monster was attacked once by other unit.
     * @param unit the player or the monster.
     */
    public void attacked(Unit unit){
        currentHealth -= unit.getDamage();
    }

    /**
     * Check if the player or the monster is defeated.
     */
    public boolean isDefeated() {

        if (getCurrentHealth() <= 0) {
            return true;
        }
        return false;
    }

    public void setDamage(int damage){
        this.damage = damage;
    }

    public void setHealth(int health){
        maximumHealth = health;
        currentHealth = health;
    }

    public String getName(){
        return name;
    }

    public int getDamage(){
        return damage;
    }

    public int getCurrentHealth(){
        return currentHealth;
    }

    public int getMaximumHealth(){
        return maximumHealth;
    }

}
