package com.bizzman.dao.services;

import com.bizzman.entities.Supplier;

import java.util.Optional;

public interface SupplierService {

    Supplier save(Supplier product);

    void deleteById(Long id);

    void deleteAll();

    void deleteAll(Iterable<Supplier> products);

    Iterable<Supplier> getAllSuppliers();

    Optional<Supplier> findById(Long id);

    long count();
}
