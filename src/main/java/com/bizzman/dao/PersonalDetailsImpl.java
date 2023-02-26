package com.bizzman.dao;

import com.bizzman.dao.repos.employee.PersonalDetailsRepository;
import com.bizzman.dao.services.employee.PersonalDetailsService;
import com.bizzman.entities.employee.PersonalDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonalDetailsImpl implements PersonalDetailsService {

    @Autowired
    PersonalDetailsRepository personalDetailsRepository;

    @Override
    public PersonalDetails save(PersonalDetails personalDetails) {
        return personalDetailsRepository.save(personalDetails);
    }

    @Override
    public void deleteById(long id) {
        personalDetailsRepository.deleteById(id);
    }

}
