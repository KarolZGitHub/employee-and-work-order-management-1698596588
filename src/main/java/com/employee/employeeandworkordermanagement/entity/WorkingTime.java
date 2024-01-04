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
    @Temporal(TemporalType.TIMESTAMP)
    private Date workStarted;
    @Temporal(TemporalType.TIMESTAMP)
    private Date workFinished;
    @NotNull
    private Long currentWorkingTime;
    @NotNull
    private Long overallWorkingTime;
    @NotNull
    private boolean isWorking = false;
    @NotNull
    @ManyToOne
    private User theUser;
    @NotNull
    @OneToOne
    private Task task;
}
