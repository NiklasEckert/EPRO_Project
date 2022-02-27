package de.niklaseckert.epro_project.controller.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception which is thrown when a {@link de.niklaseckert.epro_project.model.BusinessUnitObjectiveKeyResult Business Unit Objective Key Result} is not found.
 *
 * @author Niklas Eckert
 * @author Jakob Friedsam
 * @author Fabian Schulz
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class BusinessUnitObjectivesKeyResultNotFoundException extends RuntimeException{

    public BusinessUnitObjectivesKeyResultNotFoundException(Long id) {
        super("BusinessUnitObjectiveKeyResult Not Found: " +id);
    }
}
