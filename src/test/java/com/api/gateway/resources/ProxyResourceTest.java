package com.api.gateway.resources;

import com.api.gateway.domain.Apis;
import com.api.gateway.domain.Companies;
import com.api.gateway.domain.Projects;
import com.api.gateway.dto.ApiSecurityResponse;
import com.api.gateway.dto.ProxyRequest;
import com.api.gateway.repositories.ApiRepository;
import com.api.gateway.repositories.ApiSecurityRepository;
import com.api.gateway.repositories.CompaniesRepository;
import com.api.gateway.repositories.ProjectsRepository;
import com.api.gateway.utils.AESUtils;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.mockito.ArgumentMatchers.any;

@QuarkusTest
class ProxyResourceTest {
    @InjectMock
    CompaniesRepository companiesRepository;
    @InjectMock
    ProjectsRepository projectsRepository;
    @InjectMock
    ApiRepository apiRepository;
    @InjectMock
    ApiSecurityRepository apiSecurityRepository;

    ProxyRequest proxyRequest;

    Apis apiResponse;

    ApiSecurityResponse apiSecurityResponse;
    ApiSecurityResponse apiSecurityResponse1;

    @Inject
    AESUtils aesUtils;

    @BeforeEach
    void setUp(){
        proxyRequest = new ProxyRequest();
        proxyRequest.setPayload("{'username':'sam','password':'sam'}");

        apiResponse = new Apis();
        apiResponse.setName(aesUtils.encryptionAES("login"));
        apiResponse.setDescription(aesUtils.encryptionAES("SAM"));
        apiResponse.setApiUrl(aesUtils.encryptionAES("http://localhost:9000/api/company/login"));
        apiResponse.setMethod(aesUtils.encryptionAES("POST"));
        apiResponse.setHeaders(aesUtils.encryptionAES("{'Content-Type':'application/json'}"));
        apiResponse.setRequest(aesUtils.encryptionAES("{'username':'','password':''}"));

        apiSecurityResponse = new ApiSecurityResponse();
        apiSecurityResponse.setId(1L);
        apiSecurityResponse.setName("security status");
        apiSecurityResponse.setValue("true");

        apiSecurityResponse1 = new ApiSecurityResponse();
        apiSecurityResponse1.setId(1L);
        apiSecurityResponse1.setName("client id");
        apiSecurityResponse1.setValue("Su4Tf+0+s14ruk3b5otG9tApvqg/ho+QX0XLnE6PankEWvBAVN7387U4");
    }

    @Test
    @TestTransaction
    void proxyResource(){
        Mockito.doReturn(new Companies()).when(companiesRepository).findByUsername(any());
        Mockito.doReturn(new Projects()).when(projectsRepository).findByNameAndCompanyId(any(),any());
        Mockito.doReturn(Optional.ofNullable(apiResponse)).when(apiRepository).findApiByNameAndProjectId(any(),any());
        Mockito.doReturn(Optional.ofNullable(apiSecurityResponse)).when(apiSecurityRepository).findByNameApis(any(),any());
        Mockito.doReturn(Optional.ofNullable(apiSecurityResponse1)).when(apiSecurityRepository).findByNameApisClientId(any(),any());
        given()
                .when()
                .body(proxyRequest)
                .header("clientId","Su4Tf+0+s14ruk3b5otG9tApvqg/ho+QX0XLnE6PankEWvBAVN7387U3")
                .contentType(MediaType.APPLICATION_JSON)
                .post("/sam/sam/login")
                .then()
                .statusCode(Response.Status.OK.getStatusCode());
    }
}
