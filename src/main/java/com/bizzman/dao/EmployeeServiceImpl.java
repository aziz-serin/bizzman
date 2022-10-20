package com.bizzman.dao;

import com.bizzman.dao.repos.EmployeeRepository;
import com.bizzman.dao.services.EmployeeService;
import com.bizzman.entities.EmergencyContactDetails;
import com.bizzman.entities.Employee;
import com.bizzman.entities.PersonalDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
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
    public PersonalDetails getEmployeePersonalDetails(Employee employee) {
        return employee.getPersonalDetails();
    }

    @Override
    public EmergencyContactDetails getEmployeeEmergencyContactDetails(Employee employee) {
        return employee.getEmergencyContactDetails();
    }

    @Override
    public Iterable<Employee> getAllEmployeeSortedBySalaryAscending() {
        List<Employee> employeeList = (List<Employee>) employeeRepository.findAll();
        employeeList.sort((e1, e2) -> (int) (e1.getSalary() - e2.getSalary()));
        return employeeList;
    }

    @Override
    public Iterable<Employee> getAllEmployeeSortedBySalaryDescending() {
        List<Employee> employeeList = (List<Employee>) employeeRepository.findAll();
        employeeList.sort((e1, e2) -> (int) (e2.getSalary() - e1.getSalary()));
        return employeeList;
    }


    @Override
    public Iterable<Employee> getAllEmployeeSortedByJoiningDateAscending() {
        List<Employee> employeeList = (List<Employee>) employeeRepository.findAll();
        employeeList.sort((e1, e2) ->
                (LocalDate.parse(e1.getJoiningDate()).compareTo(LocalDate.parse(e2.getJoiningDate()))));
        return employeeList;
    }

    @Override
    public Iterable<Employee> getAllEmployeeSortedByJoiningDateDescending() {
        List<Employee> employeeList = (List<Employee>) employeeRepository.findAll();
        employeeList.sort((e1, e2) ->
                (LocalDate.parse(e2.getJoiningDate()).compareTo(LocalDate.parse(e1.getJoiningDate()))));
        return employeeList;
    }

    @Override
    public Iterable<Employee> getAllEmployeeSortedByAgeAscending() {
        List<Employee> employeeList = (List<Employee>) employeeRepository.findAll();
        employeeList.sort((e1, e2) ->
                (int) (e2.getPersonalDetails().getAge() - e1.getPersonalDetails().getAge()));
        return employeeList;
    }

    @Override
    public Iterable<Employee> getAllEmployeeSortedByAgeDescending() {
        List<Employee> employeeList = (List<Employee>) employeeRepository.findAll();
        employeeList.sort((e1, e2) ->
                (int) (e2.getPersonalDetails().getAge() - e1.getPersonalDetails().getAge()));
        return employeeList;
    }

    @Override
    public Iterable<Employee> findEmployeeByNationalInsuranceNumber(String nationalInsuranceNumber) {
        return null;
    }

    @Override
    public long count(){
        return employeeRepository.count();
    }
}
