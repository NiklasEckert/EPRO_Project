package de.niklaseckert.epro_project.controller.exceptions;

public class InvalidUserException extends RuntimeException{

    public InvalidUserException(String message) {
        super(message);
    }
}
