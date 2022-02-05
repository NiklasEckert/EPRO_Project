package de.niklaseckert.epro_project.controller.exceptions;

public class BusinessUnitObjectivesKeyResultNotFoundException extends RuntimeException{

    public BusinessUnitObjectivesKeyResultNotFoundException(Long id) {
        super("BusinessUnitObjectiveKeyResult Not Found: " +id);
    }
}
