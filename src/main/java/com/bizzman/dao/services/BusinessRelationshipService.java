package com.bizzman.dao.services;

import com.bizzman.entities.BusinessRelationship;

import java.util.Optional;

public interface BusinessRelationshipService {

    BusinessRelationship save(BusinessRelationship product);

    void deleteById(Long id);

    void deleteAll();

    void deleteAll(Iterable<BusinessRelationship> products);

    Iterable<BusinessRelationship> getAllSuppliers();

    Optional<BusinessRelationship> findById(Long id);

    long count();
}
