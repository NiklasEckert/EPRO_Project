package de.niklaseckert.epro_project.controller.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception which is thrown when a {@link de.niklaseckert.epro_project.model.CompanyObjective Company Objective} is not found.
 *
 * @author Niklas Eckert
 * @author Jakob Friedsam
 * @author Fabian Schulz
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class CompanyObjectiveNotFoundException extends RuntimeException{

    public CompanyObjectiveNotFoundException(Long id) {
        super("CompanyObjective Not Found: " + id);
    }
}
