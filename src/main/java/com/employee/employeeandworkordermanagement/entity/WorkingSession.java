package com.employee.employeeandworkordermanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "working_time")
public class WorkingSession {
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
    @NotNull(message = "Active status cannot be null")
    private boolean isActive;
    @OneToMany(mappedBy = "workingSession", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<BreakTime> breakTimes = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        isActive = true;
    }
}
