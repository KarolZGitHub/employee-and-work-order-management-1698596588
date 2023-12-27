package com.employee.employeeandworkordermanagement.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Getter
public class NotFoundException extends ResponseStatusException {

    private final String errorCode;

    public NotFoundException(String detailedMessage, String errorCode) {
        super(HttpStatus.NOT_FOUND, detailedMessage);
        this.errorCode = errorCode;
    }

}