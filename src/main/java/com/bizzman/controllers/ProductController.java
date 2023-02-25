package com.bizzman.controllers;


import com.bizzman.dao.services.BusinessRelationshipService;
import com.bizzman.dao.services.ProductService;
import com.bizzman.entities.BusinessRelationship;
import com.bizzman.entities.Product;
import com.bizzman.exceptions.custom.EntityConstructionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.bizzman.exceptions.ExceptionMessages.BAD_REQUEST_BODY;
import static com.bizzman.exceptions.ExceptionMessages.REQUESTED_ENTITY_NOT_FOUND;

@RestController
@RequestMapping(path = "rest/product", produces = { MediaType.APPLICATION_JSON_VALUE, MediaTypes.HAL_JSON_VALUE })
public class ProductController {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @Autowired
    private BusinessRelationshipService businessRelationshipService;

    @GetMapping("/getAll")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> getAll() {
        List<Product> products = (List<Product>) productService.getAllProducts();
        return ResponseEntity.ok().body(products);
    }

    @GetMapping("/get/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> get(@PathVariable("id") @NotNull long id) {
        Optional<Product> product = productService.getProductById(id);
        if (product.isPresent()) {
            return ResponseEntity.ok().body(product);
        }
        logger.debug("Product {} not found", id);
        return ResponseEntity.status(404).body(REQUESTED_ENTITY_NOT_FOUND);
    }

    @GetMapping("/getTotalWeight")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> getTotalWeight() {
        return ResponseEntity.ok().body(Map.of("weight", productService.getTotalWeight()));
    }

    @GetMapping("/getRecentEntryDate")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> getRecentEntryDate() {
        return ResponseEntity.ok().body(Map.of("date", productService.getMostRecentDateOfItemEntry().toString()));
    }

    @GetMapping("/getOldestEntryDate")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> getOldestEntryDate() {
        return ResponseEntity.ok().body(Map.of("date", productService.getOldestDateOfItemEntry().toString()));
    }

    @GetMapping("/getSuppliers")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> getSuppliers() {
        List<BusinessRelationship> businessRelationships = (List<BusinessRelationship>) productService.getAllProductSuppliers();
        return ResponseEntity.ok().body(businessRelationships);
    }

    @GetMapping("/getProductTotalSellingPrice/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> getProductTotalSellingPrice(@PathVariable("id") @NotNull long id) {
        Optional<Product> product = productService.getProductById(id);
        if (product.isPresent()) {
            return ResponseEntity.ok()
                    .body(Map.of("price", productService.getProductTotalSellingPrice(product.get())));
        }
        logger.debug("Product {} not found", id);
        return ResponseEntity.status(404).body(REQUESTED_ENTITY_NOT_FOUND);
    }

    @GetMapping("/getProductTotalEntryPrice/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> getProductTotalEntryPrice(@PathVariable("id") @NotNull long id) {
        Optional<Product> product = productService.getProductById(id);
        if (product.isPresent()) {
            return ResponseEntity.ok()
                    .body(Map.of("price", productService.getProductTotalEntryPrice(product.get())));
        }
        logger.debug("Product {} not found", id);
        return ResponseEntity.status(404).body(REQUESTED_ENTITY_NOT_FOUND);
    }

    @GetMapping("/getExpectedProfit/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> getExpectedProfitFromProduct(@PathVariable("id") @NotNull long id) {
        Optional<Product> product = productService.getProductById(id);
        if (product.isPresent()) {
            return ResponseEntity.ok()
                    .body(Map.of("price", productService.getExpectedProfitFromProduct(product.get())));
        }
        logger.debug("Product {} not found!", id);
        return ResponseEntity.status(404).body(REQUESTED_ENTITY_NOT_FOUND);
    }

    @GetMapping("/getAllFromSameSupplier/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> getAllFromSameSupplier(@PathVariable @NotNull long id) {
        Optional<BusinessRelationship> businessRelationship = businessRelationshipService.findById(id);
        if (businessRelationship.isPresent()) {
            return ResponseEntity.ok(productService.getAllProductsFromSameSupplier(id));
        }
        logger.debug("Business Relationship {} not found", id);
        return ResponseEntity.status(404).body(REQUESTED_ENTITY_NOT_FOUND);
    }

    @GetMapping("/getAllWeightFromSameSupplier/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> getAllWeightFromSameSupplier(@PathVariable @NotNull long id) {
        Optional<BusinessRelationship> businessRelationship = businessRelationshipService.findById(id);
        if (businessRelationship.isPresent()) {
            return ResponseEntity.ok(Map.of("weight", productService.getWeightOfAllProductsFromSameSupplier(id)));
        }
        logger.debug("Business Relationship {} not found", id);
        return ResponseEntity.status(404).body(REQUESTED_ENTITY_NOT_FOUND);
    }

    @PostMapping("/getProductByCategory")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> getProductByCategory(@RequestBody Map<String, ?> body) {
        try {
            Optional<Product.ProductCategory> productCategory = getProductCategory(body);
            if (productCategory.isPresent()) {
                List<Product> products = (List<Product>) productService
                        .getAllProductsFromSameCategory(productCategory.get());
                return ResponseEntity.ok().body(products);
            } else {
                throw new EntityConstructionException("Could not construct entity");
            }
        } catch (EntityConstructionException e) {
            logger.debug("Invalid body, \n {}", body.toString());
            return ResponseEntity.badRequest().body(BAD_REQUEST_BODY);
        }
    }

    @PostMapping("/getTotalPriceOfCategory")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> getTotalPriceOfCategory(@RequestBody Map<String, ?> body) {
        try {
            Optional<Product.ProductCategory> productCategory = getProductCategory(body);
            if (productCategory.isPresent()) {
                return ResponseEntity.ok().body(Map.of("price", productService
                        .getTotalPriceOfCategory(productCategory.get())));
            } else {
                throw new EntityConstructionException("Could not construct entity");
            }
        } catch (EntityConstructionException e) {
            logger.debug("Invalid body, \n {}", body.toString());
            return ResponseEntity.badRequest().body(BAD_REQUEST_BODY);
        }
    }

    @PostMapping("/getTotalWeightOfCategory")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> getTotalWeightOfCategory(@RequestBody Map<String, ?> body) {
        try {
            Optional<Product.ProductCategory> productCategory = getProductCategory(body);
            if (productCategory.isPresent()) {
                return ResponseEntity.ok().body(Map.of("weight", productService
                        .getTotalWeightOfCategory(productCategory.get())));
            } else {
                throw new EntityConstructionException("Could not construct entity");
            }
        } catch (EntityConstructionException e) {
            logger.debug("Invalid body, \n {}", body);
            return ResponseEntity.badRequest().body(BAD_REQUEST_BODY);
        }
    }

    @GetMapping("/getTotalPrice")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> getTotalPrice(@RequestParam(required = false, name = "isSelling") String isSelling) {
        boolean selling = Boolean.parseBoolean(isSelling);
        return ResponseEntity.ok().body(Map.of("price", productService.getTotalPrice(selling)));
    }

    @GetMapping("/getAllSortedByWeight")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> getSortedByWeight(@RequestParam(required = false, name = "isAscending") String isAscending) {
        boolean isAsc = Boolean.parseBoolean(isAscending);
        List<Product> products = (List<Product>) productService.getProductListSortedByWeight(isAsc);
        return ResponseEntity.ok().body(products);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable @NotNull long id) {
        try {
            productService.deleteById(id);
            return ResponseEntity.ok().body("Deleted the requested resource");
        } catch (EmptyResultDataAccessException e) {
            logger.error("Product with id {} does not exist", id);
            return ResponseEntity.status(404).body(REQUESTED_ENTITY_NOT_FOUND);
        }
    }

    @DeleteMapping("/deleteAll")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> deleteAll() {
        productService.deleteAll();
        return ResponseEntity.ok().body("Deleted all resources");
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> create(@RequestBody Map<String, ?> body) {
        try{
            Optional<Product.ProductCategory> productCategory = getProductCategory(body);
            if (productCategory.isEmpty()) {
                throw new EntityConstructionException("Could not parse the request body!");
            }
            double entryUnitPrice = Double.parseDouble((String) body.get("entryUnitPrice"));
            double sellingUnitPrice = Double.parseDouble((String) body.get("sellingUnitPrice"));
            double stockWeight = Double.parseDouble((String) body.get("stockWeight"));
            long quantity = Long.parseLong((String) body.get("quantity"));
            String imagePath = (String) body.get("imagePath");

            LocalDate arrival = LocalDate.parse((String) body.get("arrivalDate"));
            long businessRelationshipId = Long.parseLong((String) body.get("businessRelationship"));
            Optional<BusinessRelationship> businessRelationship
                    = businessRelationshipService.findById(businessRelationshipId);
            if (businessRelationship.isEmpty()) {
                throw new EntityConstructionException("Could not parse the request body!");
            }
            Product product = new Product();
            product.setCategory(productCategory.get());
            product.setArrivalDate(arrival);
            product.setSupplier(businessRelationship.get());
            product.setQuantity(quantity);
            product.setStockWeight(stockWeight);
            product.setSellingUnitPrice(sellingUnitPrice);
            product.setEntryUnitPrice(entryUnitPrice);
            product.setImagePath(imagePath);

            Product saved = productService.save(product);
            return ResponseEntity.ok().body("Created product with id " + saved.getId());

        }
        catch (EntityConstructionException | ClassCastException | NumberFormatException | DateTimeParseException e) {
            logger.debug("Failed to construct a new product entity with given request body, \n {}", body.toString());
            return ResponseEntity.unprocessableEntity().body(BAD_REQUEST_BODY);
        }
    }

    private Optional<Product.ProductCategory> getProductCategory(Map<String, ?> body) {
        try {
            String category = (String) body.get("category");
            switch (category.toLowerCase()) {
                case "price_by_weight":
                    return Optional.of(Product.ProductCategory.PRICE_BY_WEIGHT);
                case "price_by_quantity":
                    return Optional.of(Product.ProductCategory.PRICE_BY_QUANTITY);
                default:
                    return Optional.empty();
            }
        } catch (ClassCastException | NullPointerException e) {
            logger.debug("Could not parse the body, \n {}", body.toString());
            return Optional.empty();
        }
    }
}