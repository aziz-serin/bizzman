package com.bizzman.dao.services;


import com.bizzman.entities.Product;
import com.bizzman.entities.Supplier;

import java.time.LocalDate;
import java.util.Optional;

public interface ProductService {

    Optional<Product> getProductById(Long id);

    Product save(Product product);

    long count();

    Iterable<Product> getAllProductsFromSameSupplier(Supplier supplier);

    double getWeightOfAllProductsFromSameSupplier(Supplier supplier);

    LocalDate getOldestDateOfItemEntry(Long id);

    LocalDate getMostRecentDateOfItemEntry(Long id);

    Iterable<Product> getAllProductsFromSameCategory(String category);

    Iterable<Product> getProductListSortedByWeight(boolean isAscending);

    double getProductTotalPrice(Product product);

    double getTotalPrice();

    double getTotalWeight();

    double getTotalWeightOfCategory(String category);

    double getTotalPriceOfCategory(String category);

    // stock price = price*quantity,
    Iterable<Product> getProductListSortedByStockPrice(boolean isAscending);

}
