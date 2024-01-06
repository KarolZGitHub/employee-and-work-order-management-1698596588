package com.employee.employeeandworkordermanagement.service;

import com.employee.employeeandworkordermanagement.repository.BreakTimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BreakTimeService {
    private final UserService userService;
    private final BreakTimeRepository breakTimeRepository;

}
