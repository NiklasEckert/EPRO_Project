package de.niklaseckert.epro_project.controller.exceptions;

public class BusinessUnitObjectiveNotFoundException extends RuntimeException {

    public BusinessUnitObjectiveNotFoundException(Long oid) {
        super("Objective not Found: " + oid);
    }
}
