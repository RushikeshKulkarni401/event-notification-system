package org.poc.exception;

public class EventingException extends Exception {
    public EventingException(String message, Throwable e) {
        super(message, e);
    }

    public EventingException(Throwable e) {
        super(e);
    }

    public EventingException(String message) {
        super(message);
    }
}
