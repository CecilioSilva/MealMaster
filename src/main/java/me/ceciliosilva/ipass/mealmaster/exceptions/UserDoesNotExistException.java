package me.ceciliosilva.ipass.mealmaster.exceptions;

public class UserDoesNotExistException extends Exception {

    public UserDoesNotExistException() {
        super("User does not exist");
    }
}
