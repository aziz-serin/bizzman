package com.bizzman.dao.repos;

import com.bizzman.entities.EmergencyContact;
import org.springframework.data.repository.CrudRepository;

public interface EmergencyContactRepository extends CrudRepository<EmergencyContact, Long> {
}
