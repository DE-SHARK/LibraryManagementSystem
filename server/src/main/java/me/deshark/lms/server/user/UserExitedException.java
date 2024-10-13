package me.deshark.lms.server.user;

public class UserExitedException extends RuntimeException {
    public UserExitedException(String message) {
        super(message);
    }
}
