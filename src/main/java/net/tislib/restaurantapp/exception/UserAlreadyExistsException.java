package net.tislib.restaurantapp.exception;

public class UserAlreadyExistsException extends RuntimeException {

    private final long serialVersionUID = 1238478347L;

    public UserAlreadyExistsException(String message) {
        super(message);
    }

}
