package com.employee.employeeandworkordermanagement.feedback;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeedbackRequest {
    @NotBlank
    private String feedback;
    @Min(value = 1, message = "Minimum value is 1.")
    @Max(value = 10, message = "Maximum value is 10.")
    private int grade;
}
