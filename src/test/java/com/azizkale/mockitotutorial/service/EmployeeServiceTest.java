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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.Optional;

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

    @Test
    public void EmployeeService_GetEmployeeById_WhenEmployeeExists_ReturnsEmployeeOptional() {
        // Arrange
        int employeeId = 1;
        Employee mockEmployee = Mockito.mock(Employee.class); // create a mock Employee
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(mockEmployee));

        // Act
        Optional<Employee> result = employeeService.findById(employeeId);

        // Assert
        assertTrue(result.isPresent()); // assert that the result is present
        assertSame(mockEmployee, result.get()); // assert that the result is the same instance as the mockEmployee
    }


    @Test
    public void EmployeeService_GetEmployeeById_WhenEmployeeNotExist_ReturnsOptionalEmpty(){
        // Arrange
        int employeeId = 2;
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        // Act
        Optional<Employee> result = employeeService.findById(employeeId);

        // Assert
        assertFalse(result.isPresent()); // assert that the result is not present
    }

    @Test
    public void EmployeeService_UpdateEmployee_ReturnsEmployeeDto(){
        int employeeId=1;
        Employee employee = Employee.builder()
                .id(employeeId).name("Aziz KAle")
                .dob(new Date()).gender("Male")
                .department("Tesing").build();

        EmployeeDto employeeDto = employeeService.mapToDto(employee);
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.ofNullable(employee));
        when(employeeRepository.save(employee)).thenReturn(employee);

        EmployeeDto updatedEmployee = employeeService.update(employeeDto,employeeId);

        Assertions.assertThat(updatedEmployee).isNotNull();
    }

    @Test
    public void EmployeeService_DeleteEmployee_ReturnsVoid() {
        int employeeId = 1;
        Employee employee = Employee.builder()
                .id(employeeId).name("Aziz KAle")
                .dob(new Date()).gender("Male")
                .department("Tesing").build();

        // When findById is invoked with the specified employeeId, it returns an Optional containing the employee.
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.ofNullable(employee));

        // Configure the delete method to perform no action when called with an Employee object.
        doNothing().when(employeeRepository).delete(employee);

        // Invoke the delete method of the employeeService with the created employeeId.
        employeeService.delete(employeeId);

        // Use assertAll to ensure that no exceptions are thrown during the execution of the delete method.
        assertAll(() -> employeeService.delete(employeeId));
    }

}
