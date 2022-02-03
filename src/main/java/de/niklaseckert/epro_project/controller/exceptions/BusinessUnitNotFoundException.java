package de.niklaseckert.epro_project.controller.exceptions;

public class BusinessUnitNotFoundException extends RuntimeException {

    public BusinessUnitNotFoundException(Long id) {
        super("Not Found: " + id);
    }
}
