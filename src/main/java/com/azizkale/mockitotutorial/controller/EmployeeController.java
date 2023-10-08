package com.azizkale.mockitotutorial.controller;

import com.azizkale.mockitotutorial.dto.EmployeeDto;
import com.azizkale.mockitotutorial.dto.EmployeeResponse;
import com.azizkale.mockitotutorial.exception.EmployeeNotFoundException;
import com.azizkale.mockitotutorial.exception.InternalServerException;
import com.azizkale.mockitotutorial.model.Employee;
import com.azizkale.mockitotutorial.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/employee")
    public ResponseEntity<EmployeeResponse> getEmployees(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize
    ) {
        return new ResponseEntity<>(employeeService.findAll(pageNo, pageSize), HttpStatus.OK);
    }

    @GetMapping("/employee/{id}")
    public ResponseEntity<Employee> find(@PathVariable int id) {
        Optional<Employee> employee = employeeService.findById(id);

        return employee.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/employee")
    public ResponseEntity<EmployeeDto> create(@RequestBody EmployeeDto employeeDto){
        try {
            EmployeeDto createdEmployeeDto = employeeService.create(employeeDto);
            Integer id = createdEmployeeDto.getId();
            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                    .buildAndExpand(id).toUri();

            return ResponseEntity.created(location).body(createdEmployeeDto);
        } catch (InternalServerException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/employee")
    public ResponseEntity<?> update(@RequestBody EmployeeDto employeeDto, int id){
        try{
            employeeService.update(employeeDto,id);
            return ResponseEntity.ok(employeeDto);
        } catch(EmployeeNotFoundException ex){
            return ResponseEntity.notFound().build();
        } catch(Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/employee/{id}")
    public ResponseEntity<?> delete(@PathVariable int id){
        try{
            employeeService.delete(id);
            return ResponseEntity.ok().build();
        }catch (EmployeeNotFoundException ex){
            return ResponseEntity.notFound().build();
        }catch (InternalServerException ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}