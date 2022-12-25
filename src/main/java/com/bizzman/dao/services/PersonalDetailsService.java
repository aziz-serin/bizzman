package com.bizzman.dao.services;

import com.bizzman.entities.Employee;
import com.bizzman.entities.PersonalDetails;

import java.util.Optional;

public interface PersonalDetailsService {

    PersonalDetails save(PersonalDetails personalDetails);

    void deleteByEmployeeId(Employee employee);

    Optional<PersonalDetails> findByEmployee(Employee employee);
}
