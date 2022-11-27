package com.bizzman.dao.services;

import com.bizzman.entities.BusinessInformation;

import java.util.Optional;

public interface BusinessInformationService {
    BusinessInformation save(BusinessInformation businessInformation) throws Exception;

    void deleteById(Long id);

    Optional<BusinessInformation> findById(Long id);

    long count();
}
