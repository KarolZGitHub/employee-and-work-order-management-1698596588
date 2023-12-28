package com.employee.employeeandworkordermanagement.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Getter
public class UserAlreadyExistsException extends ResponseStatusException {

    private final String errorCode;

    public UserAlreadyExistsException(String detailedMessage, String errorCode) {
        super(HttpStatus.CONFLICT, detailedMessage);
        this.errorCode = errorCode;
    }

}
