package com.bizzman.dao.services.employee;

import com.bizzman.entities.employee.EmergencyContact;
import com.bizzman.entities.employee.Employee;
import com.bizzman.entities.employee.PersonalDetails;

import java.util.Optional;

public interface EmployeeService {
    Optional<Employee> findEmployeeById(Long id);

    Employee save(Employee employee);

    void delete(Long id);

    void deleteAll();

    void deleteAll(Iterable<Employee> employees);

    PersonalDetails getEmployeePersonalDetails(Employee employee);

    Iterable<EmergencyContact> getEmployeeEmergencyContactDetails(Employee employee);

    Iterable<Employee> getAllEmployeeSortedBySalaryAscending();
    Iterable<Employee> getAllEmployeeSortedBySalaryDescending();

    Iterable<Employee> getAllEmployeeSortedByJoiningDateAscending();

    Iterable<Employee> getAllEmployeeSortedByJoiningDateDescending();

    Iterable<Employee> getAllEmployeeSortedByAgeAscending();

    Iterable<Employee> getAllEmployeeSortedByAgeDescending();

    long count();

    Iterable<Employee> findAllEmployees();
}
