package com.bizzman.config.data;

import com.bizzman.dao.services.EmployeeService;
import com.bizzman.entities.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("default")
public class InitialDataLoader {

    private final static Logger log = LoggerFactory.getLogger(InitialDataLoader.class);

    @Autowired
    private EmployeeService employeeService;

    @Bean
    CommandLineRunner initDatabase(){
        Employee employee = new Employee();
        employee.setName("Harun");
        return args -> {
          if (employeeService.count() > 0) {
              log.info("There is already an employee in the database, skipping the initialisation");
          } else {
              log.info("Loading Data: " + employeeService.save(employee));
          }
        };
    }
}
