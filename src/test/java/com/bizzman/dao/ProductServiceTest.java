package com.bizzman.dao;

import com.bizzman.BizzmanApplication;
import com.bizzman.dao.services.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

/* TODO
*   Initialise the database with product and supplier objects
*   Write the tests
*/

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = BizzmanApplication.class)
@DirtiesContext
@ActiveProfiles("test")
class ProductServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    ProductService productService;

    @Test
    public void getProductById() {
    }

    @Test
    public void save() {
    }

    @Test
    public void count() {
    }

    @Test
    public void getAllProductsFromSameSupplier() {
    }

    @Test
    public void getWeightOfAllProductsFromSameSupplier() {
    }

    @Test
    public void getOldestDateOfItemEntry() {
    }

    @Test
    public void getMostRecentDateOfItemEntry() {
    }

    @Test
    public void getAllProductsFromSameCategory() {
    }

    @Test
    public void getProductListSortedByWeight() {
    }

    @Test
    public void getProductTotalPrice() {
    }

    @Test
    public void getTotalWeight() {
    }

    @Test
    public void getTotalWeightOfCategory() {
    }

    @Test
    public void getTotalPriceOfCategory() {
    }

    @Test
    public void getTotalPrice() {
    }

    @Test
    public void getProductListSortedByStockPrice() {
    }
}