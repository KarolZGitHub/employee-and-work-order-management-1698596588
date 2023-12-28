package com.employee.employeeandworkordermanagement.controller;

import com.employee.employeeandworkordermanagement.exception.NotFoundException;
import com.employee.employeeandworkordermanagement.exception.UserAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ModelAndView handleUserAlreadyExists(UserAlreadyExistsException ex) {
        ModelAndView modelAndView = new ModelAndView("error/emailError");
        modelAndView.addObject("errorCode", ex.getErrorCode());
        modelAndView.addObject("errorMessage", ex.getReason());
        return modelAndView;
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handle404(NotFoundException ex) {
        ModelAndView modelAndView = new ModelAndView("error/responseError");
        modelAndView.addObject("errorCode", ex.getErrorCode());
        modelAndView.addObject("errorMessage", ex.getReason());
        return modelAndView;
    }

    @ExceptionHandler(ResponseStatusException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleNotFoundException(ResponseStatusException ex) {
        ModelAndView modelAndView = new ModelAndView("error/responseError");
        modelAndView.addObject("errorMessage", ex.getReason());
        return modelAndView;
    }
}