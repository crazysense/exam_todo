package myyuk.exam.exception;

/**
 * ResourceNotFoundException
 * - No value was found from the data cache.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
