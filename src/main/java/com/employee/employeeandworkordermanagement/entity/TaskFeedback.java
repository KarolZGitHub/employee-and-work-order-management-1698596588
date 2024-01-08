package com.employee.employeeandworkordermanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
@Table(name = "task_feedback")
public class TaskFeedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Feedback cannot be empty.")
    private String feedback;
    @NotNull
    @Min(value = 1, message = "Difficulty should be at least 1")
    @Max(value = 10, message = "Difficulty should be at most 10")
    private Integer difficulty;
    @NotNull(message = "Task feedback has to be set to task.")
    @NotNull(message = "Status cannot be null")
    private boolean isSet;
    @ManyToOne
    private Task task;

    @PrePersist
    protected void onCreate() {
        isSet = false;
    }

    @PreUpdate
    private void onUpdate() {
        isSet = true;
    }
}
