package de.niklaseckert.epro_project.controller.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Exception Handler which handles every possible Exception.
 *
 * @author Niklas Eckert
 * @author Jakob Friedsam
 * @author Fabian Schulz
 */
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Method to handle all not found exceptions.
     *
     * @param exception contains the occured exception.
     * @param webRequest contains the request body.
     * @return a Response Entity which handles the exception.
     */
    @ExceptionHandler(value = {
            BusinessUnitNotFoundException.class,
            BusinessUnitObjectiveNotFoundException.class,
            BusinessUnitObjectivesKeyResultNotFoundException.class,
            CompanyObjectiveNotFoundException.class,
            CompanyObjectiveKeyResultNotFoundException.class,
            UsernameNotFoundException.class

    })
    protected ResponseEntity<Object> handleConflict(RuntimeException exception, WebRequest webRequest) {
        String bodyOfResponse = exception.getMessage();
        return handleExceptionInternal(exception, bodyOfResponse, new HttpHeaders(), HttpStatus.NOT_FOUND, webRequest);
    }

    /**
     * Method to handle {@link InvalidUserException Invalid User Exception}.
     *
     * @param exception contains the occured exception.
     * @param webRequest contains the request body.
     * @return a Response Entity which handles the exception.
     */
    @ExceptionHandler(value = {
            InvalidUserException.class
    })
    protected ResponseEntity<Object> handleInvalidUser(RuntimeException exception, WebRequest webRequest ){
        String bodyOfResponse = exception.getMessage();
        return handleExceptionInternal(exception, bodyOfResponse, new HttpHeaders(), HttpStatus.FORBIDDEN, webRequest);
    }
}
