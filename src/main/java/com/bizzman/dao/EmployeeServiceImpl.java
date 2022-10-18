package com.bizzman.dao;

import com.bizzman.dao.repos.EmployeeRepository;
import com.bizzman.dao.services.EmployeeService;
import com.bizzman.entities.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Optional<Employee> findEmployeeById(Long id){
        return employeeRepository.findById(id);
    }

    @Override
    public Iterable<Employee> findAllEmployees(){
        return employeeRepository.findAll();
    }


    @Override
    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public long count(){
        return employeeRepository.count();
    }
}
