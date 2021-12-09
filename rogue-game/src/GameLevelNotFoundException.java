
/**
 * A custom exception class to indicate the message "Map not found".
 *
 * @author Yebin Ge
 */
public class GameLevelNotFoundException extends Exception {

    public GameLevelNotFoundException() {
        super("Map not found.");
    }

    public GameLevelNotFoundException(String message) {
        super(message);
    }

}