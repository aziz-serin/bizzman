package com.bizzman.dao;

import com.bizzman.BizzmanApplication;
import com.bizzman.dao.services.employee.EmployeeService;
import com.bizzman.dao.services.employee.PersonalDetailsService;
import com.bizzman.entities.employee.Employee;
import com.bizzman.entities.employee.PersonalDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = BizzmanApplication.class)
@DirtiesContext
@ActiveProfiles("test")
public class PersonalDetailsServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private PersonalDetailsService personalDetailsService;

    @Autowired
    private EmployeeService employeeService;

    @Test
    public void testFindByEmployee() {
        Employee employee = ((List<Employee>) employeeService.findAllEmployees()).get(0);
        PersonalDetails personalDetails = employee.getPersonalDetails();
        Optional<PersonalDetails> personalDetailsOptional = personalDetailsService.findByEmployee(employee);

        assertThat(personalDetailsOptional.isPresent()).isTrue();
        assertThat(personalDetailsOptional.get().getId()).isEqualTo(personalDetails.getId());
        assertThat(personalDetailsOptional.get().getEmployee()).isEqualTo(personalDetails.getEmployee());

    }
}
