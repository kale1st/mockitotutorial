package com.azizkale.mockitotutorial.service;

import com.azizkale.mockitotutorial.dao.EmployeeRepository;
import com.azizkale.mockitotutorial.dto.EmployeeDto;
import com.azizkale.mockitotutorial.exception.EmployeeNotFoundException;
import com.azizkale.mockitotutorial.model.Employee;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAll();
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
        Employee pokemon = employeeRepository.findById(id).orElseThrow(() -> new EmployeeNotFoundException("Pokemon could not be delete"));
        employeeRepository.delete(pokemon);
    }

    @Override
    @Transactional
    public EmployeeDto update(EmployeeDto employeeDto, int id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new EmployeeNotFoundException("Pokemon could not be updated"));

        employee.setName(employeeDto.getName());
        employee.setGender(employeeDto.getGender());
        employee.setDob(employeeDto.getDob());
        employee.setDepartment(employeeDto.getDepartment());

        Employee updatedEmployee = employeeRepository.save(employee);
        return mapToDto(updatedEmployee);

    }

    private EmployeeDto mapToDto(Employee employee) {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setId(employee.getId());
        employeeDto.setName(employee.getName());
        employeeDto.setDepartment(employee.getDepartment());
        employeeDto.setGender(employee.getGender());
        employeeDto.setDob(employee.getDob());
        return employeeDto;
    }

}
