package com.employee.employeeandworkordermanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
@Table(name = "message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Title cannot be empty.")
    private String title;
    @NotBlank(message = "Message cannot be empty.")
    private String content;
    @NotNull(message = "Receiver has to be set.")
    @ManyToOne
    private User receiver;
    @NotNull(message = "Sender has to be set.")
    @ManyToOne
    private User sender;
    @NotNull(message = "Is read condition cannot be empty.")
    private boolean isRead = false;
}
