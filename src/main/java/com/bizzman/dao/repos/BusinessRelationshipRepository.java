package com.bizzman.dao.repos;

import com.bizzman.entities.BusinessRelationship;
import org.springframework.data.repository.CrudRepository;

public interface BusinessRelationshipRepository extends CrudRepository<BusinessRelationship, Long> {
    @Override
    long count();
}
