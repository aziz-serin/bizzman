package com.bizzman.dao.repos.employee;

import com.bizzman.entities.employee.Employee;
import org.springframework.data.repository.CrudRepository;

public interface EmployeeRepository extends CrudRepository<Employee, Long> {
    @Override
    long count();
}
