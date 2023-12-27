package com.employee.employeeandworkordermanagement.repository;

import com.employee.employeeandworkordermanagement.entity.Message;
import com.employee.employeeandworkordermanagement.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface MessageRepository extends JpaRepository<Message,Long> {
    List<Message> findAllByReceiver(User user);
}
