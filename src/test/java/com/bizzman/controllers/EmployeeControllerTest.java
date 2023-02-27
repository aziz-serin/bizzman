package com.bizzman.controllers;

import com.bizzman.BizzmanApplication;
import com.bizzman.dao.services.employee.EmployeeService;
import com.bizzman.entities.employee.EmergencyContact;
import com.bizzman.entities.employee.Employee;
import com.bizzman.entities.employee.PersonalDetails;
import com.bizzman.utils.TestTokenObtain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = BizzmanApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
@ActiveProfiles("test")
public class EmployeeControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private EmployeeService employeeService;

    private WebTestClient client;

    @BeforeEach
    public void setup() {
        client = WebTestClient.bindToServer().baseUrl("http://localhost:" + port).build();
    }

    @Test
    public void shouldGetAllReturnAll() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);

        client.get().uri("/rest/employee/getAll")
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(List.class)
                .consumeWith(response -> assertThat(response.getResponseBody())
                        .hasSameSizeAs(employeeService.findAllEmployees()));
    }

    @Test
    public void shouldGetFailGivenEmployeeDoesntExist() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);

        client.get().uri("/rest/employee/get/" + 9999999)
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .is4xxClientError();
    }

    @Test
    public void shouldGetSucceed() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        List<Employee> employees = (List<Employee>) employeeService.findAllEmployees();
        client.get().uri("/rest/employee/get/" + employees.get(0).getId())
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    public void shouldGetEmergencyContactsFailGivenEmployeeDoesntExist() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);

        client.get().uri("/rest/employee/getEmergencyContacts/" + 9999999)
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .is4xxClientError();
    }

    @Test
    public void shouldGetEmergencyContactsSucceed() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        List<Employee> employees = (List<Employee>) employeeService.findAllEmployees();
        List<EmergencyContact> emergencyContacts
                = (List<EmergencyContact>) employeeService.getEmployeeEmergencyContactDetails(employees.get(0));

        client.get().uri("/rest/employee/getEmergencyContacts/" + employees.get(0).getId())
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(List.class)
                .consumeWith(response -> {
                    assertThat(response.getResponseBody()).hasSameSizeAs(emergencyContacts);
                });
    }

    @Test
    public void shouldGetPersonalDetailsFailGivenEmployeeDoesntExist() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);

        client.get().uri("/rest/employee/getPersonalDetails/" + 9999999)
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .is4xxClientError();
    }

    @Test
    public void shouldGetPersonalDetailsSucceed() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        List<Employee> employees = (List<Employee>) employeeService.findAllEmployees();
        PersonalDetails personalDetails = employeeService.getEmployeePersonalDetails(employees.get(0));
        client.get().uri("/rest/employee/getPersonalDetails/" + employees.get(0).getId())
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(PersonalDetails.class)
                .consumeWith(response -> {
                    assertThat(response.getResponseBody().getId()).isEqualTo(personalDetails.getId());
                });
    }

    @Test
    public void shouldGetAllSortedByJoiningDateSucceedDescending() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);

        client.get().uri("/rest/employee/getAllSortedByJoiningDate")
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    public void shouldGetAllSortedByJoiningDateSucceedAscending() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);

        client.get().uri("/rest/employee/getAllSortedByJoiningDate?isAscending=true")
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    public void shouldGetAllSortedBySalarySucceedDescending() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);

        client.get().uri("/rest/employee/getAllSortedBySalary")
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    public void shouldGetAllSortedBySalarySucceedAscending() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);

        client.get().uri("/rest/employee/getAllSortedBySalary?isAscending=true")
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    public void shouldGetAllSortedByAgeSucceedDescending() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);

        client.get().uri("/rest/employee/getAllSortedByAge")
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    public void shouldGetAllSortedByAgeSucceedAscending() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);

        client.get().uri("/rest/employee/getAllSortedByAge?isAscending=true")
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    public void shouldDeleteFailGivenEmployeeDoesntExist() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);

        client.delete().uri("/rest/employee/delete/" + 99999)
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .is4xxClientError();
    }

    @Test
    public void shouldDeleteFailGivenEmployeeIsReferencedByOtherEntities() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        List<Employee> employees = (List<Employee>) employeeService.findAllEmployees();

        client.delete().uri("/rest/employee/delete/" + employees.get(0).getId())
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .is5xxServerError();
    }

    @Test
    public void shouldCreateFailGivenErrorInEmergencyContact() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        Map<String, ?> personalDetailsBody = Map.of(
                "passportNumber", "83190312",
                "phoneNumber", "90328109",
                "address", "test",
                "personalEmail", "test",
                "birthDate", LocalDate.now().toString()
        );
        Map<String, ?> emergencyContacts = Map.of(
                "name", "test",
                "relationship", "other"
        );
        Map<String, ?> employeeDetails = Map.of(
                "name", "name",
                "email", "email",
                "nationalInsurance", "nationalInsurance",
                "salary", "65",
                "otherExpenses", "38291",
                "joiningDate", LocalDate.now().toString(),
                "personalDetails", personalDetailsBody,
                "emergencyContact", List.of(emergencyContacts)
        );
        long count = employeeService.count();

        client.post().uri("/rest/employee/create")
                .header("Authorization", "Bearer " + token)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(employeeDetails))
                .exchange()
                .expectStatus()
                .is4xxClientError();

        assertThat(count).isEqualTo(employeeService.count());
    }

    @Test
    public void shouldCreateFailGivenErrorInPersonalDetails() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        Map<String, ?> personalDetailsBody = Map.of(
                "passportNumber", "83190312",
                "phoneNum", "90328109",
                "address", "test",
                "personalEmail", "test",
                "birthDate", LocalDate.now().toString()
        );
        Map<String, ?> emergencyContacts = Map.of(
                "name", "test",
                "phoneNumber", "839201832",
                "relationship", "other"
        );
        Map<String, ?> employeeDetails = Map.of(
                "name", "name",
                "email", "email",
                "nationalInsurance", "nationalInsurance",
                "salary", "65",
                "otherExpenses", "38291",
                "joiningDate", LocalDate.now().toString(),
                "personalDetails", personalDetailsBody,
                "emergencyContact", List.of(emergencyContacts)
        );
        long count = employeeService.count();

        client.post().uri("/rest/employee/create")
                .header("Authorization", "Bearer " + token)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(employeeDetails))
                .exchange()
                .expectStatus()
                .is4xxClientError();

        assertThat(count).isEqualTo(employeeService.count());
    }

    @Test
    public void shouldCreateSucceed() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        Map<String, ?> personalDetailsBody = Map.of(
                "passportNumber", "83190312",
                "phoneNumber", "90328109",
                "address", "test",
                "personalEmail", "test",
                "birthDate", LocalDate.now().toString()
        );
        Map<String, ?> emergencyContacts = Map.of(
                "name", "test",
                "phoneNumber", "839201832",
                "relationship", "other"
        );
        Map<String, ?> employeeDetails = Map.of(
                "name", "name",
                "email", "email",
                "nationalInsurance", "nationalInsurance",
                "salary", "65",
                "otherExpenses", "38291",
                "joiningDate", LocalDate.now().toString(),
                "personalDetails", personalDetailsBody,
                "emergencyContact", List.of(emergencyContacts)
        );
        long count = employeeService.count();

        client.post().uri("/rest/employee/create")
                .header("Authorization", "Bearer " + token)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(employeeDetails))
                .exchange()
                .expectStatus()
                .isOk();

        assertThat(count + 1).isEqualTo(employeeService.count());
    }
}
