package com.bizzman.dao;

import static org.assertj.core.api.Assertions.assertThat;

import com.bizzman.BizzmanApplication;
import com.bizzman.dao.services.OrderService;
import com.bizzman.entities.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import java.util.List;

// These tests will need modification if the TestDataLoader class's product initialisation order or values are modified.

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = BizzmanApplication.class)
@DirtiesContext
@ActiveProfiles("test")
class OrderServiceImplTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    OrderService orderService;

    // Test only custom methods here, the other ones don't need to be tested, e.g. deleteById, save

    @Test
    void getOrderPriceReturnsOrderPrice() {
        List<Order> orders = (List<Order>) orderService.getAllOrders();

        assertThat(orderService.getOrderPrice(orders.get(0).getId())).isEqualTo(4532.0);
    }

    @Test
    void getAllOrdersSortedByArrivalDateSortsOrdersInAscendingOrder() {
        List<Order> orders = (List<Order>) orderService.getAllOrders();
        List<Order> ordersSorted = (List<Order>) orderService.getAllOrdersSortedByArrivalDate(true);

        assertThat(ordersSorted.get(0)).isEqualTo(orders.get(1));
    }

    @Test
    void getAllOrdersSortedByArrivalDateSortsOrdersInDescendingOrder() {
        List<Order> orders = (List<Order>) orderService.getAllOrders();
        List<Order> ordersSorted = (List<Order>) orderService.getAllOrdersSortedByArrivalDate(false);

        assertThat(ordersSorted.get(0)).isEqualTo(orders.get(3));
    }

    @Test
    void getAllOrdersForBusinessRelationshipReturnsOrdersForBusiness() {
    }

    @Test
    void getAllOrdersWithSameTypeReturnsOrdersWithSameType() {
    }
}

/*
* TODO
*  Initialise orders in TestDataLoader
*  Write the tests and make sure they pass
* */