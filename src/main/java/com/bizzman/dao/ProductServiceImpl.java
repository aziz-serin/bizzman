package com.bizzman.dao;

import com.bizzman.dao.repos.ProductRepository;
import com.bizzman.dao.services.ProductService;
import com.bizzman.entities.Product;
import com.bizzman.entities.BusinessRelationship;
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
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        productRepository.deleteAll();
    }

    @Override
    public void deleteAll(Iterable<Product> products) {
        productRepository.deleteAll(products);
    }

    @Override
    public long count(){
        return productRepository.count();
    }

    @Override
    public Iterable<Product> getAllProductsFromSameSupplier(long businessRelationshipId) {
        List<Product> productList = (List<Product>) productRepository.findAll();
        return productList.stream()
                .filter(o -> o.getSupplier().getId() == businessRelationshipId)
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Iterable<BusinessRelationship> getAllProductSuppliers() {
        List<Product> products = (List<Product>) getAllProducts();
        return products.stream().map(Product::getSupplier).collect(Collectors.toList());
    }

    @Override
    public double getWeightOfAllProductsFromSameSupplier(long businessRelationshipId) {
        List<Product> productList = (List<Product>) productRepository.findAll();
        return productList.stream()
                .filter(o -> o.getSupplier().getId() == businessRelationshipId)
                .map(Product::getStockWeight)
                .reduce(0.0, Double::sum);
    }

    @Override
    public LocalDate getOldestDateOfItemEntry() {
        List<Product> productList = (List<Product>) productRepository.findAll();
        return Collections.min(productList.stream()
                .map(Product::getArrivalDate)
                .collect(Collectors.toList()));
    }

    @Override
    public LocalDate getMostRecentDateOfItemEntry() {
        List<Product> productList = (List<Product>) productRepository.findAll();
        return Collections.max(productList.stream()
                .map(Product::getArrivalDate)
                .collect(Collectors.toList()));
    }

    @Override
    public Iterable<Product> getAllProductsFromSameCategory(Product.ProductCategory category) {
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
    public double getProductTotalSellingPrice(Product product) {
        if(product.getCategory().equals(Product.ProductCategory.PRICE_BY_WEIGHT)) {
            return product.getSellingUnitPrice() * product.getStockWeight();
        } else {
            return product.getSellingUnitPrice() * product.getQuantity();
        }
    }

    @Override
    public double getProductTotalEntryPrice(Product product) {
        if(product.getCategory().equals(Product.ProductCategory.PRICE_BY_WEIGHT)) {
            return product.getEntryUnitPrice() * product.getStockWeight();
        } else {
            return product.getEntryUnitPrice() * product.getQuantity();
        }
    }

    @Override
    public double getTotalWeight(){
        List<Product> productList = (List<Product>) productRepository.findAll();
        return productList.stream()
                .map(Product::getStockWeight)
                .reduce(0.0, Double::sum);
    }

    @Override
    public double getExpectedProfitFromProduct(Product product) {
        return getProductTotalSellingPrice(product) - getProductTotalEntryPrice(product);
    }

    @Override
    public double getTotalWeightOfCategory(Product.ProductCategory category) {
        List<Product> productList = (List<Product>) productRepository.findAll();
        return productList.stream()
                .filter(o -> o.getCategory().equals(category))
                .map(Product::getStockWeight)
                .reduce(0.0, Double::sum);
    }

    @Override
    public double getTotalPriceOfCategory(Product.ProductCategory category) {
        List<Product> productList = (List<Product>) productRepository.findAll();
        List<Product> categoryProducts = productList.stream()
                .filter(o -> o.getCategory().equals(category))
                .collect(Collectors.toList());
        double sum = 0;
        for (Product product : categoryProducts) {
            if (product.getCategory().equals(Product.ProductCategory.PRICE_BY_WEIGHT)) {
                sum += product.getStockWeight() * product.getSellingUnitPrice();
            } else {
                sum += product.getQuantity() * product.getSellingUnitPrice();
            }
        }
        return sum;
    }

    @Override
    public double getTotalPrice(boolean isSelling){
        List<Product> productList = (List<Product>) productRepository.findAll();
        double sum = 0;
        for (Product product : productList) {
            if (product.getCategory().equals(Product.ProductCategory.PRICE_BY_WEIGHT) && isSelling) {
                sum += product.getStockWeight() * product.getSellingUnitPrice();
            }
            else if (product.getCategory().equals(Product.ProductCategory.PRICE_BY_WEIGHT) && !isSelling) {
                sum += product.getStockWeight() * product.getEntryUnitPrice();
            }
            else if (product.getCategory().equals(Product.ProductCategory.PRICE_BY_QUANTITY) && isSelling) {
                sum += product.getQuantity() * product.getSellingUnitPrice();
            }
            else if (product.getCategory().equals(Product.ProductCategory.PRICE_BY_QUANTITY) && !isSelling) {
                sum += product.getQuantity() * product.getEntryUnitPrice();
            }
        }
        return sum;
    }
}
