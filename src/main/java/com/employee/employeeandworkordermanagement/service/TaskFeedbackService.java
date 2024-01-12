package com.employee.employeeandworkordermanagement.service;

import com.employee.employeeandworkordermanagement.data.TaskStatus;
import com.employee.employeeandworkordermanagement.entity.Task;
import com.employee.employeeandworkordermanagement.entity.TaskFeedback;
import com.employee.employeeandworkordermanagement.feedback.FeedbackRequest;
import com.employee.employeeandworkordermanagement.repository.TaskFeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskFeedbackService {
    private final TaskFeedbackRepository taskFeedbackRepository;
    private final TaskService taskService;
    public void addFeedback(Long taskId, FeedbackRequest feedbackRequest){
        Task task = taskService.findById(taskId);
        TaskFeedback taskFeedback =new TaskFeedback();
        taskFeedback.setTask(task);
        taskFeedback.setSet(true);
        taskFeedback.setFeedback(feedbackRequest.getFeedback());
        taskFeedback.setGrade(feedbackRequest.getGrade());
        taskFeedbackRepository.save(taskFeedback);
    }
}
