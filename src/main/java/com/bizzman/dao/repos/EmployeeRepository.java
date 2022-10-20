package com.bizzman.dao.repos;

import com.bizzman.entities.Employee;
import org.springframework.data.repository.CrudRepository;

public interface EmployeeRepository extends CrudRepository<Employee, Long> {


    //Employee save(Employee employee);
    @Override
    long count();
}
