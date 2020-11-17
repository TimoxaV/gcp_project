package gcp.project.cloud.exceptions;

public class JobWaitingException extends RuntimeException {
    public JobWaitingException(String message, Throwable cause) {
        super(message, cause);
    }
}
