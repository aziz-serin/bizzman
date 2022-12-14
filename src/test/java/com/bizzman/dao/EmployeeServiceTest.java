package com.bizzman.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import com.bizzman.entities.employee.EmergencyContact;
import com.bizzman.entities.employee.PersonalDetails;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.bizzman.BizzmanApplication;
import com.bizzman.dao.services.employee.EmployeeService;
import com.bizzman.entities.employee.Employee;

// These tests will need modification if the TestDataLoader class's product initialisation order or values are modified.

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = BizzmanApplication.class)
@DirtiesContext
@ActiveProfiles("test")
public class EmployeeServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

    // Test any custom method in employeeService
    @Autowired
    private EmployeeService employeeService;

    @Test
    public void getEmployeeEmergencyDetailsReturnsEmergencyContact() {
        Employee employee = ((List<Employee>) employeeService.findAllEmployees()).get(0);
        assertThat(employeeService.getEmployeeEmergencyContactDetails(employee)).isInstanceOf(List.class);
        assertThat(((List<EmergencyContact>) employeeService.getEmployeeEmergencyContactDetails(employee)).get(0))
                .isEqualTo(employee.getEmergencyContactDetails().get(0));
    }

    @Test
    public void getEmployeePersonalDetailsReturnsPersonalDetails() {
        Employee employee = ((List<Employee>) employeeService.findAllEmployees()).get(0);
        assertThat(employeeService.getEmployeePersonalDetails(employee)).isInstanceOf(PersonalDetails.class);
        assertThat(employeeService.getEmployeePersonalDetails(employee)).isEqualTo(employee.getPersonalDetails());
    }

    @Test
    public void sortBySalaryDescendingReturnsTrue() {
        assertThat(((List<Employee>) employeeService.getAllEmployeeSortedBySalaryDescending()).get(0).getName()).isEqualTo("B");
    }

    @Test
    public void sortBySalaryAscendingReturnsTrue() {
        assertThat(((List<Employee>) employeeService.getAllEmployeeSortedBySalaryAscending()).get(0).getName()).isEqualTo("Aziz");
    }

    @Test
    public void sortByJoiningDateDescendingReturnsTrue() {
        assertThat(((List<Employee>) employeeService.getAllEmployeeSortedByJoiningDateDescending()).get(0).getName()).isEqualTo("Aziz");
    }

    @Test
    public void sortByJoiningDateAscendingReturnsTrue() {
        assertThat(((List<Employee>) employeeService.getAllEmployeeSortedByJoiningDateAscending()).get(0).getName()).isEqualTo("B");
    }

    @Test
    public void sortByAgeDescendingReturnsTrue() {
        assertThat(((List<Employee>) employeeService.getAllEmployeeSortedByAgeAscending()).get(0).getName()).isEqualTo("B");
    }

    @Test
    public void sortByAgeAscendingReturnsTrue() {
        assertThat(((List<Employee>) employeeService.getAllEmployeeSortedByAgeDescending()).get(0).getName()).isEqualTo("Aziz");
    }

    @Test
    public void getPersonalDetailsShouldReturnPersonalDetails() {
        Employee employee = ((List<Employee>) employeeService.findAllEmployees()).get(0);
        assertThat(employeeService.getEmployeePersonalDetails(employee)).isEqualTo(employee.getPersonalDetails());
    }

    @Test
    public void getEmergencyContactShouldReturnEmergencyContact() {
        Employee employee = ((List<Employee>) employeeService.findAllEmployees()).get(0);
        assertThat(employeeService.getEmployeeEmergencyContactDetails(employee)).isEqualTo(employee.getEmergencyContactDetails());
    }


}
