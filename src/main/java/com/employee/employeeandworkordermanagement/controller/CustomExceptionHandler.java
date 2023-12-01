package com.employee.employeeandworkordermanagement.controller;

import com.employee.employeeandworkordermanagement.exception.NotFoundException;
import com.employee.employeeandworkordermanagement.exception.UserAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleUserAlreadyExists(UserAlreadyExistsException ex, Model model) {
        model.addAttribute("errorCode", ex.getErrorCode());
        model.addAttribute("errorMessage", ex.getReason());
        return "error/emailError";
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handle404(NotFoundException ex, Model model) {
        model.addAttribute("errorCode", ex.getErrorCode());
        model.addAttribute("errorMessage", ex.getReason());
        return "error/404Error";
    }
}