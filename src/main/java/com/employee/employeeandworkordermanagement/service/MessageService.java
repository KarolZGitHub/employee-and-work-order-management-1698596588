package com.employee.employeeandworkordermanagement.service;

import com.employee.employeeandworkordermanagement.entity.Message;
import com.employee.employeeandworkordermanagement.entity.Task;
import com.employee.employeeandworkordermanagement.entity.User;
import com.employee.employeeandworkordermanagement.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    public void notifyDesignerIfAssignedToTask(User designer, User sender, Task task) {
        Message message = new Message();
        message.setContent("You are assigned to task " + task.getTaskName() + ".");
        message.setReceiver(designer);
        message.setSender(sender);
        messageRepository.save(message);
    }

    public void notifyDesignerIfTaskIsEdited(User designer, User sender, Task task) {
        Message message = new Message();
        message.setContent(task.getTaskName() + " has been edited");
        message.setReceiver(designer);
        message.setSender(sender);
        messageRepository.save(message);
    }

    public void notifyDesignerIfClosedTask(User designer, User sender, Task task) {
        Message message = new Message();
        message.setContent(task.getTaskName() + " has been closed");
        message.setReceiver(designer);
        message.setSender(sender);
        messageRepository.save(message);
    }

    public void notifyDesignerIfTaskIsDeleted(User designer, User sender, Task task) {
        Message message = new Message();
        message.setContent(task.getTaskName() + " has been deleted");
        message.setReceiver(designer);
        message.setSender(sender);
        messageRepository.save(message);
    }

    public void deleteMessage(Message message) {
        messageRepository.delete(message);
    }

    public Message findById(Long id) {
        return messageRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Message has not been found."));
    }

    public void notifyChangeDesignerInTask(User previousUser, User currentUser, User sender, Task task) {
        Message messageToPrevious = new Message();
        messageToPrevious.setContent("You are not assigned to task " + task.getTaskName() + " anymore.");
        messageToPrevious.setSender(sender);
        messageToPrevious.setReceiver(previousUser);
        messageRepository.save(messageToPrevious);
        Message messageToCurrent = new Message();
        messageToCurrent.setReceiver(currentUser);
        messageToCurrent.setSender(sender);
        messageToCurrent.setContent("You are now assigned to task " + task.getTaskName() + ".");
        messageRepository.save(messageToCurrent);
    }
}
