package com.bizzman.dao;

import static org.assertj.core.api.Assertions.assertThat;

import com.bizzman.BizzmanApplication;
import com.bizzman.dao.services.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = BizzmanApplication.class)
@DirtiesContext
@ActiveProfiles("test")
class OrderServiceImplTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    OrderService orderService;

    @Test
    void getOrderPrice() {
    }

    @Test
    void getAllOrdersSortedByArrivalDate() {
    }

    @Test
    void getAllOrdersForBusinessRelationship() {
    }

    @Test
    void getAllOrdersWithSameType() {
    }
}

/*
* TODO
*  Initialise orders in TestDataLoader
*  Write the tests and make sure they pass
* */