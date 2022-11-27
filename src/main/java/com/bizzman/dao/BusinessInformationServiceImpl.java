package com.bizzman.dao;

import com.bizzman.dao.repos.BusinessInformationRepository;
import com.bizzman.dao.services.BusinessInformationService;
import com.bizzman.entities.BusinessInformation;
import com.bizzman.exceptions.ExceptionMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BusinessInformationServiceImpl implements BusinessInformationService {

    @Autowired
    BusinessInformationRepository businessInformationRepository;

    @Override
    public BusinessInformation save (BusinessInformation businessInformation) throws Exception{
        if (businessInformationRepository.count() == 1) {
            throw new RuntimeException(ExceptionMessages.BUSINESS_INFORMATION_EXCEPTION_MESSAGE);
        }
        return businessInformationRepository.save(businessInformation);
    }

    @Override
    public void deleteById(Long id) {
        businessInformationRepository.deleteById(id);
    }

    @Override
    public Optional<BusinessInformation> getBusinessInformation(){
        List<BusinessInformation> businessInformation = (List<BusinessInformation>) businessInformationRepository.findAll();
        return Optional.ofNullable(businessInformation.get(0));
    }

    @Override
    public long count() {
        return businessInformationRepository.count();
    }
}
