package com.bizzman.dao.services;

import com.bizzman.entities.Customer;

public interface CustomerService {

    Customer save(Customer customer);

    Long count();

    void deleteById(Long id);

    void deleteAll();

    void deleteAll(Iterable<Customer> customers);

}
