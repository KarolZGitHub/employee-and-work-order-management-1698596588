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
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date workStarted;
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date workFinished;
    @NotNull
    private Long currentWorkingTime = 0L;
    @NotNull
    private Long overallWorkingTime = 0L;
    @NotNull
    private boolean isWorking = false;
    @NotNull
    @ManyToOne
    private User theUser;
}
