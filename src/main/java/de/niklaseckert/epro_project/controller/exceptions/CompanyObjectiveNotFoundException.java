package de.niklaseckert.epro_project.controller.exceptions;

public class CompanyObjectiveNotFoundException extends RuntimeException{

    public CompanyObjectiveNotFoundException(Long id) {
        super("CompanyObjective Not Found: " + id);
    }
}
