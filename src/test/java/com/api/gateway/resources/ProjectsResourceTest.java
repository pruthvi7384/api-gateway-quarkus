package com.api.gateway.resources;

import com.api.gateway.domain.Projects;
import com.api.gateway.dto.ProjectResponse;
import com.api.gateway.repositories.ProjectsRepository;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.mockito.ArgumentMatchers.any;

@QuarkusTest
class ProjectsResourceTest {

    Projects projectsRequest = new Projects();
    Projects projectsResponse = new Projects();

    @InjectMock
    ProjectsRepository projectsRepository;

    @BeforeEach
    void setUp() {
        projectsRequest.setName("SAM");
        projectsRequest.setInformation("SAM PROJECT");

        projectsResponse.setId(1L);
        projectsResponse.setName("SAM");
        projectsResponse.setInformation("SAM PROJECT");
    }


    @Test
    @TestTransaction
    void addProject(){
        Mockito.doReturn(null).when(projectsRepository).findByNameAndCompanyId(any(),any());
        Mockito.doNothing().when(projectsRepository).persist(projectsRequest);
        given()
                .when()
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer AAIkZDJiMDQyZmMtMDQwYS00YTNmLTkwZmMtZDYwODk4NmJhMmM3KRYLbCumZzt9GnWWYjXYutE6NL03DmBaY4hQkJ2r1PtL5O7h1jlx2anQipSkz-FX7AFFbiu0sH2pbwELB9vcQ8dgKei629RXmKUR2Vn5eEfKZ3zlM2Cxaac_CyehKh2ZmMXaCQdAiAofs4rvwLpKZw")
                .body(projectsRequest)
                .post("/api/project/add")
                .then()
                .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    @TestTransaction
    void viewProjects(){
        List<ProjectResponse> projectResponses = new ArrayList<>();
        projectResponses.add(new ProjectResponse());
        Mockito.doReturn(projectResponses).when(projectsRepository).findByCompanyId(any());
        given()
                .when()
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer AAIkZDJiMDQyZmMtMDQwYS00YTNmLTkwZmMtZDYwODk4NmJhMmM3KRYLbCumZzt9GnWWYjXYutE6NL03DmBaY4hQkJ2r1PtL5O7h1jlx2anQipSkz-FX7AFFbiu0sH2pbwELB9vcQ8dgKei629RXmKUR2Vn5eEfKZ3zlM2Cxaac_CyehKh2ZmMXaCQdAiAofs4rvwLpKZw")
                .get("/api/project/view")
                .then()
                .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    @TestTransaction
    void viewProject(){
        Mockito.doReturn(Optional.ofNullable(new Projects())).when(projectsRepository).findByCompanyIdAndProjectId(any(),any());
        given()
                .when()
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer AAIkZDJiMDQyZmMtMDQwYS00YTNmLTkwZmMtZDYwODk4NmJhMmM3KRYLbCumZzt9GnWWYjXYutE6NL03DmBaY4hQkJ2r1PtL5O7h1jlx2anQipSkz-FX7AFFbiu0sH2pbwELB9vcQ8dgKei629RXmKUR2Vn5eEfKZ3zlM2Cxaac_CyehKh2ZmMXaCQdAiAofs4rvwLpKZw")
                .get("/api/project/view/1")
                .then()
                .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    @TestTransaction
    void removeProject(){
        Mockito.doReturn(Optional.ofNullable(new Projects())).when(projectsRepository).findByCompanyIdAndProjectId(any(),any());
        Mockito.doReturn(true).when(projectsRepository).deleteById(any());
        given()
                .when()
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer AAIkZDJiMDQyZmMtMDQwYS00YTNmLTkwZmMtZDYwODk4NmJhMmM3KRYLbCumZzt9GnWWYjXYutE6NL03DmBaY4hQkJ2r1PtL5O7h1jlx2anQipSkz-FX7AFFbiu0sH2pbwELB9vcQ8dgKei629RXmKUR2Vn5eEfKZ3zlM2Cxaac_CyehKh2ZmMXaCQdAiAofs4rvwLpKZw")
                .delete("/api/project/remove/1")
                .then()
                .statusCode(Response.Status.OK.getStatusCode());
    }
}
