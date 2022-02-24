package de.niklaseckert.epro_project.controller.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class BusinessUnitObjectivesKeyResultNotFoundException extends RuntimeException{

    public BusinessUnitObjectivesKeyResultNotFoundException(Long id) {
        super("BusinessUnitObjectiveKeyResult Not Found: " +id);
    }
}
