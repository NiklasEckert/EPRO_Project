package de.niklaseckert.epro_project.controller.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class CompanyObjectiveNotFoundException extends RuntimeException{

    public CompanyObjectiveNotFoundException(Long id) {
        super("CompanyObjective Not Found: " + id);
    }
}
