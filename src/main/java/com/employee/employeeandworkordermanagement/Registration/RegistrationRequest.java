package com.employee.employeeandworkordermanagement.Registration;

public record RegistrationRequest(
        String firstName,
        String lastName,
        String password,
        String email,
        String role) {

}
