package me.deshark.lms.server.user.exceptions;

public class UserExitedException extends RuntimeException {
    public UserExitedException(String message) {
        super(message);
    }
}
