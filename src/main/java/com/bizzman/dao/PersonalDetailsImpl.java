package com.bizzman.dao;

import com.bizzman.dao.repos.PersonalDetailsRepository;
import com.bizzman.dao.services.PersonalDetailsService;
import com.bizzman.entities.Employee;
import com.bizzman.entities.PersonalDetails;
import com.bizzman.exceptions.custom.PersonalDetailsNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PersonalDetailsImpl implements PersonalDetailsService {

    @Autowired
    PersonalDetailsRepository personalDetailsRepository;

    @Override
    public PersonalDetails save(PersonalDetails personalDetails) {
        return personalDetailsRepository.save(personalDetails);
    }

    @Override
    public void deleteByEmployeeId(Employee employee) {
        Optional<PersonalDetails> personalDetails = findByEmployee(employee);
        if (personalDetails.isPresent()){
            deleteById(personalDetails.get().getId());
        } else {
            throw new PersonalDetailsNotFoundException("Personal Details are not found for the employee: " + employee.getId());
        }
    }

    private void deleteById(long id) {
        personalDetailsRepository.deleteById(id);
    }

    @Override
    public Optional<PersonalDetails> findByEmployee(Employee employee) {
        List<PersonalDetails> personalDetails = (List<PersonalDetails>) personalDetailsRepository.findAll();
        List<PersonalDetails> filteredList =  personalDetails.stream()
                .filter(p -> p.getEmployee().equals(employee))
                .collect(Collectors.toList());
        if (filteredList.size() > 1) {
            throw new RuntimeException("You have more than one personal details for the employee, contact your administrator!");
        } else if (filteredList.size() == 0) {
            return Optional.empty();
        }
        return Optional.of(filteredList.get(0));
    }
}
