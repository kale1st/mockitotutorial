package com.azizkale.mockitotutorial.service;

import com.azizkale.mockitotutorial.dto.EmployeeDto;
import com.azizkale.mockitotutorial.dto.EmployeeResponse;
import com.azizkale.mockitotutorial.model.Employee;

import java.util.Optional;

public interface EmployeeService {
    EmployeeResponse findAll(int pageNo, int pageSize);
    Optional<Employee> findById(int id);
    EmployeeDto create(EmployeeDto employeeDto);
    void delete (int id);
    EmployeeDto update (EmployeeDto employeeDto, int id);
}
