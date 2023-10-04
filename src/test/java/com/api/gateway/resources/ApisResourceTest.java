package com.api.gateway.resources;

import com.api.gateway.domain.ApiSecurity;
import com.api.gateway.domain.Apis;
import com.api.gateway.domain.ProxyApis;
import com.api.gateway.dto.*;
import com.api.gateway.repositories.*;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.mockito.ArgumentMatchers.any;

@QuarkusTest
class ApisResourceTest {
    @InjectMock
    ApiRepository apiRepository;
    @InjectMock
    ProjectsRepository projectsRepository;
    @InjectMock
    ProxyApisRepository proxyApisRepository;
    @InjectMock
    ApiSecurityRepository apiSecurityRepository;
    @InjectMock
    LogsRepository logsRepository;
    Apis apisRequest;
    ProjectResponse projectResponse;

    Apis apisResponse;

    ApiResponse apiUpdateRequest;

    @Inject
    AESUtils aesUtils;

    @BeforeEach
    void setUp(){
        apisRequest = new Apis();
        apisRequest.setApiUrl("http://localhost/login");
        apisRequest.setName("login");
        apisRequest.setHeaders("{'Content-Type':'Application-Json'}");
        apisRequest.setRequest("{'username':'','password':'password'}");
        apisRequest.setMethod("post");
        apisRequest.setDescription("SAM Project");

        projectResponse = new ProjectResponse();
        projectResponse.setId(1L);
        projectResponse.setName("SAM");
        projectResponse.setInformation("SAM PROJECT");

        apisResponse = new Apis();
        apisResponse.setId(1L);
        apisResponse.setName(aesUtils.encryptionAES("SAM"));
        apisResponse.setDescription(aesUtils.encryptionAES("SAM"));
        apisResponse.setApiUrl(aesUtils.encryptionAES("http://localhost:6000/login"));
        apisResponse.setMethod(aesUtils.encryptionAES("POST"));
        apisResponse.setHeaders(aesUtils.encryptionAES("{'Content-Type':'Application-Json'}"));
        apisResponse.setRequest(aesUtils.encryptionAES("{'username':'sam','password':'sam'}"));

        apiUpdateRequest = new ApiResponse();
        apiUpdateRequest.setName(aesUtils.encryptionAES("SAM"));
        apiUpdateRequest.setDescription(aesUtils.encryptionAES("SAM"));
        apiUpdateRequest.setApiUrl(aesUtils.encryptionAES("http://localhost:6000/login"));
        apiUpdateRequest.setMethod(aesUtils.encryptionAES("POST"));
        apiUpdateRequest.setHeaders(aesUtils.encryptionAES("{'Content-Type':'Application-Json'}"));
        apiUpdateRequest.setRequest(aesUtils.encryptionAES("{'username':'sam','password':'sam'}"));
    }

    @Test
    @TestTransaction
    void addApi(){
        Mockito.doReturn(Optional.ofNullable(projectResponse)).when(projectsRepository).findByCompanyIdAndProjectId(any(),any());
        Mockito.doReturn(Optional.ofNullable(null)).when(apiRepository).findApiByNameAndProjectId(any(),any());
        Mockito.doNothing().when(apiRepository).persist(apisRequest);
        Mockito.doNothing().when(proxyApisRepository).persist(new ProxyApis());
        Mockito.doNothing().when(apiSecurityRepository).persist(new ApiSecurity());
        given()
                .when()
                .body(apisRequest)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer AAIkZDJiMDQyZmMtMDQwYS00YTNmLTkwZmMtZDYwODk4NmJhMmM3KRYLbCumZzt9GnWWYjXYutE6NL03DmBaY4hQkJ2r1PtL5O7h1jlx2anQipSkz-FX7AFFbiu0sH2pbwELB9vcQ8dgKei629RXmKUR2Vn5eEfKZ3zlM2Cxaac_CyehKh2ZmMXaCQdAiAofs4rvwLpKZw")
                .post("/api/project/apis/add/9")
                .then()
                .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    @TestTransaction
    void viewApi(){
        List<ApiResponse> apiResponsesList = new ArrayList<>();
        apiResponsesList.add(new ApiResponse());
        Mockito.doReturn(Optional.ofNullable(projectResponse)).when(projectsRepository).findByCompanyIdAndProjectId(any(),any());
        Mockito.doReturn(apiResponsesList).when(apiRepository).findByProjectId(any());
        given()
                .when()
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer AAIkZDJiMDQyZmMtMDQwYS00YTNmLTkwZmMtZDYwODk4NmJhMmM3KRYLbCumZzt9GnWWYjXYutE6NL03DmBaY4hQkJ2r1PtL5O7h1jlx2anQipSkz-FX7AFFbiu0sH2pbwELB9vcQ8dgKei629RXmKUR2Vn5eEfKZ3zlM2Cxaac_CyehKh2ZmMXaCQdAiAofs4rvwLpKZw")
                .get("/api/project/apis/view/all/9")
                .then()
                .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    @TestTransaction
    void viewApiSingle(){
        List<ApiSecurityResponse> apiSecurityResponseList = new ArrayList<>();
        apiSecurityResponseList.add(new ApiSecurityResponse());

        List<LogResponse> logResponseList = new ArrayList<>();
        logResponseList.add(new LogResponse());

        Mockito.doReturn(Optional.ofNullable(projectResponse)).when(projectsRepository).findByCompanyIdAndProjectId(any(),any());
        Mockito.doReturn(apisResponse).when(apiRepository).findById(any());
        Mockito.doReturn(Optional.ofNullable(new ProxyApiResponse())).when(proxyApisRepository).findByApiId(any());
        Mockito.doReturn(apiSecurityResponseList).when(apiSecurityRepository).findByApiId(any());
        Mockito.doReturn(logResponseList).when(logsRepository).findByApis(any());
        given()
                .when()
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer AAIkZDJiMDQyZmMtMDQwYS00YTNmLTkwZmMtZDYwODk4NmJhMmM3KRYLbCumZzt9GnWWYjXYutE6NL03DmBaY4hQkJ2r1PtL5O7h1jlx2anQipSkz-FX7AFFbiu0sH2pbwELB9vcQ8dgKei629RXmKUR2Vn5eEfKZ3zlM2Cxaac_CyehKh2ZmMXaCQdAiAofs4rvwLpKZw")
                .get("/api/project/apis/view/9/1")
                .then()
                .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    @TestTransaction
    void updateApi(){
        Mockito.doReturn(Optional.ofNullable(projectResponse)).when(projectsRepository).findByCompanyIdAndProjectId(any(),any());
        Mockito.doReturn(null).when(apiRepository).findById(any());
        given()
                .when()
                .body(apiUpdateRequest)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer AAIkZDJiMDQyZmMtMDQwYS00YTNmLTkwZmMtZDYwODk4NmJhMmM3KRYLbCumZzt9GnWWYjXYutE6NL03DmBaY4hQkJ2r1PtL5O7h1jlx2anQipSkz-FX7AFFbiu0sH2pbwELB9vcQ8dgKei629RXmKUR2Vn5eEfKZ3zlM2Cxaac_CyehKh2ZmMXaCQdAiAofs4rvwLpKZw")
                .put("/api/project/apis/update/9/9")
                .then()
                .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    @TestTransaction
    void softRemoveApi(){
        Mockito.doReturn(Optional.ofNullable(projectResponse)).when(projectsRepository).findByCompanyIdAndProjectId(any(),any());
        Mockito.doReturn(apisResponse).when(apiRepository).findById(any());
        Mockito.doNothing().when(apiRepository).persist(apisRequest);
        given()
                .when()
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer AAIkZDJiMDQyZmMtMDQwYS00YTNmLTkwZmMtZDYwODk4NmJhMmM3KRYLbCumZzt9GnWWYjXYutE6NL03DmBaY4hQkJ2r1PtL5O7h1jlx2anQipSkz-FX7AFFbiu0sH2pbwELB9vcQ8dgKei629RXmKUR2Vn5eEfKZ3zlM2Cxaac_CyehKh2ZmMXaCQdAiAofs4rvwLpKZw")
                .post("/api/project/apis/soft-remove/9/9")
                .then()
                .statusCode(Response.Status.OK.getStatusCode());
    }
    @Test
    @TestTransaction
    void hardRemoveApi(){
        Mockito.doReturn(Optional.ofNullable(projectResponse)).when(projectsRepository).findByCompanyIdAndProjectId(any(),any());
        Mockito.doReturn(apisResponse).when(apiRepository).findById(any());
        Mockito.doNothing().when(apiRepository).delete(any());
        given()
                .when()
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer AAIkZDJiMDQyZmMtMDQwYS00YTNmLTkwZmMtZDYwODk4NmJhMmM3KRYLbCumZzt9GnWWYjXYutE6NL03DmBaY4hQkJ2r1PtL5O7h1jlx2anQipSkz-FX7AFFbiu0sH2pbwELB9vcQ8dgKei629RXmKUR2Vn5eEfKZ3zlM2Cxaac_CyehKh2ZmMXaCQdAiAofs4rvwLpKZw")
                .delete("/api/project/apis/hard-remove/9/9")
                .then()
                .statusCode(Response.Status.OK.getStatusCode());
    }
}
