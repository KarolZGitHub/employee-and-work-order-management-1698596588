package com.employee.employeeandworkordermanagement.entity;

import com.employee.employeeandworkordermanagement.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name="break_time")
public class BreakTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "Creation date cannot be empty.")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Temporal(TemporalType.TIMESTAMP)
    private Date finishedAt;
    @NotNull(message = "Break duration cannot be empty.")
    private Long breakDuration;
    @NotNull(message = "isFinished condition cannot be empty.")
    private boolean isFinished;
    @ManyToOne
    private WorkingTime workingTime;
}
