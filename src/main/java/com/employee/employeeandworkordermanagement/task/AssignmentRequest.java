package com.employee.employeeandworkordermanagement.task;

import com.employee.employeeandworkordermanagement.entity.User;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignmentRequest {
    @NotNull(message = "User cannot be null")
    private User designer;
}
