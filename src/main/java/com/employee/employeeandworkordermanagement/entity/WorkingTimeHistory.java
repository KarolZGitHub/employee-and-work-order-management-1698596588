package com.employee.employeeandworkordermanagement.entity;

import com.employee.employeeandworkordermanagement.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "working_time_history")
public class WorkingTimeHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Temporal(TemporalType.TIMESTAMP)
    private Date workStarted;
    @Temporal(TemporalType.TIMESTAMP)
    private Date workFinished;
    @NotNull(message = "Overall working time value cannot be empty.")
    private Long overallWorkingTime;
    @NotNull(message = "User has to be set.")
    @ManyToOne
    private User user;
}
