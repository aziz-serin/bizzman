package com.bizzman.dao.services;


import com.bizzman.entities.Product;

import java.time.LocalDate;
import java.util.Optional;

public interface ProductService {

    Optional<Product> findProductById(Long id);

    Product save(Product product);

    long findProductStockWeight(Long id);

    Iterable<Product> findAllProductsFromSameSupplier(String supplier);

    long findWeightOfAllProductsFromSameSupplier(String supplier);

    LocalDate findOldestDateOfItemEntry(Long id);

    LocalDate findMostRecentDateOfItemEntry(Long id);
}
