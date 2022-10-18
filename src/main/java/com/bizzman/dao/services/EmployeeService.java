package com.bizzman.dao.services;

import com.bizzman.entities.Employee;

import java.util.Optional;

public interface EmployeeService {
    Optional<Employee> findEmployeeById(Long id);

    Employee save(Employee employee);

    long count();

    Iterable<Employee> findAllEmployees();
}
