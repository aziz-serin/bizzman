package com.bizzman.dao.services;

import com.bizzman.entities.Order;

import java.util.Optional;

public interface OrderService {

    Order save(Order order);

    long count();

    void deleteById(Long id);

    void deleteAll();

    void deleteAll(Iterable<Order> orders);

    Optional<Order> findById(Long id);

    Double getOrderPrice(Order order);

    Iterable<Order> getAllOrders();

    Iterable<Order> getAllOrdersSortedByArrivalDate(boolean isAscending);

    Iterable<Order> getAllOrdersForBusinessRelationship(long businessRelationshipId);

    Iterable<Order> getAllOrdersWithSameType(Order.Type type);

    Iterable<Order> getAllOrdersSameTypeSortedByPrice(Order.Type type, boolean isAscending);

    Iterable<Order> getAllOrdersSameTypeSortedByArrivalDate(Order.Type type, boolean isAscending);

}
