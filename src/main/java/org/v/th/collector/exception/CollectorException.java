package org.v.th.collector.exception;

public class CollectorException extends RuntimeException {

    public CollectorException() {
        super();
    }

    public CollectorException(String message) {
        super(message);
    }

    public CollectorException(String message, Throwable cause) {
        super(message, cause);
    }

    public CollectorException(Throwable cause) {
        super(cause);
    }

    protected CollectorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
