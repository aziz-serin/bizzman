package com.bizzman.dao;

import static org.assertj.core.api.Assertions.assertThat;

import com.bizzman.BizzmanApplication;
import com.bizzman.dao.services.BusinessRelationshipService;
import com.bizzman.dao.services.OrderService;
import com.bizzman.entities.BusinessRelationship;
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
class OrderServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    OrderService orderService;

    @Autowired
    BusinessRelationshipService businessRelationshipService;

    // Test only custom methods here, the other ones don't need to be tested, e.g. deleteById, save

    @Test
    public void getOrderPriceReturnsOrderPrice() {
        List<Order> orders = (List<Order>) orderService.getAllOrders();

        assertThat(orderService.getOrderPrice(orders.get(0))).isEqualTo(4532.0);
    }

    @Test
    public void getAllOrdersSortedByArrivalDateSortsOrdersInAscendingOrder() {
        List<Order> orders = (List<Order>) orderService.getAllOrders();
        List<Order> ordersSorted = (List<Order>) orderService.getAllOrdersSortedByArrivalDate(true);

        assertThat(ordersSorted.get(0)).isEqualTo(orders.get(1));
    }

    @Test
    public void getAllOrdersSortedByArrivalDateSortsOrdersInDescendingOrder() {
        List<Order> orders = (List<Order>) orderService.getAllOrders();
        List<Order> ordersSorted = (List<Order>) orderService.getAllOrdersSortedByArrivalDate(false);

        assertThat(ordersSorted.get(0)).isEqualTo(orders.get(3));
    }

    @Test
    public void getAllOrdersForBusinessRelationshipReturnsOrdersForBusiness() {
        List<BusinessRelationship> businessRelationshipList = (List<BusinessRelationship>) businessRelationshipService.getAllRelationships();
        List<Order> orders = (List<Order>) orderService.getAllOrdersForBusinessRelationship(businessRelationshipList.get(0));

        assertThat(orders.get(0).getBusinessRelationship()).isEqualTo(orders.get(1).getBusinessRelationship());
        assertThat(orders.get(0).getBusinessRelationship()).isNotEqualTo(businessRelationshipList.get(1));
        assertThat(orders.get(1).getBusinessRelationship()).isNotEqualTo(businessRelationshipList.get(1));
    }

    @Test
    public void getAllOrdersWithSameTypeReturnsOrdersWithSameType() {
        List<Order> orders = (List<Order>) orderService.getAllOrdersWithSameType(Order.Type.INCOMING);

        assertThat(orders.get(0).getType()).isEqualTo(Order.Type.INCOMING);
        assertThat(orders.get(1).getType()).isEqualTo(Order.Type.INCOMING);
    }

    @Test
    public void getAllOrdersSameTypeSortedByPriceReturnsTrueAscendingOrder() {
        List<Order> orders = (List<Order>) orderService.getAllOrders();
        List<Order> ordersSorted = (List<Order>) orderService.getAllOrdersSameTypeSortedByPrice(Order.Type.INCOMING, true);

        assertThat(ordersSorted.get(0)).isEqualTo(orders.get(0));
    }

    @Test
    public void getAllOrdersSameTypeSortedByPriceReturnsTrueDescendingOrder() {
        List<Order> orders = (List<Order>) orderService.getAllOrders();
        List<Order> ordersSorted = (List<Order>) orderService.getAllOrdersSameTypeSortedByPrice(Order.Type.INCOMING, false);

        assertThat(ordersSorted.get(0)).isEqualTo(orders.get(1));
    }

    @Test
    public void getAllOrdersSameTypeSortedByArrivalDateReturnsTrueAscendingOrder() {
        List<Order> orders = (List<Order>) orderService.getAllOrders();
        List<Order> ordersSorted = (List<Order>) orderService.getAllOrdersSameTypeSortedByArrivalDate(Order.Type.OUTGOING, true);

        assertThat(ordersSorted.get(0)).isEqualTo(orders.get(2));
    }
    @Test
    public void getAllOrdersSameTypeSortedByArrivalDateReturnsTrueDescendingOrder() {
        List<Order> orders = (List<Order>) orderService.getAllOrders();
        List<Order> ordersSorted = (List<Order>) orderService.getAllOrdersSameTypeSortedByArrivalDate(Order.Type.OUTGOING, false);

        assertThat(ordersSorted.get(0)).isEqualTo(orders.get(3));
    }
}