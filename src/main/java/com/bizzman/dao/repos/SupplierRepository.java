package com.bizzman.dao.repos;

import com.bizzman.entities.Supplier;
import org.springframework.data.repository.CrudRepository;

public interface SupplierRepository extends CrudRepository<Supplier, Long> {
    @Override
    long count();
}
