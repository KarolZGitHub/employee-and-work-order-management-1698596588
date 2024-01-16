package com.employee.employeeandworkordermanagement.entity;

import com.employee.employeeandworkordermanagement.data.TaskStatus;
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
    @OneToOne
    @JoinColumn(name = "designer_id")
    private User designer;

    @Enumerated(EnumType.STRING)
    private TaskStatus taskStatus;

    private Instant createdAt;
    private Instant editedAt;

    @OneToOne(mappedBy = "task", cascade = CascadeType.ALL)
    private TaskFeedback taskFeedback;

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
