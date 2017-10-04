package socialnetwork.exceptions;

/**
 * Created by Roman on 19.08.2017.
 */
public class NameExistsException extends ValidationException {
    public NameExistsException() {
        super();
    }

    public NameExistsException(String message) {
        super(message);
    }

    public NameExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public NameExistsException(Throwable cause) {
        super(cause);
    }

    protected NameExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
