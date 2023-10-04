package com.api.gateway.resources;

import com.api.gateway.domain.Companies;
import com.api.gateway.dto.CompanyLogin;
import com.api.gateway.repositories.CompaniesRepository;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static io.restassured.RestAssured.given;

@QuarkusTest
class CompanyResourceTest {

    Companies companiesRequest;
    Companies companiesResponse;
    CompanyLogin companyLogin;

    @InjectMock
    CompaniesRepository companiesRepository;

    @BeforeEach
    void beforeEach(){
        companiesRequest = new Companies();
        companiesRequest.setUsername("sam");
        companiesRequest.setName("sam");
        companiesRequest.setPassword("sam");
        companiesRequest.setEmailId("sam@siddhatech.com");
        companiesRequest.setContactNumber("+91 7720993937");
        companiesRequest.setLastLoginDate(new Date());
        companiesRequest.setUpdatedDate(new Date());

        companiesResponse = new Companies();
        companiesResponse.setId(1L);
        companiesResponse.setUsername("sam");
        companiesResponse.setName("sam");
        companiesResponse.setPassword("iXOjRWOt6xs+bqRsRrK8X8zS7vRmS8Nm3AY+8ixuefocXWOagVTED76xf6ogNTsiQU9G2ZwYDqGHE1PtdelaHE3sb19lxLVjgd328fuag8kmtkZ4LKNo/G9YSyh6GHG0CpRvJn1Eepx2jo9f7F5HES3C/m/Ep5dVru3H46RyQ1Y=");
        companiesResponse.setEmailId("sam@siddhatech.com");
        companiesResponse.setContactNumber("+91 7720993937");

        companyLogin = new CompanyLogin();
        companyLogin.setUsername("sam");
        companyLogin.setPassword("sam");
    }

    /**
     * Add Company Endpoint Test
     */
    @Test
    @TestTransaction
    void addCompany(){
        Mockito.doReturn(null).when(companiesRepository).findByUsername("sam");
        Mockito.doNothing().when(companiesRepository).persist(companiesRequest);
        given()
                .when()
                .contentType(MediaType.APPLICATION_JSON)
                .body(companiesRequest)
                .post("/api/company/add")
                .then()
                .statusCode(Response.Status.OK.getStatusCode());
    }

    /**
     * Remove Company Endpoint Test
     */
    @Test
    @TestTransaction
    void removeCompany(){
        Mockito.doReturn(true).when(companiesRepository).deleteById(1L);
        given()
                .when()
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer AAIkZDJiMDQyZmMtMDQwYS00YTNmLTkwZmMtZDYwODk4NmJhMmM3KRYLbCumZzt9GnWWYjXYutE6NL03DmBaY4hQkJ2r1PtL5O7h1jlx2anQipSkz-FX7AFFbiu0sH2pbwELB9vcQ8dgKei629RXmKUR2Vn5eEfKZ3zlM2Cxaac_CyehKh2ZmMXaCQdAiAofs4rvwLpKZw")
                .delete("/api/company/remove/1")
                .then()
                .statusCode(Response.Status.OK.getStatusCode());
    }

    /**
     * ValidatorMapper If 'Authorization' token not found
     */

    @Test
    @TestTransaction
    void validatorMapper(){
        given()
                .when()
                .contentType(MediaType.APPLICATION_JSON)
                .delete("/api/company/remove/1")
                .then()
                .statusCode(Response.Status.FORBIDDEN.getStatusCode());
    }

    /**
     * ValidatorMapper if request validate
     */
    @Test
    @TestTransaction
    void validatorMapper2(){
        companyLogin.setPassword("");
        given()
                .when()
                .body(companyLogin)
                .contentType(MediaType.APPLICATION_JSON)
                .post("/api/company/login")
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    /**
     * Company Login Endpoint Call
     */
    @Test
    @TestTransaction
    void login(){
        Mockito.doReturn(companiesResponse).when(companiesRepository).findByUsername("sam");
        given()
                .when()
                .body(companyLogin)
                .contentType(MediaType.APPLICATION_JSON)
                .post("/api/company/login")
                .then()
                .statusCode(Response.Status.OK.getStatusCode());
    }

    /**
     * Company Update Endpoint Call
     */
    @Test
    @TestTransaction
    void updateCompany(){
        Mockito.doReturn(companiesResponse).when(companiesRepository).findById(1L);
        Mockito.doNothing().when(companiesRepository).persist(companiesRequest);
        given()
                .when()
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer AAIkZDJiMDQyZmMtMDQwYS00YTNmLTkwZmMtZDYwODk4NmJhMmM3KRYLbCumZzt9GnWWYjXYutE6NL03DmBaY4hQkJ2r1PtL5O7h1jlx2anQipSkz-FX7AFFbiu0sH2pbwELB9vcQ8dgKei629RXmKUR2Vn5eEfKZ3zlM2Cxaac_CyehKh2ZmMXaCQdAiAofs4rvwLpKZw")
                .body(companiesRequest)
                .put("/api/company/update/1")
                .then()
                .statusCode(Response.Status.OK.getStatusCode());
    }

    /**
     * Get All Company Endpoint Call
     */
    @Test
    @TestTransaction
    void getAll(){
        List<Companies> companies = new ArrayList<>();
        companiesResponse.setName("VKqlS4oyYIGXmgC+BqXHS25g/q2LCUyactDYOx6AcjwmxSorqyVjHMTU9HjpU2YUHg/p9ZEcYbZHaT7MEwk42rjqiUf3jpWgWuKKAZFxYcQ0yU8uv7B4USK8T1oRYsMxu48Bgfek4IkarPOIVZYkTi+kVhqkIPNTavdg23ijmFQ=");
        companiesResponse.setEmailId("HSqUSDRCIyemvc9st1JjJBaBTR3MfaLzXuv8y323EErsz8aenseyQ5M0rsjBC83vrAXjdRobSEaOG75moPGMJRIW5UiVFqup4kHoi4nyziGT5vzhH6S5WACo7cFUwy+xxARk4T3KwcOplAg5gaWnlJSLLjdkqNNiNhlEw1f6BaA=");
        companiesResponse.setContactNumber("OBUaWMfnqeuhYTE0MPxynVyCSyg6hXV6jLqlhnSJt4zBZb8vfY/E9crHiG28OAUI7c6Rnm8ZRoeGFzvBMn6t51pKPt1o8bWVZVzxI8iJiaPqt8D6l4YSbckMuqlSGE+f1DbIsBFEjRbMFk3P0kJetBoJkVoa3qXQgyA6DRAWniI=");
        companies.add(companiesResponse);

        Mockito.doReturn(companies).when(companiesRepository).listAll();
        given()
                .when()
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer AAIkZDJiMDQyZmMtMDQwYS00YTNmLTkwZmMtZDYwODk4NmJhMmM3KRYLbCumZzt9GnWWYjXYutE6NL03DmBaY4hQkJ2r1PtL5O7h1jlx2anQipSkz-FX7AFFbiu0sH2pbwELB9vcQ8dgKei629RXmKUR2Vn5eEfKZ3zlM2Cxaac_CyehKh2ZmMXaCQdAiAofs4rvwLpKZw")
                .get("/api/company/all")
                .then()
                .statusCode(Response.Status.OK.getStatusCode());
    }

}
