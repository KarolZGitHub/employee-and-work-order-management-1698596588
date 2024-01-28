package com.employee.employeeandworkordermanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.Instant;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "working_duration")
public class WorkingDuration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private Instant workStarted;
    @NotNull
    private Instant workFinished;
    @NotNull
    private String taskName;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
