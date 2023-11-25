package com.employee.employeeandworkordermanagement.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserAlreadyExistsException extends ResponseStatusException {

    private final String errorCode;

    public UserAlreadyExistsException(String detailedMessage, String errorCode) {
        super(HttpStatus.CONFLICT, detailedMessage);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
