package me.ceciliosilva.ipass.mealmaster.exceptions;

public class PasswordIncorrectException extends Exception {
    public PasswordIncorrectException() {
        super("Password is incorrect");
    }
}
