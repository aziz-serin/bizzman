package com.bizzman.dao.repos;

import com.bizzman.entities.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {

    @Override
    long count();
}
