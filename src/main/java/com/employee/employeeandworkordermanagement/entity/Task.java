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

import java.time.Instant;

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
    private User designer;

    @NotNull(message = "Task status cannot be null")
    @Enumerated(EnumType.STRING)
    private TaskStatus taskStatus = TaskStatus.PENDING;

    private Instant createdAt;
    private Instant editedAt;

    @ManyToOne(cascade = CascadeType.ALL)
    private TaskFeedback taskFeedback;

    @NotNull(message = "Working time has to be set.")
    @ManyToOne(cascade = CascadeType.ALL)
    private WorkingTime workingTime;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        editedAt = Instant.now();
    }
}
