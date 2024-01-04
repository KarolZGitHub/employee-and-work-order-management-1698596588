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
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Temporal(TemporalType.TIMESTAMP)
    private Date finishedAt;
    @NotNull
    private Long breakDuration;
    @NotNull
    private boolean isFinished;
    @ManyToOne
    private User user;
}
