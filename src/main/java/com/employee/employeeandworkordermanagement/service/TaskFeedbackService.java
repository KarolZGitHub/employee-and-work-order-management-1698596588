package com.employee.employeeandworkordermanagement.service;

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
    public void addFeedback(Task task, FeedbackRequest feedbackRequest){
        TaskFeedback taskFeedback =task.getTaskFeedback();
        taskFeedback.setFeedback(feedbackRequest.getFeedback());
        taskFeedback.setDifficulty(feedbackRequest.getDifficulty());
        taskFeedbackRepository.save(taskFeedback);
    }
}
