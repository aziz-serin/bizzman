package com.bizzman.dao.repos;

import com.bizzman.entities.BusinessInformation;
import org.springframework.data.repository.CrudRepository;

public interface BusinessInformationRepository extends CrudRepository<BusinessInformation, Long> {
    @Override
    long count();
}
