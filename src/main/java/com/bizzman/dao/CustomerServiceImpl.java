package com.bizzman.dao;

import com.bizzman.dao.repos.CustomerRepository;
import com.bizzman.dao.services.CustomerService;
import com.bizzman.entities.Customer;
import org.springframework.beans.factory.annotation.Autowired;

public class CustomerServiceImpl implements CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    @Override
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Long count() {
        return customerRepository.count();
    }

    @Override
    public void deleteById(Long id) {
        customerRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        customerRepository.deleteAll();
    }

    @Override
    public void deleteAll(Iterable<Customer> customers) {
        customerRepository.deleteAll(customers);
    }
}
