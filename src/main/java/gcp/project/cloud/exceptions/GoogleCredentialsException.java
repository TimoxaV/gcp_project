package gcp.project.cloud.exceptions;

public class GoogleCredentialsException extends RuntimeException {
    public GoogleCredentialsException(String message, Throwable cause) {
        super(message, cause);
    }
}
