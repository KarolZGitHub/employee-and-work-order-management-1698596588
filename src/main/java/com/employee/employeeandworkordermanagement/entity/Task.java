package com.employee.employeeandworkordermanagement.entity;

import com.employee.employeeandworkordermanagement.data.TaskStatus;
import com.employee.employeeandworkordermanagement.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "task")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Task name can not be blank")
    private String taskName;
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;
    @NotNull(message = "Assigned designer cannot be null")
    @ManyToOne
    @JoinColumn(name = "assigned_designer_id")
    private User designer;
    @NotNull(message = "Task status cannot be null")
    @Enumerated(EnumType.STRING)
    private TaskStatus taskStatus = TaskStatus.ACTIVE;
    @NotNull(message = "Created at date cannot be null")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();
    @Temporal(TemporalType.TIMESTAMP)
    private Date editedAt;
}
