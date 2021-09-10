package lab.zhang.hermes.exception;

/**
 * @author zhangrj
 */
public class InvalidParameterException extends RuntimeException {

    public InvalidParameterException() {
        super();
    }

    public InvalidParameterException(String message) {
        super(message);
    }

    public InvalidParameterException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidParameterException(Throwable cause) {
        super(cause);
    }
}
