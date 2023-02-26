package com.bizzman.dao.services.employee;

import com.bizzman.entities.employee.EmergencyContact;
import com.bizzman.entities.employee.Employee;

public interface EmergencyContactService {
    EmergencyContact save(EmergencyContact emergencyContact);

    void deleteById(long id);

}
