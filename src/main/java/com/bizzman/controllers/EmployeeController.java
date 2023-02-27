package com.bizzman.controllers;


import com.bizzman.dao.services.employee.EmergencyContactService;
import com.bizzman.dao.services.employee.EmployeeService;
import com.bizzman.dao.services.employee.PersonalDetailsService;
import com.bizzman.entities.employee.EmergencyContact;
import com.bizzman.entities.employee.Employee;
import com.bizzman.entities.employee.PersonalDetails;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.bizzman.exceptions.ExceptionMessages.BAD_REQUEST_BODY;
import static com.bizzman.exceptions.ExceptionMessages.REQUESTED_ENTITY_NOT_FOUND;

@RestController
@RequestMapping(path = "rest/employee",  produces = { MediaType.APPLICATION_JSON_VALUE, MediaTypes.HAL_JSON_VALUE })
public class EmployeeController {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmergencyContactService emergencyContactService;

    @Autowired
    private PersonalDetailsService personalDetailsService;

    @GetMapping("/getAll")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok().body(employeeService.findAllEmployees());
    }

    @GetMapping("/get/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> get(@PathVariable("id") @NotNull long id) {
        Optional<Employee> employee = employeeService.findEmployeeById(id);
        if (employee.isEmpty()) {
            logger.debug("Employee {} does not exist!", id);
            return ResponseEntity.status(404).body(REQUESTED_ENTITY_NOT_FOUND);
        }
        return ResponseEntity.ok().body(employee.get());
    }

    @GetMapping("/getEmergencyContacts/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> getEmergencyContacts(@PathVariable("id") @NotNull long id) {
        Optional<Employee> employee = employeeService.findEmployeeById(id);
        if (employee.isEmpty()) {
            logger.debug("Employee {} does not exist!", id);
            return ResponseEntity.status(404).body(REQUESTED_ENTITY_NOT_FOUND);
        }
        Iterable<EmergencyContact> emergencyContacts = employeeService
                .getEmployeeEmergencyContactDetails(employee.get());
        return ResponseEntity.ok().body(emergencyContacts);
    }

    @GetMapping("/getPersonalDetails/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> getPersonalDetails(@PathVariable("id") @NotNull long id) {
        Optional<Employee> employee = employeeService.findEmployeeById(id);
        if (employee.isEmpty()) {
            logger.debug("Employee {} does not exist!", id);
            return ResponseEntity.status(404).body(REQUESTED_ENTITY_NOT_FOUND);
        }
        PersonalDetails personalDetails = employee.get().getPersonalDetails();
        return ResponseEntity.ok().body(personalDetails);
    }

    @GetMapping("/getAllSortedBySalary")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> getAllSortedBySalary(@RequestParam(required = false, name = "isAscending")
                                                      String isAscending) {
        boolean ascending = Boolean.parseBoolean(isAscending);
        if (ascending) {
            return ResponseEntity.ok().body(employeeService.getAllEmployeeSortedBySalaryAscending());
        }
        return ResponseEntity.ok().body(employeeService.getAllEmployeeSortedBySalaryDescending());
    }

    @GetMapping("/getAllSortedByJoiningDate")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> getAllSortedByJoiningDate(@RequestParam(required = false, name = "isAscending")
                                                  String isAscending) {
        boolean ascending = Boolean.parseBoolean(isAscending);
        if (ascending) {
            return ResponseEntity.ok().body(employeeService.getAllEmployeeSortedByJoiningDateAscending());
        }
        return ResponseEntity.ok().body(employeeService.getAllEmployeeSortedByJoiningDateDescending());
    }

    @GetMapping("/getAllSortedByAge")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> getAllSortedByAge(@RequestParam(required = false, name = "isAscending")
                                                  String isAscending) {
        boolean ascending = Boolean.parseBoolean(isAscending);
        if (ascending) {
            return ResponseEntity.ok().body(employeeService.getAllEmployeeSortedByAgeAscending());
        }
        return ResponseEntity.ok().body(employeeService.getAllEmployeeSortedByAgeDescending());
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable("id") @NotNull long id) {
        try {
            employeeService.delete(id);
            return ResponseEntity.ok().body("Deleted the requested resource");
        } catch (EmptyResultDataAccessException e) {
            logger.error("Employee with id {} does not exist", id);
            return ResponseEntity.status(404).body(REQUESTED_ENTITY_NOT_FOUND);
        }
    }

    @DeleteMapping("/deleteAll")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> deleteAll() {
        employeeService.deleteAll();
        return ResponseEntity.ok().body("Deleted requested resources");
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> create(@RequestBody @NotNull Map<String, ?> body) {
        try {
            String name = (String) body.get("name");
            String workEmail = (String) body.get("email");
            String nationalInsurance = (String) body.get("nationalInsurance");
            double salary = Double.parseDouble((String) body.get("salary"));
            long otherExpenses = Long.parseLong((String) body.get("otherExpenses"));
            LocalDate joiningDate = LocalDate.parse((String) body.get("joiningDate"));
            if (name == null || workEmail == null || nationalInsurance == null || joiningDate == null) {
                throw new EntityConstructionException("Could not parse the body, \n " + body.toString());
            }
            PersonalDetails personalDetails = constructPersonalDetails((Map<String, ?>) body.get("personalDetails"));
            List<EmergencyContact> emergencyContacts = constructEmergencyContacts((List<Map<String, ?>>) body.get("emergencyContact"));
            personalDetailsService.save(personalDetails);
            emergencyContacts.forEach(
                    emergencyContact -> emergencyContactService.save(emergencyContact)
            );
            Employee employee = new Employee();
            employee.setName(name);
            employee.setWorkEmail(workEmail);
            employee.setNationalInsurance(nationalInsurance);
            employee.setSalary(salary);
            employee.setOtherExpenses(otherExpenses);
            employee.setJoiningDate(joiningDate);
            employee.setPersonalDetails(personalDetails);
            employee.setEmergencyContactDetails(emergencyContacts);
            Employee saved = employeeService.save(employee);
            return ResponseEntity.ok().body("Created employee with id " + saved.getId());
        } catch (NumberFormatException | DateTimeParseException | ClassCastException | NullPointerException e) {
            logger.debug("Could not parse the body, \n {}", body.toString());
            return ResponseEntity.unprocessableEntity().body(BAD_REQUEST_BODY);
        } catch (EntityConstructionException e) {
            logger.debug(e.getMessage());
            return ResponseEntity.unprocessableEntity().body(BAD_REQUEST_BODY);
        }
    }

    private PersonalDetails constructPersonalDetails(Map<String, ?> personalDetailsJson) {
        try {
            String passportNumber = (String) personalDetailsJson.get("passportNumber");
            String phoneNumber = (String) personalDetailsJson.get("phoneNumber");
            String address = (String) personalDetailsJson.get("address");
            String personalEmail = (String) personalDetailsJson.get("personalEmail");
            LocalDate birthDate = LocalDate.parse((CharSequence) personalDetailsJson.get("birthDate"));
            if (passportNumber == null || phoneNumber == null ||
                    address == null || personalEmail == null || birthDate == null) {
                throw new EntityConstructionException("Could not parse the body, \n " + personalDetailsJson.toString());
            }
            PersonalDetails personalDetails = new PersonalDetails();
            personalDetails.setPersonalEmail(personalEmail);
            personalDetails.setAddress(address);
            personalDetails.setPhoneNumber(phoneNumber);
            personalDetails.setPassportNumber(passportNumber);
            personalDetails.setBirthDate(birthDate);
            return personalDetails;
        } catch (NumberFormatException | DateTimeParseException | ClassCastException | NullPointerException e) {
            throw new EntityConstructionException("Could not parse the body, \n " + personalDetailsJson.toString());
        }
    }

    private List<EmergencyContact> constructEmergencyContacts(List<Map<String, ?>> emergencyContactJson) {
        try {
            List<EmergencyContact> emergencyContacts = new ArrayList<>();

            emergencyContactJson.forEach(
                    emergencyContact -> {
                        String phoneNumber = (String) emergencyContact.get("phoneNumber");
                        String name = (String) emergencyContact.get("name");
                        if (name == null || phoneNumber == null ) {
                            throw new EntityConstructionException("Could not parse the body, \n " + emergencyContactJson.toString());
                        }
                        EmergencyContact.Relationship relationship =
                                getRelationship((String) emergencyContact.get("relationship"));
                        EmergencyContact emergencyContact_ = new EmergencyContact();
                        emergencyContact_.setName(name);
                        emergencyContact_.setRelationship(relationship);
                        emergencyContact_.setPhoneNumber(phoneNumber);
                        emergencyContacts.add(emergencyContact_);
                    }
            );
            return emergencyContacts;
        } catch (NumberFormatException | ClassCastException | NullPointerException e) {
            throw new EntityConstructionException("Could not parse the body, \n " + emergencyContactJson.toString());
        }
    }

    private EmergencyContact.Relationship getRelationship(String relationship) {
        switch (relationship.toLowerCase()) {
            case "friend":
                return EmergencyContact.Relationship.FRIEND;
            case "immediate_family":
                return EmergencyContact.Relationship.IMMEDIATE_FAMILY;
            case  "family":
                return EmergencyContact.Relationship.FAMILY;
            case "partner":
                return EmergencyContact.Relationship.PARTNER;
            case "other":
                return EmergencyContact.Relationship.OTHER;
            default:
                throw new EntityConstructionException("Could not parse the relationship: " + relationship);
        }
    }

}
