package com.azizkale.mockitotutorial.service;

import com.azizkale.mockitotutorial.dao.EmployeeRepository;
import com.azizkale.mockitotutorial.dto.EmployeeDto;
import com.azizkale.mockitotutorial.dto.EmployeeResponse;
import com.azizkale.mockitotutorial.exception.EmployeeNotFoundException;
import com.azizkale.mockitotutorial.model.Employee;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public EmployeeResponse findAll(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Employee> employees = employeeRepository.findAll(pageable);
        List<Employee> listOfEmployee = employees.getContent();
        List<EmployeeDto> content = listOfEmployee.stream().map(p -> mapToDto(p)).collect(Collectors.toList());

        EmployeeResponse employeeResponse = new EmployeeResponse();
        employeeResponse.setContent(content);
        employeeResponse.setPageNo(employees.getNumber());
        employeeResponse.setPageSize(employees.getSize());
        employeeResponse.setTotalElements(employees.getTotalElements());
        employeeResponse.setTotalPages(employees.getTotalPages());
        employeeResponse.setLast(employees.isLast());

        return employeeResponse;
    }

    @Override
    public Optional<Employee> findById(int id) throws EmployeeNotFoundException {
        Optional<Employee> employee = employeeRepository.findById(id);
        if(employee == null) {
            throw new EmployeeNotFoundException("Employee not found with id :" + id);
        }
        return employee;
    }

    @Override
    public EmployeeDto create(EmployeeDto employeeDto) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDto, employee);
        employeeRepository.save(employee);
        return employeeDto;
    }

    @Override
    public void delete(int id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new EmployeeNotFoundException("employee could not be delete"));
        employeeRepository.delete(employee);
    }

    @Override
    @Transactional
    public EmployeeDto update(EmployeeDto employeeDto, int id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new EmployeeNotFoundException("employee could not be updated"));

        employee.setName(employeeDto.getName());
        employee.setGender(employeeDto.getGender());
        employee.setDob(employeeDto.getDob());
        employee.setDepartment(employeeDto.getDepartment());

        Employee updatedEmployee = employeeRepository.save(employee);
        return mapToDto(updatedEmployee);

    }

    public EmployeeDto mapToDto(Employee employee) {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setId(employee.getId());
        employeeDto.setName(employee.getName());
        employeeDto.setDepartment(employee.getDepartment());
        employeeDto.setGender(employee.getGender());
        employeeDto.setDob(employee.getDob());
        return employeeDto;
    }

}
