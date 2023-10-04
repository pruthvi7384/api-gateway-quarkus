package com.api.gateway.resource;

import com.api.gateway.domain.Projects;
import com.api.gateway.services.ProjectsService;
import com.api.gateway.utils.GlobalConstant;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.ParameterIn;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

@Path("/api/project")
public class ProjectsResource {
    private static final Logger logger = LoggerFactory.getLogger(ProjectsResource.class);

    @Inject
    ProjectsService projectsService;

    /**
     * Add Project
     */
    @Operation(
            operationId = "Add Project",
            description = "Add Project",
            summary = "Add Project"
    )
    @Parameter(
            in = ParameterIn.HEADER,
            name = "Authorization",
            required = true
    )
    @Path("/add")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response addProject(@Valid Projects projects, @NotEmpty(message = GlobalConstant.HEADER_REQUIRED_MESSAGE) @HeaderParam(GlobalConstant.HEADER_AUTHORIZATION) String accessToken){
        logger.info("\nREST - PROJECT ADD REQUEST BODY - {} \nREST - PROJECT ADD REQUEST HEADERS - {}", projects,accessToken);
        Map<String,Object> response = projectsService.addProject(accessToken,projects);
        return Response.ok().entity(response).build();
    }

    /**
     * Get Projects
     */
    @Operation(
            operationId = "View Project",
            description = "View Project",
            summary = "View Project"
    )
    @Parameter(
            in = ParameterIn.HEADER,
            name = "Authorization",
            required = true
    )
    @Path("/view")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response viewProjects(@NotEmpty(message = GlobalConstant.HEADER_REQUIRED_MESSAGE) @HeaderParam(GlobalConstant.HEADER_AUTHORIZATION) String accessToken) {
        logger.info("\nREST - PROJECT GET REQUEST HEADERS - {}",accessToken);
        Map<String,Object> response = projectsService.viewProjects(accessToken);
        return Response.ok().entity(response).build();
    }

    /**
     * View Specific Project Profile
     */
    @Operation(
            operationId = "View Project By Id",
            description = "View Project By Id",
            summary = "View Project By Id"
    )
    @Parameter(
            in = ParameterIn.HEADER,
            name = "Authorization",
            required = true
    )
    @Parameter(
            in = ParameterIn.PATH,
            name = "id",
            required = true
    )
    @Path("/view/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response viewProject(@NotEmpty(message = GlobalConstant.HEADER_REQUIRED_MESSAGE) @HeaderParam(GlobalConstant.HEADER_AUTHORIZATION) String accessToken, @PathParam("id")Long id) {
        logger.info("\nREST - PROJECT GET PROJECT REQUEST HEADERS - {} ID - {}",accessToken,id);
        Map<String,Object> response = projectsService.viewProjectSpecific(accessToken,id);
        return Response.ok().entity(response).build();
    }

    /**
     * Update Project Profile
     */
    @Operation(
            operationId = "Update Project By Id",
            description = "Update Project By Id",
            summary = "Update Project By Id"
    )
    @Parameter(
            in = ParameterIn.HEADER,
            name = "Authorization",
            required = true
    )
    @Parameter(
            in = ParameterIn.PATH,
            name = "id",
            required = true
    )
    @Path("/update/{id}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response updateProject(@Valid Projects projects,@PathParam("id")Long id, @NotEmpty(message = GlobalConstant.HEADER_REQUIRED_MESSAGE) @HeaderParam(GlobalConstant.HEADER_AUTHORIZATION) String accessToken) {
        logger.info("\nREST - PROJECT UPDATE REQUEST - {} - ID - {}\nREST - PROJECT GET PROJECT REQUEST HEADERS - {}",projects,id,accessToken);
        Map<String,Object> response = projectsService.updateProject(projects,accessToken,id);
        return Response.ok().entity(response).build();
    }

    /**
     * Remove project
     */
    @Operation(
            operationId = "Remove Project By Id",
            description = "Remove Project By Id",
            summary = "Remove Project By Id"
    )
    @Parameter(
            in = ParameterIn.HEADER,
            name = "Authorization",
            required = true
    )
    @Parameter(
            in = ParameterIn.PATH,
            name = "id",
            required = true
    )
    @Path("/remove/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response removeProject(@NotEmpty(message = GlobalConstant.HEADER_REQUIRED_MESSAGE) @HeaderParam(GlobalConstant.HEADER_AUTHORIZATION) String accessToken, @PathParam("id") Long id){
        logger.info("\nREST - PROJECT REMOVE REQUEST HEADERS - {} ID - {}",accessToken,id);
        Map<String,Object> response = projectsService.removeProject(accessToken,id);
        return Response.ok().entity(response).build();
    }

}

