package com.bizzman.dao;

import com.bizzman.dao.repos.BusinessRelationshipRepository;
import com.bizzman.dao.services.BusinessRelationshipService;
import com.bizzman.entities.BusinessRelationship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BusinessRelationshipServiceImpl implements BusinessRelationshipService {

    @Autowired
    BusinessRelationshipRepository businessRelationshipRepository;

    @Override
    public BusinessRelationship save(BusinessRelationship businessRelationship) {
        return businessRelationshipRepository.save(businessRelationship);
    }

    @Override
    public void deleteById(Long id) {
        businessRelationshipRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        businessRelationshipRepository.deleteAll();
    }

    @Override
    public void deleteAll(Iterable<BusinessRelationship> suppliers) {
        businessRelationshipRepository.deleteAll(suppliers);
    }

    @Override
    public Iterable<BusinessRelationship> getAllRelationships() {
        return businessRelationshipRepository.findAll();
    }

    @Override
    public Optional<BusinessRelationship> findById(Long id) {
        return businessRelationshipRepository.findById(id);
    }

    @Override
    public long count() {
        return businessRelationshipRepository.count();
    }
}
