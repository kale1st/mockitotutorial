package com.azizkale.mockitotutorial.service;

import com.azizkale.mockitotutorial.dto.EmployeeDto;
import com.azizkale.mockitotutorial.model.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    List<Employee> findAll();
    Optional<Employee> findById(int id);
    EmployeeDto create(EmployeeDto employeeDto);
    void delete (int id);
    EmployeeDto update (EmployeeDto employeeDto, int id);
}
