package de.niklaseckert.epro_project.controller.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class BusinessUnitObjectiveNotFoundException extends RuntimeException {

    public BusinessUnitObjectiveNotFoundException(Long oid) {
        super("Objective not Found: " + oid);
    }
}
