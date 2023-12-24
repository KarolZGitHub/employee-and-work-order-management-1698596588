package com.employee.employeeandworkordermanagement.service;

import com.employee.employeeandworkordermanagement.entity.Message;
import com.employee.employeeandworkordermanagement.entity.Task;
import com.employee.employeeandworkordermanagement.repository.MessageRepository;
import com.employee.employeeandworkordermanagement.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;

    public void saveMessage(Message message) {
        messageRepository.save(message);
    }

    public List<Message> getMessagesForUser(User user) {
        return messageRepository.findAllByReceiver(user);
    }

    public void notifyDesigner(User designer,User sender, Task task) {
        Message message = new Message();
        message.setContent("You are assigned to task " + task.getTaskName() +".");
        message.setReceiver(designer);
        message.setSender(sender);
        messageRepository.save(message);
    }
}