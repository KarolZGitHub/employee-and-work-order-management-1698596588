package com.employee.employeeandworkordermanagement.service;

import com.employee.employeeandworkordermanagement.entity.Task;
import com.employee.employeeandworkordermanagement.entity.TaskFeedback;
import com.employee.employeeandworkordermanagement.feedback.FeedbackRequest;
import com.employee.employeeandworkordermanagement.repository.TaskFeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class TaskFeedbackService {
    private final TaskFeedbackRepository taskFeedbackRepository;
    private final TaskService taskService;

    public void addFeedback(Long taskId, FeedbackRequest feedbackRequest) {
        Task task = taskService.findById(taskId);
        if(task.getTaskFeedback() != null){
            throw  new ResponseStatusException(HttpStatus.CONFLICT,"You cannot set feedback once more.");
        }
        TaskFeedback taskFeedback = new TaskFeedback();
        taskFeedback.setTask(task);
        taskFeedback.setFeedback(feedbackRequest.getFeedback());
        taskFeedback.setGrade(feedbackRequest.getGrade());
        taskFeedbackRepository.save(taskFeedback);
    }
}
