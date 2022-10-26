package com.bizzman.dao;

import com.bizzman.dao.repos.ProductRepository;
import com.bizzman.dao.services.ProductService;
import com.bizzman.entities.Product;
import com.bizzman.entities.Supplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;

    @Override
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public long count(){
        return productRepository.count();
    }

    @Override
    public Iterable<Product> getAllProductsFromSameSupplier(Supplier supplier) {
        List<Product> productList = (List<Product>) productRepository.findAll();
        return productList.stream()
                .filter(o -> o.getSupplier().equals(supplier))
                .collect(Collectors.toList());
    }

    @Override
    public double getWeightOfAllProductsFromSameSupplier(Supplier supplier) {
        List<Product> productList = (List<Product>) productRepository.findAll();
        return productList.stream()
                .filter(o -> o.getSupplier().equals(supplier))
                .map(Product::getStockWeight)
                .reduce(0.0, Double::sum);
    }

    @Override
    public LocalDate getOldestDateOfItemEntry(Long id) {
        List<Product> productList = (List<Product>) productRepository.findAll();
        return Collections.max(productList.stream()
                .filter(o -> o.getId() == id)
                .map(Product::getArrivalDate)
                .collect(Collectors.toList()));
    }

    @Override
    public LocalDate getMostRecentDateOfItemEntry(Long id) {
        List<Product> productList = (List<Product>) productRepository.findAll();
        return Collections.min(productList.stream()
                .filter(o -> o.getId() == id)
                .map(Product::getArrivalDate)
                .collect(Collectors.toList()));
    }

    @Override
    public Iterable<Product> getAllProductsFromSameCategory(String category) {
        List<Product> productList = (List<Product>) productRepository.findAll();
        return productList.stream()
                .filter(o -> o.getCategory().equals(category))
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Product> getProductListSortedByWeight(boolean isAscending) {
        List<Product> productList = (List<Product>) productRepository.findAll();
        if (isAscending) {
            productList.sort(Comparator.comparingDouble(Product::getStockWeight));
        } else {
            productList.sort(Comparator.comparingDouble(Product::getStockWeight).reversed());
        }
        return productList;

    }

    @Override
    public double getProductTotalPrice(Product product) {
        return product.getUnitPrice() * product.getStockWeight();
    }

    @Override
    public double getTotalWeight(){
        List<Product> productList = (List<Product>) productRepository.findAll();
        return productList.stream()
                .map(Product::getStockWeight)
                .reduce(0.0, Double::sum);
    }

    @Override
    public double getTotalWeightOfCategory(String category) {
        List<Product> productList = (List<Product>) productRepository.findAll();
        return productList.stream()
                .filter(o -> o.getCategory().equals(category))
                .map(Product::getStockWeight)
                .reduce(0.0, Double::sum);
    }

    @Override
    public double getTotalPriceOfCategory(String category) {
        List<Product> productList = (List<Product>) productRepository.findAll();
        List<Product> categoryProducts = productList.stream()
                .filter(o -> o.getCategory().equals(category))
                .collect(Collectors.toList());
        double sum = 0;
        for (Product product : categoryProducts) {
            sum += product.getStockWeight() * product.getUnitPrice();
        }
        return sum;
    }

    @Override
    public double getTotalPrice(){
        List<Product> productList = (List<Product>) productRepository.findAll();
        double sum = 0;
        for (Product product : productList){
            sum += product.getStockWeight() * product.getUnitPrice();
        }
        return sum;
    }

    // stock price = unitPrice*quantity,
    @Override
    public Iterable<Product> getProductListSortedByStockPrice(boolean isAscending) {
        List<Product> productList = (List<Product>) productRepository.findAll();
        if (isAscending) {
            productList.sort((p1, p2) ->
                    (int) ((p1.getUnitPrice() * p1.getStockWeight() - p2.getUnitPrice() * p2.getStockWeight()) * 1000));
        } else {
            productList.sort((p1, p2) ->
                    (int) ((p2.getUnitPrice() * p2.getStockWeight() - p1.getUnitPrice() * p1.getStockWeight()) * 1000));
        }
        return productList;
    }

}
