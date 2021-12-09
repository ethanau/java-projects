package customexception;

/**
 * A class for handling the invalid input exception
 * @author
 */
public class InvalidInputException extends Exception {

    public InvalidInputException() {
        super();
    }

    public InvalidInputException(String message) {
        super(message);
    }
}
