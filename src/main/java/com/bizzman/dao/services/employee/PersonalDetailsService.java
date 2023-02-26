package com.bizzman.dao.services.employee;

import com.bizzman.entities.employee.Employee;
import com.bizzman.entities.employee.PersonalDetails;

import java.util.Optional;

public interface PersonalDetailsService {

    PersonalDetails save(PersonalDetails personalDetails);

    void deleteById(long id);
}
