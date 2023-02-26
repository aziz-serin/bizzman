package com.bizzman.dao;

import com.bizzman.dao.repos.employee.EmergencyContactRepository;
import com.bizzman.dao.services.employee.EmergencyContactService;
import com.bizzman.entities.employee.EmergencyContact;
import com.bizzman.entities.employee.Employee;
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
    public void deleteById(long id) {
        emergencyContactRepository.deleteById(id);
    }
}
