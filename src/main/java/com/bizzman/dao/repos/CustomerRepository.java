package com.bizzman.dao.repos;

import com.bizzman.entities.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, Long> {

    @Override
    long count();
}
