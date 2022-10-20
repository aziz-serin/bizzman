package com.bizzman.dao.services;

import com.bizzman.entities.EmergencyContactDetails;
import com.bizzman.entities.Employee;
import com.bizzman.entities.PersonalDetails;

import java.util.Optional;

public interface EmployeeService {
    Optional<Employee> findEmployeeById(Long id);

    Employee save(Employee employee);

    PersonalDetails getEmployeePersonalDetails(Employee employee);

    EmergencyContactDetails getEmployeeEmergencyContactDetails(Employee employee);

    Iterable<Employee> getAllEmployeeSortedBySalaryAscending();
    Iterable<Employee> getAllEmployeeSortedBySalaryDescending();

    Iterable<Employee> getAllEmployeeSortedByJoiningDateAscending();

    Iterable<Employee> getAllEmployeeSortedByJoiningDateDescending();

    Iterable<Employee> getAllEmployeeSortedByAgeAscending();

    Iterable<Employee> getAllEmployeeSortedByAgeDescending();

    Iterable<Employee> findEmployeeByNationalInsuranceNumber(String nationalInsuranceNumber);

    long count();

    Iterable<Employee> findAllEmployees();
}
