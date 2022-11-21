package com.bizzman.dao;

import static org.assertj.core.api.Assertions.assertThat;

import com.bizzman.BizzmanApplication;
import com.bizzman.dao.services.ProductService;
import com.bizzman.entities.Product;
import com.bizzman.entities.BusinessRelationship;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import java.time.LocalDate;
import java.util.List;

// These tests will need modification if the TestDataLoader class's product initialisation order or values are modified.

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = BizzmanApplication.class)
@DirtiesContext
@ActiveProfiles("test")
class ProductServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    ProductService productService;

    // Test only custom methods here, the other ones don't need to be tested, e.g. deleteById, save

    @Test
    public void getAllProductsFromSameSupplierReturnsTrueList() {
        List<BusinessRelationship> businessRelationshipList = (List<BusinessRelationship>) productService.getAllProductSuppliers();
        List<Product> productList = (List<Product>) productService.getAllProducts();

        assertThat(productList.get(0).getSupplier()).isEqualTo(businessRelationshipList.get(0));
    }

    @Test
    public void getWeightOfAllProductsFromSameSupplierReturnsTrueWeight() {
        List<Product> productList = (List<Product>) productService.getAllProducts();
        double productFromSameSupplierWeight = productService
                .getWeightOfAllProductsFromSameSupplier(productList.get(0).getSupplier());
        double weight = productList.get(0).getStockWeight() + productList.get(2).getStockWeight();

        assertThat(productFromSameSupplierWeight == weight).isTrue();
    }

    @Test
    public void getOldestDateOfItemEntryReturnsOldestDate() {
        LocalDate oldestDate = productService.getOldestDateOfItemEntry();
        LocalDate oldest = ( (List<Product>) productService.getAllProducts()).get(2).getArrivalDate();

        assertThat(oldestDate).isEqualTo(oldest);
    }

    @Test
    public void getMostRecentDateOfItemEntryReturnsMostRecentEntry() {
        LocalDate mostRecentDate = productService.getMostRecentDateOfItemEntry();
        LocalDate mostRecent = ( (List<Product>) productService.getAllProducts()).get(1).getArrivalDate();

        assertThat(mostRecentDate).isEqualTo(mostRecent);
    }

    @Test
    public void getAllProductsFromSameCategoryReturnsTrueForProfileCategory() {
        List<Product> productsFromSameCategory = (List<Product>) productService
                .getAllProductsFromSameCategory(Product.ProductCategory.PRICE_BY_WEIGHT);
        List<Product> products = (List<Product>) productService.getAllProducts();

        assertThat(productsFromSameCategory.size() == 2).isTrue();
        assertThat(productsFromSameCategory.contains(products.get(0))).isTrue();
        assertThat(productsFromSameCategory.get(0).getCategory()).isEqualTo(Product.ProductCategory.PRICE_BY_WEIGHT);
        assertThat(productsFromSameCategory.contains(products.get(2))).isTrue();
        assertThat(productsFromSameCategory.get(1).getCategory()).isEqualTo(Product.ProductCategory.PRICE_BY_WEIGHT);
        assertThat(productsFromSameCategory.contains(products.get(3))).isFalse();
    }

    @Test
    public void getAllProductsFromSameCategoryReturnsTrueForAccessoryCategory() {
        List<Product> productsFromSameCategory = (List<Product>) productService
                .getAllProductsFromSameCategory(Product.ProductCategory.PRICE_BY_QUANTITY);
        List<Product> products = (List<Product>) productService.getAllProducts();

        assertThat(productsFromSameCategory.size() == 2).isTrue();
        assertThat(productsFromSameCategory.contains(products.get(1))).isTrue();
        assertThat(productsFromSameCategory.get(0).getCategory()).isEqualTo(Product.ProductCategory.PRICE_BY_QUANTITY);
        assertThat(productsFromSameCategory.contains(products.get(3))).isTrue();
        assertThat(productsFromSameCategory.get(1).getCategory()).isEqualTo(Product.ProductCategory.PRICE_BY_QUANTITY);
        assertThat(productsFromSameCategory.contains(products.get(0))).isFalse();
    }

    @Test
    public void getProductListSortedByWeightReturnsWeightAscending() {
        List<Product> products = (List<Product>) productService.getAllProducts();
        List<Product> sorted = (List<Product>) productService.getProductListSortedByWeight(true);

        assertThat(sorted.get(0)).isEqualTo(products.get(1));
    }

    @Test
    public void getProductListSortedByWeightReturnsWeightDescending() {
        List<Product> products = (List<Product>) productService.getAllProducts();
        List<Product> sorted = (List<Product>) productService.getProductListSortedByWeight(false);

        assertThat(sorted.get(0)).isEqualTo(products.get(2));
    }

    @Test
    public void getProductSellingriceReturnsTruePriceForWeight() {
        List<Product> products = (List<Product>) productService.getAllProducts();
        double totalPrice = products.get(0).getSellingUnitPrice() * products.get(0).getStockWeight();

        assertThat(totalPrice).isEqualTo(productService.getProductTotalSellingPrice(products.get(0)));
    }

    @Test
    public void getProductSellingPriceReturnsTruePriceForQuantity() {
        List<Product> products = (List<Product>) productService.getAllProducts();
        double totalPrice = products.get(1).getSellingUnitPrice() * products.get(1).getQuantity();

        assertThat(totalPrice).isEqualTo(productService.getProductTotalSellingPrice(products.get(1)));
    }

    @Test
    public void getProductEntryriceReturnsTruePriceForWeight() {
        List<Product> products = (List<Product>) productService.getAllProducts();
        double totalPrice = products.get(0).getEntryUnitPrice() * products.get(0).getStockWeight();

        assertThat(totalPrice).isEqualTo(productService.getProductTotalEntryPrice(products.get(0)));
    }

    @Test
    public void getProductEntryPriceReturnsTruePriceForQuantity() {
        List<Product> products = (List<Product>) productService.getAllProducts();
        double totalPrice = products.get(1).getEntryUnitPrice() * products.get(1).getQuantity();

        assertThat(totalPrice).isEqualTo(productService.getProductTotalEntryPrice(products.get(1)));
    }

    @Test
    public void getTotalWeightReturnsTrueWeight() {
        assertThat(productService.getTotalWeight()).isEqualTo(2822);
    }

    @Test
    public void getTotalWeightOfCategoryReturnsTrueWeightForProfile() {
        assertThat(productService.getTotalWeightOfCategory(Product.ProductCategory.PRICE_BY_WEIGHT)).isEqualTo(2600.8);
    }

    @Test
    public void getTotalWeightOfCategoryReturnsTrueWeightForAccessory() {
        assertThat(productService.getTotalWeightOfCategory(Product.ProductCategory.PRICE_BY_QUANTITY)).isEqualTo(221.2);
    }

    @Test
    public void getTotalPriceOfCategoryReturnsTruePriceForProfile() {
        assertThat(productService.getTotalPriceOfCategory(Product.ProductCategory.PRICE_BY_WEIGHT)).isEqualTo(111532);
    }

    @Test
    public void getTotalPriceOfCategoryReturnsTruePriceForAccessory() {
        assertThat(productService.getTotalPriceOfCategory(Product.ProductCategory.PRICE_BY_QUANTITY)).isEqualTo(1450);
    }

    @Test
    public void getExpectedProfitFromProductReturnsTrueAmount() {
        List<Product> products = (List<Product>) productService.getAllProducts();
        assertThat(productService.getExpectedProfitFromProduct(products.get(0))).isEqualTo(1008.0);
    }

    @Test
    public void getTotalPriceForSellingReturnsTrueAmount() {
        assertThat(productService.getTotalPrice(true)).isEqualTo(112982.0);
    }

    @Test
    public void getTotalPriceForEntryReturnsTrueAmount() {
        assertThat(productService.getTotalPrice(false)).isEqualTo(78924);
    }

}