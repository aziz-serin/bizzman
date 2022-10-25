package com.bizzman.dao;

import com.bizzman.dao.repos.ProductRepository;
import com.bizzman.dao.services.ProductService;
import com.bizzman.entities.Product;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;

    @Override
    public Optional<Product> findProductById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public long findProductStockWeight(Long id) {
        List<Product> productList = (List<Product>) productRepository.findAll();
        return productList.stream()
                .map(Product::getWeight)
                .reduce(0L, Long::sum);
    }

    @Override
    public Iterable<Product> findAllProductsFromSameSupplier(String supplier) {
        List<Product> productList = (List<Product>) productRepository.findAll();
        return productList.stream()
                .filter(o -> o.getSupplier().equals(supplier))
                .collect(Collectors.toList());
    }

    @Override
    public long findWeightOfAllProductsFromSameSupplier(String supplier) {
        List<Product> productList = (List<Product>) productRepository.findAll();
        return productList.stream().filter(o -> o.getSupplier().equals(supplier))
                .map(Product::getWeight)
                .reduce(0L, Long::sum);
    }

    @Override
    public LocalDate findOldestDateOfItemEntry(Long id) {
        return null;
    }

    @Override
    public LocalDate findMostRecentDateOfItemEntry(Long id) {
        return null;
    }

    /*
    * TODO
    *  Separate supplier as its own table from the Product
    *  Complete the above two methods
    *  Add methods: (Don't forget to add them to the interface
    *   Get all products from the same category
    *   Get quantity of the product
    *   Get product list sorted by weight ascending/descending
    *   Get product list sorted by quantity ascending/descending
    *   Get product total price
    *   Get product list sorted by price ascending/descending
    *  Write unit tests for the above methods
    * */
}
