package com.employee.employeeandworkordermanagement.entity;

import com.employee.employeeandworkordermanagement.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@Table(name = "archived_tasks")
@NoArgsConstructor
public class ArchivedTask {
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
    @NotNull(message = "Created at date cannot be null")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();
    @Temporal(TemporalType.TIMESTAMP)
    private Date archivedAt = new Date();
    @NotBlank(message = "Feedback cannot be null")
    private String feedback;
    @Min(value = 1, message = "Difficulty should be at least 1")
    @Max(value = 10, message = "Difficulty should be at most 10")
    private Integer difficulty;

    public ArchivedTask(String taskName, String description, User designer, Date createdAt) {
        this.taskName = taskName;
        this.description = description;
        this.designer = designer;
        this.createdAt = createdAt;
        this.difficulty = 1;
        this.feedback = "There is no feedback yet.";
    }
}
