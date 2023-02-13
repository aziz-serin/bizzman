package com.bizzman.controllers;

import com.bizzman.dao.services.BusinessRelationshipService;
import com.bizzman.entities.BusinessRelationship;
import com.bizzman.exceptions.custom.EntityConstructionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(path = "/rest/businessRelationship", produces = { MediaType.APPLICATION_JSON_VALUE, MediaTypes.HAL_JSON_VALUE })
public class BusinessRelationshipController {
    private static final Logger logger = LoggerFactory.getLogger(BusinessRelationshipController.class);

    @Autowired
    private BusinessRelationshipService businessRelationshipService;

    @GetMapping("/getAll")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> getAll() {
        List<BusinessRelationship> businessRelationships = (List<BusinessRelationship>) businessRelationshipService.getAllRelationships();
        return ResponseEntity.ok().body(businessRelationships);
    }

    @GetMapping("/get/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> get(@PathVariable("id") @NotNull  long id) {
        Optional<BusinessRelationship> businessRelationship = businessRelationshipService.findById(id);
        if (businessRelationship.isEmpty()) {
            logger.info("Business Relationship {} is not found", id);
            return ResponseEntity.status(404).body("Could not find the request resource!");
        }
        return ResponseEntity.ok().body(businessRelationship.get());
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> create(@RequestBody @NotNull Map<String,?> body) {
        try {
            String name = (String) body.get("name");
            Map<String, String> contacts = (Map<String, String>) body.get("contacts");
            String type = (String) body.get("type");
            BusinessRelationship.Type bizzType;
            switch (type) {
                case "customer":
                    bizzType = BusinessRelationship.Type.CUSTOMER;
                    break;
                case "supplier":
                    bizzType = BusinessRelationship.Type.SUPPLIER;
                    break;
                default:
                    throw new EntityConstructionException("Could not construct entity BusinessRelationship");
            }
            if (name == null) {
                throw new EntityConstructionException("Could not construct entity BusinessRelationship");
            }
            BusinessRelationship businessRelationship = new BusinessRelationship();
            businessRelationship.setName(name);
            businessRelationship.setType(bizzType);
            businessRelationship.setContacts(contacts);
            BusinessRelationship created = businessRelationshipService.save(businessRelationship);
            return ResponseEntity.ok().body("Created Business Relationship with id " + created.getId());
        } catch (ClassCastException | EntityConstructionException e) {
            logger.error("Required fields are missing/malformed");
            return ResponseEntity.unprocessableEntity().body("Required fields are missing/malformed");
        }
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable("id") @NotNull  long id) {
        try {
            businessRelationshipService.deleteById(id);
            return ResponseEntity.ok().body("Deleted the requested resource");
        } catch (RuntimeException e) {
            logger.error("BusinessRelationship with id {} does not exist", id);
            return ResponseEntity.status(404).body("Could not delete the required resource");
        }
    }

    @DeleteMapping("/deleteAll")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> deleteAll() {
        businessRelationshipService.deleteAll();
        return ResponseEntity.ok().body("Deleted all resources");
    }





}
