package de.niklaseckert.epro_project.controller.exceptions;

public class CompanyObjectiveKeyResultNotFoundException extends RuntimeException{

    public CompanyObjectiveKeyResultNotFoundException(Long id) {
        super("CompanyObjectiveKeyResult Not Found: " + id);
    }
}
