package com.azizkale.mockitotutorial.dao;

import com.azizkale.mockitotutorial.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
}
