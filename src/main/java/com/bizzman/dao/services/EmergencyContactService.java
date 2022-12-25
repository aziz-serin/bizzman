package com.bizzman.dao.services;

import com.bizzman.entities.EmergencyContact;
import com.bizzman.entities.Employee;

public interface EmergencyContactService {
    EmergencyContact save(EmergencyContact emergencyContact);

    Iterable<EmergencyContact> findEmergencyContactDetailsByEmployee(Employee employee);

    void deleteByEmployee(Employee employee, EmergencyContact emergencyContact);

}
