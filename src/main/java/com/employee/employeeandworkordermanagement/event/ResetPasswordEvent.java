package com.employee.employeeandworkordermanagement.event;

import com.employee.employeeandworkordermanagement.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class ResetPasswordEvent extends ApplicationEvent {
    private User user;
    private String applicationUrl;

    public ResetPasswordEvent(User user, String applicationUrl) {
        super(user);
        this.user = user;
        this.applicationUrl = applicationUrl;
    }
}
