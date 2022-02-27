package de.niklaseckert.epro_project.controller.exceptions;

/**
 * Exception which is thrown when a {@link de.niklaseckert.epro_project.model.User User} does not have the right permission.
 *
 * @author Niklas Eckert
 * @author Jakob Friedsam
 * @author Fabian Schulz
 */
public class InvalidUserException extends RuntimeException{

    public InvalidUserException(String message) {
        super(message);
    }
}
