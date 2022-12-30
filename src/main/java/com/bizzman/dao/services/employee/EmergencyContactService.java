package com.bizzman.dao.services.employee;

import com.bizzman.entities.employee.EmergencyContact;
import com.bizzman.entities.employee.Employee;

public interface EmergencyContactService {
    EmergencyContact save(EmergencyContact emergencyContact);

    Iterable<EmergencyContact> findEmergencyContactDetailsByEmployee(Employee employee);

    void deleteByEmployee(Employee employee, EmergencyContact emergencyContact);

}
