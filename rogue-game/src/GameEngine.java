
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * A class for running the game engine.
 *
 * @author
 */
public class GameEngine {

    public static void main(String[] args) {

        // Create an instance of the game engine.
        GameEngine gameEngine = new GameEngine();

        // Run the main game loop.
        gameEngine.runGameLoop();
        
    }

    // Create the world and scanner object.
    World world = new World();
    Scanner scanner = new Scanner(System.in);

    // A variable for representing whether continue to run the game.
    private boolean isGameContinue = true;

    /**
     *  Logic for running the main game loop.
     */
    private void runGameLoop() {

        // Print out the title text.
        displayTitleText();

        // Display the status of the player and the monster in main menu.
        displayMenu();

        // Keep user input and execute the right operation according to user input.
        while (scanner.hasNextLine() && isGameContinue){

            // Split user input for checking
            StringTokenizer input = new StringTokenizer(scanner.nextLine());
            String firstString = input.nextToken();

            // Check the user input and execute the right operation according to user input.
            if (firstString.equalsIgnoreCase("help")) {

                // Displays the help text.
                displayHelp();
                continue;

            } else if (firstString.equalsIgnoreCase("commands")) {

                // Displays the commands text.
                displayCommands();
                continue;

            } else if (firstString.equalsIgnoreCase("player")) {

                // Guide the user to initialize the player.
                playerInputGuide();

            } else if (firstString.equalsIgnoreCase("monster")) {

                // Guide the user to initialize the monster.
                monsterInputGuide();

            } else if (firstString.equalsIgnoreCase("load")) {

                // Load existing player data.
                world.loadPlayer();
                continue;

            } else if (firstString.equalsIgnoreCase("save")) {

                // Save player data.
                world.savePlayer();
                continue;

            } else if (firstString.equalsIgnoreCase("start")) {


                if (input.hasMoreTokens()) {

                    // Guide the user to load a existing world.
                    String gameWorldName = input.nextToken();
                    startLoadWorldGuide(gameWorldName);

                } else {

                    // Guide the user to initialize a default world.
                    startDefaultWorldGuide();
                }

            } else if (firstString.equalsIgnoreCase("exit")) {

                // Terminate the game.
                System.out.println("Thank you for playing Rogue!");
                isGameContinue = false;
                break;

            } else if (firstString.equalsIgnoreCase("home")) {

                // Return home.
                System.out.println("Returning home...");
            }

            // Guide user input 'enter' to return main menu.
            System.out.println();
            System.out.println("(Press enter key to return to main menu)");
            scanner.nextLine();
            runGameLoop();

        }
    }

    /**
     *  Logic for loading the game world file and running the game world.
     *  @param gameWorldName the game world name.
     */
    private boolean startLoadWorldGuide(String gameWorldName) {

        // If the player is not created or loaded, guide the user input for the player attributes.
        if (world.getEntities().size() == 0) {
            System.out.println("No player found, please create a player with 'player' first.");

        // If the player is already created or loaded, load the game world file.
        } else {

            try {

                world.loadGameWorld(gameWorldName);

            // Catch a custom GameLevelNotFoundException.
            } catch (GameLevelNotFoundException e) {
                System.out.println(e.getMessage());
                return true;

            } catch (Exception e) {
                System.out.println("An error occurred while loading the file.");
                return true;
            }

            // Initialize each game world with player's level up to false at the beginning.
            Player player = (Player) world.getEntities().get(0);
            player.setLevelUp(false);

            // If the game world continues to run, keep rendering the game world
            // and guiding the user to move the player.
            while (world.isWorldContinue()){

                world.gameWorldRendering();
                String input = scanner.nextLine();

                // If the user enter "home" in half-way, return to the main menu.
                if (input.equalsIgnoreCase("home")){
                    System.out.println("Returning home...");
                    break;

                // If the user enter "exit" in half-way, terminal the game.
                } else if (input.equalsIgnoreCase("exit")) {
                    System.out.println("Thank you for playing Rogue!");
                    isGameContinue = false;
                    break;

                // If the user enter other commands, update the entities in game world.
                } else {
                    world.gameWorldUpdate(input);
                }
            }
        }
        return false;
    }


    /**
     *  Guide the user to initialize the player.
     */
    private void playerInputGuide() {

        // If the player is not created or loaded, guide the user input for the player attributes.
        if (world.getEntities().size() == 0) {

            System.out.println("What is your character's name?");
            String name = scanner.nextLine();

            // Create a player entity based on the user input.
            Player player = new Player(world.getPLAYER_DEFAULT_POSITION_X(),
                    world.getPLAYER_DEFAULT_POSITION_Y(), name);
            world.addEntity(player);
            System.out.println("Player '" + player.getName() + "' created.");

        // If the player is already created or loaded, just display the status.
        } else {

            ((Player)world.getEntities().get(0)).status();
        }
    }

    /**
     *  Guide the user to initialize the monster.
     */
    private void monsterInputGuide() {

        // Guide the user input for the monster attributes, and the character can be recreated.
        System.out.print("Monster name: ");
        String name = scanner.nextLine();

        System.out.print("Monster health: ");
        int health = Integer.parseInt(scanner.nextLine());

        System.out.print("Monster damage: ");
        int damage = Integer.parseInt(scanner.nextLine());

        // Create a monster entity based on the user input.
        Monster monster = new Monster(world.getMONSTER_DEFAULT_POSITION_X(),
                world.getMONSTER_DEFAULT_POSITION_Y(), name, health, damage);
        world.addEntity(monster);
        System.out.println("Monster '" + monster.getName() + "' created.");
    }

    /**
     *  Logic for guiding user initialize a default game world and running the game world.
     */
    private void startDefaultWorldGuide() {

        // If no player was created or loaded, guide the user to create a player first.
        if (world.getEntities().size() == 0) {
            System.out.println("No player found, please create a player with 'player' first.");

        // If the player was existed, but no monster was created, guide the user to create a monster.
        } else if (world.getEntities().size() == 1) {
            System.out.println("No monster found, please create a monster with 'monster' first.");

        // If the player and the monster were both created, start the game world.
        } else {

            Player player = (Player) world.getEntities().get(0);
            Monster monster = (Monster) world.getEntities().get(1);
            Map map = world.getMap();

            // Before starting the game, initialize the default map, set the player to default position,
            // and heal the player and the monster to full maximum health.
            map.initializeDefault();
            player.setPositionX(world.getPLAYER_DEFAULT_POSITION_X());
            player.setPositionY(world.getPLAYER_DEFAULT_POSITION_Y());
            player.healed();
            monster.healed();

            // If the player and the monster were not encountered yet, keep rendering the default game world
            // and guiding the user to move the player.
            while (!player.isEncounterOther(monster)) {

                world.gameWorldRendering();
                String input = scanner.nextLine();

                // If the user enter "home" in half-way, return to the main menu.
                if (input.equalsIgnoreCase("home")){
                    System.out.println("Returning home...");
                    break;

                // If the user enter "exit" in half-way, terminal the game.
                } else if (input.equalsIgnoreCase("exit")) {
                    System.out.println("Thank you for playing Rogue!");
                    isGameContinue = false;
                    break;

                // If the user enter other commands, update the entities in game world.
                } else {
                    player.updatePosition(input, map);
                }
            }

            //If the player and the monster were encountered, start the battle.
            if (player.isEncounterOther(monster)) {
                world.runBattleLoop(player, monster);
            }
        }
    }

    /**
     *  Display the status of the player and the monster in main menu.
     */
    private void displayMenu() {

        if (world.getEntities().size() == 0){
            System.out.println("Player: [None]  | Monster: [None]");
        } else {
            System.out.println("Player: " + ((Player) world.getEntities().get(0)).displayStatusInMenu()
                    + "  | Monster: [None]");
        }

        System.out.println();
        System.out.println("Please enter a command to continue.");
        System.out.println("Type 'help' to learn how to get started.");
        System.out.println();
        System.out.print("> ");
    }

    /**
     *  Displays the title text.
     */
    private void displayTitleText() {

        String titleText = " ____                        \n" +
                "|  _ \\ ___   __ _ _   _  ___ \n" +
                "| |_) / _ \\ / _` | | | |/ _ \\\n" +
                "|  _ < (_) | (_| | |_| |  __/\n" +
                "|_| \\_\\___/ \\__, |\\__,_|\\___|\n" +
                "COMP90041   |___/ Assignment ";

        System.out.println(titleText);
        System.out.println();
    }

    /**
     *  Displays the help text.
     */
    private void displayHelp() {

        System.out.println("Type 'commands' to list all available commands");
        System.out.println("Type 'start' to start a new game");
        System.out.println("Create a character, battle monsters, and find treasure!");
        System.out.println();
        System.out.print("> ");
    }

    /**
     *  Displays the commands text.
     */
    private void displayCommands() {

        System.out.println("help");
        System.out.println("player");
        System.out.println("monster");
        System.out.println("start");
        System.out.println("load");
        System.out.println("save");
        System.out.println("exit");
        System.out.println();
        System.out.print("> ");
    }
}


