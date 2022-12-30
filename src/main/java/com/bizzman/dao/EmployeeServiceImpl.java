package com.bizzman.dao;

import com.bizzman.dao.repos.employee.EmployeeRepository;
import com.bizzman.dao.services.employee.EmployeeService;
import com.bizzman.entities.employee.EmergencyContact;
import com.bizzman.entities.employee.Employee;
import com.bizzman.entities.employee.PersonalDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public void delete(Long id) {
        employeeRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        employeeRepository.deleteAll();
    }

    @Override
    public void deleteAll(Iterable<Employee> employees) {
        employeeRepository.deleteAll(employees);
    }

    @Override
    public PersonalDetails getEmployeePersonalDetails(Employee employee) {
        return employee.getPersonalDetails();
    }

    @Override
    public List<EmergencyContact> getEmployeeEmergencyContactDetails(Employee employee) {
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
                (e1.getJoiningDate().compareTo(e2.getJoiningDate())));
        return employeeList;
    }

    @Override
    public Iterable<Employee> getAllEmployeeSortedByJoiningDateDescending() {
        List<Employee> employeeList = (List<Employee>) employeeRepository.findAll();
        employeeList.sort((e1, e2) ->
                (e2.getJoiningDate().compareTo(e1.getJoiningDate())));
        return employeeList;
    }

    @Override
    public Iterable<Employee> getAllEmployeeSortedByAgeAscending() {
        List<Employee> employeeList = (List<Employee>) employeeRepository.findAll();
        employeeList.sort((e1, e2) ->
                (int) (e1.getPersonalDetails().getAge() - e2.getPersonalDetails().getAge()));
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
    public long count(){
        return employeeRepository.count();
    }
}
