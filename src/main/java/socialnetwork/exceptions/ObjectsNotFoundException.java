package socialnetwork.exceptions;

/**
 * Created by Roman on 19.08.2017.
 */
public class ObjectsNotFoundException extends ValidationException {
    public ObjectsNotFoundException() {
        super();
    }

    public ObjectsNotFoundException(String message) {
        super(message);
    }

    public ObjectsNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ObjectsNotFoundException(Throwable cause) {
        super(cause);
    }

    protected ObjectsNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
