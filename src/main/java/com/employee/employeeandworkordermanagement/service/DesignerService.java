package com.employee.employeeandworkordermanagement.service;

import com.employee.employeeandworkordermanagement.entity.Designer;
import com.employee.employeeandworkordermanagement.repository.DesignerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DesignerService {
    private final DesignerRepository designerRepository;
    public List<Designer> findAll() {
        return designerRepository.findAll();
    }
}
