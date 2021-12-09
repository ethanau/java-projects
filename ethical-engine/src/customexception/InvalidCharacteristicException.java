package customexception;

/**
 * A class for handling the invalid characteristic exception
 * @author
 */
public class InvalidCharacteristicException extends Exception {

    public InvalidCharacteristicException() {
        super();
    }

    public InvalidCharacteristicException(String message) {
        super(message);
    }
}
