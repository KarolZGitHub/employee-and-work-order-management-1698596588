package com.employee.employeeandworkordermanagement.profile;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ChangeFirstOrLastName {
    @NotBlank
    private String firstOrLastName;
}
