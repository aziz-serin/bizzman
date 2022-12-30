package com.bizzman.dao;

import com.bizzman.BizzmanApplication;
import com.bizzman.dao.services.employee.EmergencyContactService;
import com.bizzman.dao.services.employee.EmployeeService;
import com.bizzman.entities.employee.EmergencyContact;
import com.bizzman.entities.employee.Employee;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = BizzmanApplication.class)
@DirtiesContext
@ActiveProfiles("test")
public class EmergencyContactServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private EmergencyContactService emergencyContactService;
    @Autowired
    private EmployeeService employeeService;

    @Test
    public void findEmergencyContactDetailsByEmployeeTest() {
        Employee employee = ((List<Employee>) employeeService.findAllEmployees()).get(0);
        List<EmergencyContact> employeeEmergencyContact = employee.getEmergencyContactDetails();

        List<EmergencyContact> emergencyContact = (List<EmergencyContact>) emergencyContactService.findEmergencyContactDetailsByEmployee(employee);
        assertThat(employeeEmergencyContact.size()).isEqualTo(emergencyContact.size());
        employeeEmergencyContact.forEach(e ->
                assertThat(emergencyContact).contains(e)
        );
    }

}
