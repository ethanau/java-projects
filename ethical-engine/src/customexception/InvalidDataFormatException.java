package customexception;

/**
 * A class for handling the invalid data format exception
 * @author
 */
public class InvalidDataFormatException extends Exception {

    public InvalidDataFormatException() {
        super();
    }

    public InvalidDataFormatException(String message) {
        super(message);
    }
}
