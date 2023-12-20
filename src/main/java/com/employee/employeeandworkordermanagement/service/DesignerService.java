package com.employee.employeeandworkordermanagement.service;

import com.employee.employeeandworkordermanagement.entity.Designer;
import com.employee.employeeandworkordermanagement.repository.DesignerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DesignerService {
    private final DesignerRepository designerRepository;

    public List<Designer> findAll() {
        return designerRepository.findAll();
    }

    public Page<Designer> getAllDesigners(PageRequest pageRequest) {
        return designerRepository.findAll(pageRequest);
    }
}
