package com.employee.employeeandworkordermanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Instant;

@Entity
@Data
@Table(name = "break_time")
public class BreakTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "Star")
    private Instant startTime;
    private Instant finishTime;
    @NotNull(message = "Active status cannot be null")
    private boolean isActive;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @PrePersist
    protected void onCreate() {
        this.startTime = Instant.now();
        isActive = true;
    }
}
