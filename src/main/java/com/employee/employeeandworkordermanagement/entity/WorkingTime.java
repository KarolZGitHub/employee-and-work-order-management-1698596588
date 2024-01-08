package com.employee.employeeandworkordermanagement.entity;

import com.employee.employeeandworkordermanagement.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Instant;

@Entity
@Data
@Table(name = "working_time")
public class WorkingTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Creation date cannot be empty.")
    private Instant createdAt;
    private Instant workStarted;
    private Instant workFinished;
    @NotNull(message = "Working time have to be set to user.")
    @ManyToOne
    private User user;
    @ManyToOne
    private Task task;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
    }
}
