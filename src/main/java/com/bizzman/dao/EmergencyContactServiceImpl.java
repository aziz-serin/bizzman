package com.bizzman.dao;

import com.bizzman.dao.repos.EmergencyContactRepository;
import com.bizzman.dao.services.EmergencyContactService;
import com.bizzman.entities.EmergencyContact;
import com.bizzman.entities.Employee;
import com.bizzman.exceptions.custom.EmergencyContactDetailsNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmergencyContactServiceImpl implements EmergencyContactService {

    @Autowired
    private EmergencyContactRepository emergencyContactRepository;

    @Override
    public EmergencyContact save(EmergencyContact emergencyContact) {
        return emergencyContactRepository.save(emergencyContact);
    }

    @Override
    public Iterable<EmergencyContact> findEmergencyContactDetailsByEmployee(Employee employee) {
        List<EmergencyContact> emergencyContactDetails
                = (List<EmergencyContact>) emergencyContactRepository.findAll();
        return emergencyContactDetails.stream()
                .filter(e -> e.getEmployee().equals(employee))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteByEmployee(Employee employee, EmergencyContact emergencyContact) {
        List<EmergencyContact> emergencyContactList
                = (List<EmergencyContact>) findEmergencyContactDetailsByEmployee(employee);
        List<EmergencyContact> filteredList = emergencyContactList.stream()
                .filter(e -> e.equals(emergencyContact))
                .collect(Collectors.toList());
        if (filteredList.size() == 0) {
            throw new EmergencyContactDetailsNotFoundException("Emergency Contact Details " +
                    emergencyContact.getId() + "not found for the employee " + employee.getId());
        }
        deleteById(emergencyContact.getId());
    }

    private void deleteById(Long id) {
        emergencyContactRepository.deleteById(id);
    }
}
