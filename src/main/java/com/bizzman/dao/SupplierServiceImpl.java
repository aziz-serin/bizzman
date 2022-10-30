package com.bizzman.dao;

import com.bizzman.dao.repos.SupplierRepository;
import com.bizzman.dao.services.SupplierService;
import com.bizzman.entities.Supplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SupplierServiceImpl implements SupplierService {

    @Autowired
    SupplierRepository supplierRepository;

    @Override
    public Supplier save(Supplier supplier) {
        return supplierRepository.save(supplier);
    }

    @Override
    public void deleteById(Long id) {
        supplierRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        supplierRepository.deleteAll();
    }

    @Override
    public void deleteAll(Iterable<Supplier> suppliers) {
        supplierRepository.deleteAll(suppliers);
    }

    @Override
    public Iterable<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    @Override
    public Optional<Supplier> findById(Long id) {
        return supplierRepository.findById(id);
    }

    @Override
    public long count() {
        return supplierRepository.count();
    }
}
