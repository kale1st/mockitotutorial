package com.azizkale.mockitotutorial.controller;

import com.azizkale.mockitotutorial.dto.EmployeeDto;
import com.azizkale.mockitotutorial.dto.EmployeeResponse;
import com.azizkale.mockitotutorial.model.Employee;
import com.azizkale.mockitotutorial.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = EmployeeController.class)
@ExtendWith(MockitoExtension.class)
public class EmployeeControllerTests {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    private Employee employee;
    private EmployeeDto employeeDto;

    @BeforeEach
    public void init () {
        employee = new Employee();
        employeeDto = new EmployeeDto();
    }

    @Test
    public void EmployeeController_CreateEmployee_ReturnCreated () throws Exception {
        // Mocking the service behavior
        given(employeeService.create(ArgumentMatchers.any())).willAnswer(invocation -> invocation.getArgument(0));

        // Performing an HTTP POST request to create an employee
        ResultActions response = mockMvc.perform(post("/api/employee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeDto)));

        // Asserting the response expectations
        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", CoreMatchers.is(employeeDto.getName())))
                .andExpect(jsonPath("$.department", CoreMatchers.is(employeeDto.getDepartment())));
    }

    @Test
    public void EmployeeController_GetEmployees_ReturnResponseDto() throws Exception {
        // Creating a mock response DTO
        EmployeeResponse responseDto = EmployeeResponse.builder().pageSize(10).last(true).pageNo(1).content(Arrays.asList(employeeDto)).build();

        // Mocking the service behavior
        when(employeeService.findAll(1,10)).thenReturn(responseDto);

        // Performing an HTTP GET request to get employees
        ResultActions response = mockMvc.perform(get("/api/employee")
                .contentType(MediaType.APPLICATION_JSON)
                .param("pageNo","1")
                .param("pageSize", "10"));

        // Asserting the response expectations
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", CoreMatchers.is(responseDto.getContent().size())));
    }

    @Test
    public void EmployeeController_FindEmployeeById() throws Exception {
        // Define the employee ID for the test
        int employeeId = 1;

        // Mocking the service behavior to return an Optional containing a specific Employee instance
        when(employeeService.findById(employeeId)).thenReturn(Optional.of(employee));

        // Performing an HTTP GET request to retrieve an employee by ID
        ResultActions response = mockMvc.perform(get("/api/employee/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        // Asserting the response expectations
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.name", CoreMatchers.is(employee.getName())))
                .andExpect(jsonPath("$.department", CoreMatchers.is(employee.getDepartment())));
    }

    @Test
    public void EmployeeController_FindEmployeeById_WhenEmployeeExistsButIsNull() throws Exception {
        // Define the employee ID for the test
        int employeeId = 1;

        // Mocking the service behavior to return an Optional containing a specific Employee instance
        when(employeeService.findById(employeeId)).thenReturn(Optional.of(employee));

        // Performing an HTTP GET request to retrieve an employee by ID
        ResultActions response = mockMvc.perform(get("/api/employee/1")
                .contentType(MediaType.APPLICATION_JSON));

        // Asserting the response expectations
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").doesNotExist())
                .andExpect(jsonPath("$.department").doesNotExist());
    }

    @Test
    public void EmployeeController_FindEmployeeById_WhenEmployeeDoesNotExist() throws Exception {
        // Define the employee ID for the test
        int employeeId = 1;

        // Mock the service behavior to return an empty Optional when findById is called with the given ID
        when(employeeService.findById(employeeId)).thenReturn(Optional.empty());

        // Perform an HTTP GET request to retrieve an employee by ID
        mockMvc.perform(get("/employee/{id}", employeeId))
                .andExpect(status().isNotFound()); // Expecting a 404 Not Found status, as the employee is not found
    }

    @Test
    public void EmployeeController_UpdateEmployee_ReturnEmployeeDto() throws Exception {
        int employeeId = 1;

// Mocking the service behavior
        when(employeeService.update(employeeDto, employeeId)).thenReturn(employeeDto);

// Performing an HTTP PUT request to update an employee
        ResultActions response = mockMvc.perform(put("/api/employee")
                .param("id", String.valueOf(employeeId))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeDto)));

// Asserting the response expectations
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(employeeDto.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.department", CoreMatchers.is(employeeDto.getDepartment())));
    }

    @Test
    public void EmployeeController_DeleteEmployee_ReturnString() throws Exception {
        int employeeId = 1;

        // Mocking the service behavior
        doNothing().when(employeeService).delete(employeeId);

        // Performing an HTTP DELETE request to update an employee
        ResultActions response = mockMvc.perform(delete("/api/employee/1")
                .contentType(MediaType.APPLICATION_JSON));

        // Asserting the response expectations
        response.andExpect(MockMvcResultMatchers.status().isOk());
    }
}