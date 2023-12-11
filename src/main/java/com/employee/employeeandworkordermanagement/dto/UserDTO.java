package com.employee.employeeandworkordermanagement.dto;

import com.employee.employeeandworkordermanagement.data.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserDTO {
    private String firstName;
    private String lastName;
    private String email;
    @Enumerated(EnumType.STRING)
    private Role role;
}
