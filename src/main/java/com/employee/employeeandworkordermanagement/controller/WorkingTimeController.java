package com.employee.employeeandworkordermanagement.controller;

import com.employee.employeeandworkordermanagement.dto.UserDTO;
import com.employee.employeeandworkordermanagement.entity.WorkingTime;
import com.employee.employeeandworkordermanagement.service.UserService;
import com.employee.employeeandworkordermanagement.service.WorkingTimeService;
import com.employee.employeeandworkordermanagement.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/work")
public class WorkingTimeController {
    private final WorkingTimeService workingTimeService;
    private final UserService userService;

    @ModelAttribute("user")
    public UserDTO userDTO(Authentication authentication) {
        if (authentication != null) {
            return userService.getUserDTO(authentication);
        } else {
            return null;
        }
    }

    @GetMapping("/work-list")
    public String showYourWorkInformation(@RequestParam(required = false, defaultValue = "0") int page,
                                      @RequestParam(required = false, defaultValue = "asc") String direction,
                                      @RequestParam(required = false, defaultValue = "id") String sortField,
                                      Model model,
                                      Authentication authentication
    ) {
        User theUser = userService.findByEmail(authentication.getName()).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND,"User has not been found."));
        workingTimeService.updateWorkingTime(workingTimeService.findAll());
        model.addAttribute("sortField", sortField);
        Page<WorkingTime> workingTimePage = workingTimeService.getUserSortedWorkingTimePage(page, direction, sortField,theUser);
        model.addAttribute("workingTimePage", workingTimePage);
        return "workingTime/workingTimeList";
    }
    @GetMapping("/all-work-list")
    public String showAllWorkInformation(@RequestParam(required = false, defaultValue = "0") int page,
                                          @RequestParam(required = false, defaultValue = "asc") String direction,
                                          @RequestParam(required = false, defaultValue = "id") String sortField,
                                          Model model
    ) {
        workingTimeService.updateWorkingTime(workingTimeService.findAll());
        model.addAttribute("sortField", sortField);
        Page<WorkingTime> workingTimePage = workingTimeService.getAllSortedWorkingTimePage(page, direction, sortField);
        model.addAttribute("workingTimePage", workingTimePage);
        return "workingTime/workingTimeList";
    }

    @GetMapping("start-work/{id}")
    public String handleStartWork(@PathVariable Long id, Authentication authentication) {
        User user = userService.findByEmail(authentication.getName()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "User has not been found."));
        WorkingTime workingTime = workingTimeService.findById(id);
        if (!workingTime.getUser().equals(user)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "You are not right user.");
        }
        workingTimeService.startWorking(workingTime,authentication);
        return "redirect:/work/work-list";
    }

    @GetMapping("finish-work/{id}")
    public String handleStopWork(@PathVariable Long id, Authentication authentication) {
        User user = userService.findByEmail(authentication.getName()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "User has not been found."));
        WorkingTime workingTime = workingTimeService.findById(id);
        if (!workingTime.getUser().equals(user)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "You are not right user.");
        }
        workingTimeService.stopWorking(workingTime);
        return "redirect:/work/work-list";
    }
}
