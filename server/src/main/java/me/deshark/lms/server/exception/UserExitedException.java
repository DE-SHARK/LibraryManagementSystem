package me.deshark.lms.server.exception;

public class UserExitedException extends RuntimeException {
    public UserExitedException(String message) {
        super(message);
    }
}
