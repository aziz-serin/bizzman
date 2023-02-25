package com.bizzman.dao.services;


import com.bizzman.entities.Product;
import com.bizzman.entities.BusinessRelationship;

import java.time.LocalDate;
import java.util.Optional;

public interface ProductService {

    Optional<Product> getProductById(Long id);

    Product save(Product product);

    void deleteById(Long id);

    void deleteAll();

    void deleteAll(Iterable<Product> products);

    long count();

    Iterable<Product> getAllProductsFromSameSupplier(long businessRelationshipId);

    Iterable<Product> getAllProducts();

    Iterable<BusinessRelationship> getAllProductSuppliers();

    double getWeightOfAllProductsFromSameSupplier(long businessRelationshipId);

    LocalDate getOldestDateOfItemEntry();

    LocalDate getMostRecentDateOfItemEntry();

    Iterable<Product> getAllProductsFromSameCategory(Product.ProductCategory category);

    Iterable<Product> getProductListSortedByWeight(boolean isAscending);

    double getProductTotalSellingPrice(Product product);

    double getProductTotalEntryPrice(Product product);

    double getTotalPrice(boolean isSelling);

    double getTotalWeight();

    double getExpectedProfitFromProduct(Product product);

    double getTotalWeightOfCategory(Product.ProductCategory category);

    double getTotalPriceOfCategory(Product.ProductCategory category);

}
