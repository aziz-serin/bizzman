package com.bizzman.dao.services;

import com.bizzman.entities.BusinessRelationship;

import java.util.Optional;

public interface BusinessRelationshipService {

    BusinessRelationship save(BusinessRelationship relationship);

    void deleteById(Long id);

    void deleteAll();

    void deleteAll(Iterable<BusinessRelationship> relationships);

    Iterable<BusinessRelationship> getAllSuppliers();

    Optional<BusinessRelationship> findById(Long id);

    long count();
}
