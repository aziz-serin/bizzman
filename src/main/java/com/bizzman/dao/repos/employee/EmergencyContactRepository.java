package com.bizzman.dao.repos.employee;

import com.bizzman.entities.employee.EmergencyContact;
import org.springframework.data.repository.CrudRepository;

public interface EmergencyContactRepository extends CrudRepository<EmergencyContact, Long> {
}
