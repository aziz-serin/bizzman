package com.bizzman.controllers;

import static com.bizzman.exceptions.ExceptionMessages.BAD_REQUEST_BODY;
import static com.bizzman.exceptions.ExceptionMessages.REQUESTED_ENTITY_NOT_FOUND;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bizzman.dao.services.BusinessRelationshipService;
import com.bizzman.dao.services.OrderService;
import com.bizzman.dao.services.ProductService;
import com.bizzman.entities.BusinessRelationship;
import com.bizzman.entities.Order;
import com.bizzman.entities.Product;
import com.bizzman.exceptions.custom.EntityConstructionException;

@RestController
@RequestMapping(path = "rest/order",  produces = { MediaType.APPLICATION_JSON_VALUE, MediaTypes.HAL_JSON_VALUE })
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;
    @Autowired
    private BusinessRelationshipService businessRelationshipService;
    @Autowired
    private ProductService productService;


    @GetMapping("/getAll")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> getAll() {
        List<Order> orders = (List<Order>) orderService.getAllOrders();
        return ResponseEntity.ok().body(orders);
    }

    @GetMapping("/get/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> get(@PathVariable("id") @NotNull long id) {
        Optional<Order> order = orderService.findById(id);
        if (order.isEmpty()) {
            logger.debug("Order {} does not exist", id);
            return ResponseEntity.status(404).body(REQUESTED_ENTITY_NOT_FOUND);
        }
        return ResponseEntity.ok().body(order.get());
    }

    @PostMapping("/getAllWithSameType")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> getAllOrdersWithSameType(@RequestBody @NotNull Map<String,?> body) {
        Optional<Order.Type> orderType = getOrderType(body);
        if (orderType.isEmpty()) {
            logger.debug("Given type does not exist");
            return ResponseEntity.badRequest().body(BAD_REQUEST_BODY);
        }
        List<Order> orders = (List<Order>) orderService.getAllOrdersWithSameType(orderType.get());
        return ResponseEntity.ok().body(orders);
    }

    @GetMapping("/getPrice/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> getOrderPrice(@PathVariable("id") @NotNull long id) {
        Optional<Order> order = orderService.findById(id);
        if (order.isEmpty()) {
            logger.debug("Order {} does not exist", id);
            return ResponseEntity.status(404).body(REQUESTED_ENTITY_NOT_FOUND);
        }
        return ResponseEntity.ok().body(
                Map.of(
                       "price", orderService.getOrderPrice(order.get())
                )
        );
    }

    @PostMapping("/getAllSortedByArrival")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> getAllSortedByArrival(@RequestBody @NotNull Map<String,?> body) {
        boolean isAscending;
        try {
            isAscending = (Boolean) body.get("isAscending");
        } catch (ClassCastException | NullPointerException e) {
            logger.error("Could not cast the given request body to boolean!");
            return ResponseEntity.badRequest().body(BAD_REQUEST_BODY);
        }
        List<Order> orders = (List<Order>) orderService.getAllOrdersSortedByArrivalDate(isAscending);
        return ResponseEntity.ok().body(orders);
    }

    @GetMapping("/getAllForBusinessRelationship/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> getAllForBusinessRelationship(@PathVariable("id") @NotNull long id) {
        Optional<BusinessRelationship> businessRelationship = businessRelationshipService.findById(id);
        if (businessRelationship.isEmpty()) {
            logger.debug("Requested businessRelationship {} does not exist", id);
            return ResponseEntity.status(404).body(REQUESTED_ENTITY_NOT_FOUND);
        }
        List<Order> orders = (List<Order>) orderService.getAllOrdersForBusinessRelationship(id);
        return ResponseEntity.ok().body(orders);
    }

    @PostMapping("/getAllWithTypeSortedByArrival")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> getAllWithTypeSortedByArrival(@RequestBody @NotNull Map<String,?> body) {
        Optional<Order.Type> orderType = getOrderType(body);
        if (orderType.isEmpty()) {
            logger.debug("Given type does not exist");
            return ResponseEntity.badRequest().body(BAD_REQUEST_BODY);
        }
        boolean isAscending;
        try {
            isAscending = (Boolean) body.get("isAscending");
        } catch (ClassCastException | NullPointerException e) {
            logger.error("Could not cast the given request body to boolean!");
            return ResponseEntity.badRequest().body(BAD_REQUEST_BODY);
        }
        List<Order> orders = (List<Order>) orderService
                .getAllOrdersSameTypeSortedByArrivalDate(orderType.get(), isAscending);
        return ResponseEntity.ok().body(orders);
    }

    @PostMapping("/getAllWithTypeSortedByPrice")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> getAllWithTypeSortedByPrice(@RequestBody @NotNull Map<String,?> body) {
        Optional<Order.Type> orderType = getOrderType(body);
        if (orderType.isEmpty()) {
            logger.debug("Given type does not exist");
            return ResponseEntity.badRequest().body(BAD_REQUEST_BODY);
        }
        boolean isAscending;
        try {
            isAscending = (Boolean) body.get("isAscending");
        } catch (ClassCastException | NullPointerException e) {
            logger.error("Could not cast the given request body to boolean!");
            return ResponseEntity.badRequest().body(BAD_REQUEST_BODY);
        }
        List<Order> orders = (List<Order>) orderService
                .getAllOrdersSameTypeSortedByPrice(orderType.get(), isAscending);
        return ResponseEntity.ok().body(orders);
    }

    @DeleteMapping("/deleteById/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable("id") @NotNull long id) {
        try {
            orderService.deleteById(id);
            return ResponseEntity.ok().body("Deleted the requested resource");
        } catch (EmptyResultDataAccessException e) {
            logger.error("Order with id {} does not exist", id);
            return ResponseEntity.status(404).body(REQUESTED_ENTITY_NOT_FOUND);
        }
    }

    @DeleteMapping("/deleteAll")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> deleteAll() {
        orderService.deleteAll();
        return ResponseEntity.ok().body("Deleted all resources");
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> create(@RequestBody @NotNull Map<String,?> body) {
        try {
            Optional<Order.Type> orderType = getOrderType(body);
            if (orderType.isEmpty()) {
                logger.debug("Given type does not exist");
                throw new EntityConstructionException("Given type does not exist");
            }

            long businessId = Long.parseLong((String) body.get("businessRelationship"));
            Optional<BusinessRelationship> businessRelationship = businessRelationshipService.findById(businessId);
            if (businessRelationship.isEmpty()) {
                logger.debug("Requested businessRelationship {} does not exist", businessId);
                throw new EntityConstructionException("Given businessRelationship does not exist");
            }

            List<Integer> productIdsInt = (List<Integer>) body.get("products");
            List<Long> productIdsLong =
                    productIdsInt.stream().map(Integer::longValue).collect(Collectors.toList());

            List<Product> products = getProductsFromIds(productIdsLong);

            LocalDate arrival = LocalDate.parse((String) body.get("arrivalTime"));
            LocalDate placing = LocalDate.parse((String) body.get("placingDate"));

            Order order = new Order();
            order.setType(orderType.get());
            order.setArrivalDate(arrival);
            order.setPlacingDate(placing);
            order.setBusinessRelationship(businessRelationship.get());
            order.setProducts(products);

            Order saved = orderService.save(order);
            return ResponseEntity.ok().body("Created order with id " + saved.getId());

        } catch (ClassCastException | EntityConstructionException | NumberFormatException | NullPointerException |
                 DateTimeParseException e) {
            logger.error("Required fields are missing/malformed");
            return ResponseEntity.unprocessableEntity().body(BAD_REQUEST_BODY);
        }
    }

    private List<Product> getProductsFromIds(List<Long> ids) {
        List<Product> products = ids.stream()
                .map(id -> productService.getProductById(id))
                .map(product -> product.orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        if (products.size() != ids.size()) {
            throw new EntityConstructionException("Some of the products do not exist!");
        }
        return products;
    }

    private Optional<Order.Type> getOrderType(Map<String, ?> body) {
        if (body.get("type") == null) {
            logger.debug("Could not find the required parameter \"type\"");
            return Optional.empty();
        }
        String type = ((String) body.get("type")).toLowerCase();
        Optional<Order.Type> orderType;
        switch (type) {
        case "outgoing":
            orderType = Optional.of(Order.Type.OUTGOING);
            break;
        case "incoming":
            orderType = Optional.of(Order.Type.INCOMING);
            break;
        default:
            orderType = Optional.empty();
        }
        return orderType;
    }
}