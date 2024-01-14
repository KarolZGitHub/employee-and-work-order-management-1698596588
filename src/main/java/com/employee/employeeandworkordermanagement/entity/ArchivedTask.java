package com.employee.employeeandworkordermanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.Instant;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "archived_task")
public class ArchivedTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Task name can not be blank")
    private String taskName;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;
    @NotNull(message = "You have to set designer to this archived task.")
    @OneToOne
    @JoinColumn(name = "designer_id")
    private User designer;

    private Instant createdAt;

    @OneToOne(mappedBy = "task", cascade = CascadeType.ALL)
    private TaskFeedback taskFeedback;

    private Duration totalWorkingTime;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
    }
}
