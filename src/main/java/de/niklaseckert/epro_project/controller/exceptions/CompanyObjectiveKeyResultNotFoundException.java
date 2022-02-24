package de.niklaseckert.epro_project.controller.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class CompanyObjectiveKeyResultNotFoundException extends RuntimeException{

    public CompanyObjectiveKeyResultNotFoundException(Long id) {
        super("CompanyObjectiveKeyResult Not Found: " + id);
    }
}
