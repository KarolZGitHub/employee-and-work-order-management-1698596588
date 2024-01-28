package com.employee.employeeandworkordermanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Instant;

@Entity
@Data
@Table(name = "working_time_history")
public class WorkingSessionHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Instant createdAt;
    private Instant workStarted;
    private Instant workFinished;

    @NotNull(message = "User has to be set.")
    @ManyToOne
    private User user;

    @NotNull(message = "Task has to be set.")
    @ManyToOne
    private Task task;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
    }
}
