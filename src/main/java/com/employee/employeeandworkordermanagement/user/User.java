package com.employee.employeeandworkordermanagement.user;

import com.employee.employeeandworkordermanagement.data.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String firstName;
    private String lastName;
    private String password;
    @NaturalId(mutable = true)
    private String email;
    @Enumerated(EnumType.STRING)
    private Role role;
    private boolean isEnabled = false;
}
