package com.employee.employeeandworkordermanagement.entity;

import com.employee.employeeandworkordermanagement.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;


@Entity
@Data
@Table(name = "working_time")
public class WorkingTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "Creation date cannot be empty.")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Temporal(TemporalType.TIMESTAMP)
    private Date workStarted;
    @Temporal(TemporalType.TIMESTAMP)
    private Date workFinished;
    @NotNull(message = "Current working time value cannot be empty.")
    private Long currentWorkingTime;
    @NotNull(message = "Overall working time value cannot be empty.")
    private Long overallWorkingTime;
    @NotNull(message = "Is working condition cannot be empty.")
    private boolean isWorking = false;
    @NotNull(message = "Working time have to be set to user.")
    @ManyToOne
    private User user;
    @NotNull(message = "Task cannot be empty")
    @OneToOne
    private Task task;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        currentWorkingTime=0L;
    }
}
