package de.niklaseckert.epro_project.controller.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception which is thrown when a {@link de.niklaseckert.epro_project.model.CompanyObjectiveKeyResult Company Objective Key Result} is not found.
 *
 * @author Niklas Eckert
 * @author Jakob Friedsam
 * @author Fabian Schulz
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class CompanyObjectiveKeyResultNotFoundException extends RuntimeException{

    public CompanyObjectiveKeyResultNotFoundException(Long id) {
        super("CompanyObjectiveKeyResult Not Found: " + id);
    }
}
