package com.portaone.servlet.exception;

public class RecordingException extends RuntimeException {

    public RecordingException() {
    }

    public RecordingException(String message) {
        super(message);
    }

    public RecordingException(String message, Throwable cause) {
        super(message, cause);
    }

    public RecordingException(Throwable cause) {
        super(cause);
    }

    public RecordingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
