package com.bizzman.dao.repos;

import com.bizzman.entities.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Long> {

    @Override
    long count();
}
