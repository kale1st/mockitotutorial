package com.azizkale.mockitotutorial.service;

import com.azizkale.mockitotutorial.dao.EmployeeRepository;
import com.azizkale.mockitotutorial.dto.EmployeeDto;
import com.azizkale.mockitotutorial.dto.EmployeeResponse;
import com.azizkale.mockitotutorial.model.Employee;
import org.assertj.core.api.Assertions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    // Creating a Mock to mimic EmployeeRepository.
    @Mock
    private EmployeeRepository employeeRepository;

    // An instance of EmployeeServiceImpl, injected with the Mock created with @Mock.
    @InjectMocks
    private EmployeeServiceImpl employeeService;

    // A test method to test the create method of the EmployeeService class.
    @Test
    public void EmployeeService_CreateEmployee_ReturnsEmployeeDto() {
        // Creating an instance of Employee to be used for the test.
        Employee employee = new Employee();

        // Creating an instance of EmployeeDto to be used for the test.
        EmployeeDto employeeDto = EmployeeDto.builder()
                .name("Aziz Kale")
                .department("IT")
                .gender("male")
                .dob(new Date())
                .build();

        // Using BeanUtils.copyProperties to copy properties from EmployeeDto to Employee for the test.
        BeanUtils.copyProperties(employeeDto, employee);

        // Mocking the employeeRepository.save method to return this instance of Employee whenever any instance of Employee is passed.
        when(employeeRepository.save(Mockito.any(Employee.class))).thenReturn(employee);

        // Calling the create method of EmployeeService and checking if it returns the expected instance of EmployeeDto.
        EmployeeDto savedEmployee = employeeService.create(employeeDto);

        // Using AssertJ to check the correctness of the expected values.
        Assertions.assertThat(savedEmployee).isNotNull();
    }

    @Test
    public void EmployeeService_GetAllEmployee_ReturnsResponseDto(){
        Page<Employee> employees = Mockito.mock(Page.class);

        when(employeeRepository.findAll(Mockito.any(Pageable.class))).thenReturn(employees);

        EmployeeResponse saveEmployee = employeeService.findAll(1,10);

        Assertions.assertThat(saveEmployee).isNotNull();
    }
}
