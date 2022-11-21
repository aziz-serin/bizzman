package com.bizzman.dao;

import com.bizzman.dao.repos.OrderRepository;
import com.bizzman.dao.services.OrderService;
import com.bizzman.dao.services.ProductService;
import com.bizzman.entities.BusinessRelationship;
import com.bizzman.entities.Order;
import com.bizzman.entities.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ProductService productService;

    @Override
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public long count() {
        return orderRepository.count();
    }

    @Override
    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        orderRepository.deleteAll();
    }

    @Override
    public void deleteAll(Iterable<Order> orders) {
        orderRepository.deleteAll(orders);
    }

    @Override
    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    public Double getOrderPrice(Long id) {
        Order order = findById(id).orElseThrow();
        List<Product> products = order.getProducts();
        double sum = 0;
        for (Product product : products) {
            sum += productService.getProductTotalSellingPrice(product);
        }
        return sum;
    }

    @Override
    public Iterable<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Iterable<Order> getAllOrdersSortedByArrivalDate(boolean isAscending) {
        List<Order> orders = (List<Order>) getAllOrders();
        if (isAscending){
            orders.sort(Comparator.comparing(Order::getArrivalDate));
        } else {
            orders.sort(Comparator.comparing(Order::getArrivalDate).reversed());
        }
        return orders;
    }

    @Override
    public Iterable<Order> getAllOrdersForBusinessRelationship(BusinessRelationship businessRelationship) {
        List<Order> orders = (List<Order>) getAllOrders();
        return orders.stream()
                .filter(r -> r.getBusinessRelationship().equals(businessRelationship))
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Order> getAllOrdersWithSameType(Order.Type type) {
        List<Order> orders = (List<Order>) getAllOrders();
        return orders.stream()
                .filter(r -> r.getType().equals(type))
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Order> getAllOrdersSameTypeSortedByPrice(Order.Type type, boolean isAscending) {
        List<Order> orders = (List<Order>) getAllOrders();
        List<Order> filteredOrders = orders.stream()
                .filter(o -> o.getType().equals(type))
                .sorted((o1, o2) -> (int) (getOrderPrice(o1.getId()) - getOrderPrice(o2.getId())))
                .collect(Collectors.toList());
        if (!isAscending) {
            Collections.reverse(filteredOrders);
        }
        return filteredOrders;
    }

    @Override
    public Iterable<Order> getAllOrdersSameTypeSortedByArrivalDate(Order.Type type, boolean isAscending) {
        List<Order> orders = (List<Order>) getAllOrders();
        List<Order> filteredOrders = orders.stream()
                .filter(o -> o.getType().equals(type))
                .sorted(Comparator.comparing(Order::getArrivalDate))
                .collect(Collectors.toList());
        if (!isAscending) {
            Collections.reverse(filteredOrders);
        }
        return filteredOrders;
    }
}
