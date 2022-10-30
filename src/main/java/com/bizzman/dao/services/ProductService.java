package com.bizzman.dao.services;


import com.bizzman.entities.Product;
import com.bizzman.entities.Supplier;

import java.time.LocalDate;
import java.util.Optional;

public interface ProductService {

    Optional<Product> getProductById(Long id);

    Product save(Product product);

    void deleteById(Long id);

    void deleteAll();

    void deleteAll(Iterable<Product> products);

    long count();

    Iterable<Product> getAllProductsFromSameSupplier(Supplier supplier);

    Iterable<Product> getAllProducts();

    Iterable<Supplier> getAllProductSuppliers();

    double getWeightOfAllProductsFromSameSupplier(Supplier supplier);

    LocalDate getOldestDateOfItemEntry();

    LocalDate getMostRecentDateOfItemEntry();

    Iterable<Product> getAllProductsFromSameCategory(Product.ProductCategory category);

    Iterable<Product> getProductListSortedByWeight(boolean isAscending);

    double getProductTotalPrice(Product product);

    double getTotalPrice();

    double getTotalWeight();

    double getTotalWeightOfCategory(Product.ProductCategory category);

    double getTotalPriceOfCategory(Product.ProductCategory category);

}
