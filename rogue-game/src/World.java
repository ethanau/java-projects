
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * A class for rendering the game world, updating the entities in game world, running the battle loop, loading and
 * saving player data, and loading a game world file.
 *
 * @author
 */
public class World {

    // Some constants for representing the position of player and monster in default world.
    private final int PLAYER_DEFAULT_POSITION_X = 1;
    private final int PLAYER_DEFAULT_POSITION_Y = 1;
    private final int MONSTER_DEFAULT_POSITION_X = 4;
    private final int MONSTER_DEFAULT_POSITION_Y = 2;

    // A variable for representing whether continue to run the game world.
    private boolean isWorldContinue = true;

    // Create an ArrayList to hold the entities in the game world.
    private ArrayList<Entity> entities = new ArrayList<>();

    // Create a map object hold the map data in the game world.
    private Map map = new Map();

    /**
     * Render the map and the entities in game world.
     */
    public void gameWorldRendering() {

        // Render the game world by rendering each row.
        for (int y = 0; y < map.getHeight(); y++) {

            // Render each cell in a row.
            for (int x = 0; x < map.getWidth(); x++) {

                boolean isDisplayed = false;
                for (Entity entity : entities) {
                    isDisplayed = entity.display(y, x);

                    // If an entity is rendered, then render the next cell.
                    if (isDisplayed) {
                        break;
                    }
                }

                // If no entity was rendered, then render the map cell.
                if (!isDisplayed) {
                    map.display(y, x);
                }
            }
            System.out.println();
        }
        System.out.println();
        System.out.print("> ");
    }

    /**
     * Update the entities in game world.
     */
    public void gameWorldUpdate(String input) {

        // Record the player's position before update for monster to update position.
        Player player = (Player) entities.get(0);
        int playerLastPositionX = player.getPositionX();
        int playerLastPositionY = player.getPositionY();

        // Update the position of all player and monsters.
        for (Entity entity : entities) {

            if (entity instanceof Player) {
                ((Player) entity).updatePosition(input, map);

            } else if (entity instanceof Monster) {
                ((Monster) entity).updatePosition(playerLastPositionX, playerLastPositionY, map);
            }
        }

        // Record the entities will be removed after the loop, to avoid the error of removing entity in the loop.
        ArrayList<Entity> pendingRemoveEntities = new ArrayList<>();

        // Check if the player encounters any monster or item.
        for (Entity entity : entities) {

            if (entity instanceof Monster) {

                // If the player encounters a monster, run the battle loop, and record the entity is defeated.
                if (player.isEncounterOther(entity)) {
                    runBattleLoop(player, (Monster) entity);

                    if (((Monster) entity).isDefeated()) {
                        pendingRemoveEntities.add(entity);

                        // If the player is defeated, end the game world, and return to the main menu.
                    } else if (player.isDefeated()) {
                        isWorldContinue = false;
                        break;
                    }
                }

            } else if (entity instanceof Item) {

                // If the player encounters a item, the item wll be picked up and used by the player.
                if (player.isEncounterOther(entity)) {
                    ((Item) entity).itemUsed(player);
                    pendingRemoveEntities.add(entity);

                    // If the player picks up warp stone, the player level up, and end the game world.
                    if (player.isLevelUp()) {
                        isWorldContinue = false;
                        break;
                    }
                }
            }
        }

        // Remove defeated monsters or picked up items after the loop.
        if (!pendingRemoveEntities.isEmpty()) {
            for (Entity entity : pendingRemoveEntities) {
                removeEntity(entity);
            }
        }
    }

    /**
     * Display messages of the battle between the play and the monster.
     * @param player the play object.
     * @param monster the monster object.
     */
    public void runBattleLoop(Player player, Monster monster) {

        // Display the player and the monster encounter each other.
        System.out.println(player.getName() + " encountered a " + monster.getName() + "!");

        // The player and the monster keep attacking each other when their current health are both positive.
        while (player.getCurrentHealth() > 0 && monster.getCurrentHealth() > 0) {

            // Display the status of the player and the monster.
            System.out.println();
            System.out.println(player.displayStatusInMenu() + " | " + monster.displayStatusInMenu());

            // The monster is attacked by the player first.
            monster.attacked(player);
            System.out.println(player.getName() + " attacks " + monster.getName() + " for "
                    + player.getDamage() + " damage.");

            // If the monster's current health is positive, it attacks back.
            if (monster.getCurrentHealth() > 0) {
                player.attacked(monster);
                System.out.println(monster.getName() + " attacks " + player.getName() + " for "
                        + monster.getDamage() + " damage.");

                // If the play's current health is not positive after being attacked, indicate the monster wins.
                if (player.getCurrentHealth() <= 0) {
                    System.out.println(monster.getName() + " wins!");
                }

            // If the monster's current health is not positive, indicate the player wins.
            } else  {
                System.out.println(player.getName() + " wins!");
                System.out.println();
            }
        }
    }

    /**
     * Logic for loading the player data.
     */
    public void loadPlayer() {

        String playerFilename = "player.dat";
        Scanner inputPlayerData = null;

        try {

            inputPlayerData = new Scanner(new FileInputStream(playerFilename));

            String name = inputPlayerData.next();
            int level = Integer.parseInt(inputPlayerData.next());

            // Initialize the player based on the "player.dat" file.
            Player player = new Player(PLAYER_DEFAULT_POSITION_X, PLAYER_DEFAULT_POSITION_Y, name);
            player.setLevel(level);

            // Add the player to the game world entities.
            entities.add(player);
            System.out.println("Player data loaded.");

        } catch (FileNotFoundException e) {
            System.out.println("No player found, please create a player with 'player' first.");

        } catch (Exception e) {
            System.out.println("An error occurred while loading the file.");

        }

        System.out.println();
        System.out.print("> ");
    }

    /**
     * Logic for saving the player data.
     */
    public void savePlayer() {

        String playerFilename = "player.dat";

        // Check if the player exists before saving data.
        if (!entities.isEmpty()) {

            try {

                PrintWriter outPlayerData = new PrintWriter(new FileOutputStream(playerFilename));

                Player player = (Player) entities.get(0);

                // Save the player data to the "player.dat" file.
                outPlayerData.print(player.getName() + " " + player.getLevel());
                outPlayerData.close();

                System.out.println("Player data saved.");

            } catch (Exception e) {
                System.err.println("An error occurred while saving the file.");

            }

        // If no player exists, indicate user "No player data to save".
        } else {
            System.out.println("No player data to save.");

        }

        System.out.println();
        System.out.print("> ");
    }

    /**
     *  Logic for loading the game world data.
     *  @param gameWorldName the game world name.
     */
    public void loadGameWorld(String gameWorldName) throws GameLevelNotFoundException, IOException {

        BufferedReader inputStream = null;

        try {

            inputStream = new BufferedReader(new FileReader(gameWorldName + ".dat"));

        // Catch the FileNotFoundException and throw a custom GameLevelNotFoundException.
        } catch (FileNotFoundException e) {
            throw new GameLevelNotFoundException();

        } catch (Exception e) {
            System.out.println("An error occurred while loading the file.");
        }

        // Logic for loading the map data.
        String mapSize = inputStream.readLine();
        String[] mapSizeSplit = mapSize.split(" ");
        int width = Integer.parseInt(mapSizeSplit[0]);
        int height = Integer.parseInt(mapSizeSplit[1]);

        map.setWidth(width);
        map.setHeight(height);

        String[] mapData = new String[height];

        for (int i = 0; i  < height; i++) {
            mapData[i] = inputStream.readLine();
        }
        map.initialize(mapData);

        // Logic for loading the entities data.
        while (true) {

            String entityData = inputStream.readLine();

            if (entityData == null) {
                inputStream.close();
                break;
            }

            String[] entityDataSplit = entityData.split(" ");

            // Logic for loading the player data.
            if (entityDataSplit[0].equalsIgnoreCase("player")) {

                Player player = (Player) entities.get(0);
                int positionX = Integer.parseInt(entityDataSplit[1]);
                int positionY = Integer.parseInt(entityDataSplit[2]);

                player.setPositionX(positionX);
                player.setPositionY(positionY);

            // Logic for loading the monster data.
            } else if (entityDataSplit[0].equalsIgnoreCase("monster")) {

                int positionX = Integer.parseInt(entityDataSplit[1]);
                int positionY = Integer.parseInt(entityDataSplit[2]);
                String name = entityDataSplit[3];
                int health = Integer.parseInt(entityDataSplit[4]);
                int attack = Integer.parseInt(entityDataSplit[5]);

                Monster monster = new Monster(positionX, positionY, name, health, attack);
                addEntity(monster);

            // Logic for loading the item data.
            } else if (entityDataSplit[0].equalsIgnoreCase("item")) {

                int positionX = Integer.parseInt(entityDataSplit[1]);
                int positionY = Integer.parseInt(entityDataSplit[2]);
                String symbol = entityDataSplit[3];

                Item item = new Item(positionX, positionY, symbol);
                addEntity(item);

            }
        }

        inputStream.close();
    }


    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    public void removeEntity(Entity entity) {
        entities.remove(entity);
    }

    public int getPLAYER_DEFAULT_POSITION_X() {
        return PLAYER_DEFAULT_POSITION_X;
    }

    public int getPLAYER_DEFAULT_POSITION_Y() {
        return PLAYER_DEFAULT_POSITION_Y;
    }

    public int getMONSTER_DEFAULT_POSITION_X() {
        return MONSTER_DEFAULT_POSITION_X;
    }

    public int getMONSTER_DEFAULT_POSITION_Y() {
        return MONSTER_DEFAULT_POSITION_Y;
    }

    public boolean isWorldContinue(){
        return isWorldContinue;
    }

    public ArrayList<Entity> getEntities(){
        return entities;
    }

    public Map getMap() {
        return map;
    }
}


