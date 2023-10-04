package com.api.gateway.resource;

import com.api.gateway.domain.Apis;
import com.api.gateway.dto.ApiResponse;
import com.api.gateway.services.ApisService;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

@Path("/api/project/apis")
public class ApisResource {
    private static final Logger logger = LoggerFactory.getLogger(ApisResource.class);

    @Inject
    ApisService apisService;

    /**
     * API Add
     */
    @Operation(
            operationId = "Add Project API",
            description = "Add Project API",
            summary = "Add Project API"
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
    @Path("/add/{id}")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response addApi(@Valid Apis apis, @PathParam("id") Long id, @NotEmpty( message = GlobalConstant.HEADER_REQUIRED_MESSAGE) @HeaderParam(GlobalConstant.HEADER_AUTHORIZATION) String accessToken,@Context HttpHeaders httpHeaders){
        logger.info("\nREST - API ADD REQUEST - {} \nREST - API ADD PROJECT ID - {}\nREST - API ADD ACCESS_TOKEN - {},\nREST - HTTP HEADERS - {}",apis,id,accessToken,httpHeaders.getRequestHeaders());
        Map<String,Object> response = apisService.addApi(apis,accessToken,id,httpHeaders);
        return Response.ok().entity(response).build();
    }

    /**
     * Project related all apis
     */
    @Operation(
            operationId = "View All Project API",
            description = "View All Project API",
            summary = "View All Project API"
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
    @Path("/view/all/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response viewApi(@PathParam("id") Long id,@NotEmpty( message = GlobalConstant.HEADER_REQUIRED_MESSAGE) @HeaderParam(GlobalConstant.HEADER_AUTHORIZATION) String accessToken){
        logger.info("\nREST - API VIEW HEADER - {}\nREST - PROJECT ID - {}",accessToken,id);
        Map<String,Object> response = apisService.viewApis(accessToken,id);
        return Response.ok().entity(response).build();
    }

    /**
     * Specific Api information get
     */
    @Operation(
            operationId = "View Project API By Id",
            description = "View Project API By Id",
            summary = "View Project API By Id"
    )
    @Parameter(
            in = ParameterIn.HEADER,
            name = "Authorization",
            required = true
    )
    @Parameter(
            in = ParameterIn.PATH,
            name = "project_id",
            required = true
    )
    @Parameter(
            in = ParameterIn.PATH,
            name = "id",
            required = true
    )
    @Path("/view/{project_id}/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response viewApi(@NotEmpty( message = GlobalConstant.HEADER_REQUIRED_MESSAGE) @HeaderParam(GlobalConstant.HEADER_AUTHORIZATION) String accessToken, @PathParam("project_id") Long projectId, @PathParam("id")Long id){
        logger.info("\nREST - API VIEW HEADER - {}\nREST - PROJECT ID - {}\nREST - RECORD ID - {}",accessToken,projectId,id);
        Map<String,Object> response = apisService.viewApiInformation(accessToken,projectId,id);
        return Response.ok().entity(response).build();
    }

    /**
     * Update API
     */
    @Operation(
            operationId = "Update Project API By Id",
            description = "Update Project API By Id",
            summary = "Update Project API By Id"
    )
    @Parameter(
            in = ParameterIn.HEADER,
            name = "Authorization",
            required = true
    )
    @Parameter(
            in = ParameterIn.PATH,
            name = "project_id",
            required = true
    )
    @Parameter(
            in = ParameterIn.PATH,
            name = "id",
            required = true
    )
    @Path("/update/{project_id}/{id}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response updateApi(ApiResponse apis, @NotEmpty( message = GlobalConstant.HEADER_REQUIRED_MESSAGE) @HeaderParam(GlobalConstant.HEADER_AUTHORIZATION) String accessToken, @PathParam("project_id") Long projectId, @PathParam("id") Long id){
        logger.info("\nREST - API ADD REQUEST - {}\nREST - API VIEW HEADER - {}",apis,accessToken);
        Map<String,Object> response = apisService.updateApi(accessToken,projectId,id,apis);
        return Response.ok().entity(response).build();
    }

    /**
     *  Soft Removing API
     */
    @Operation(
            operationId = "Soft Remove Project API By Id",
            description = "Soft Remove Project API By Id",
            summary = "Soft Remove Project API By Id"
    )
    @Parameter(
            in = ParameterIn.HEADER,
            name = "Authorization",
            required = true
    )
    @Parameter(
            in = ParameterIn.PATH,
            name = "project_id",
            required = true
    )
    @Parameter(
            in = ParameterIn.PATH,
            name = "id",
            required = true
    )
    @Path("/soft-remove/{project_id}/{id}")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response softRemoveApi(@NotEmpty( message = GlobalConstant.HEADER_REQUIRED_MESSAGE) @HeaderParam(GlobalConstant.HEADER_AUTHORIZATION) String accessToken, @PathParam("project_id")Long projectId, @PathParam("id") Long id){
        logger.info("\nREST - API SOFT PROJECT ID - {} REMOVE ID - {}\nREST - API VIEW HEADER - {}",projectId,id,accessToken);
        Map<String,Object> response = apisService.softRemoveApi(accessToken,projectId,id);
        return Response.ok().entity(response).build();
    }

    /**
     *  Hard Removing API
     */
    @Operation(
            operationId = "Hard Remove Project API By Id",
            description = "Hard Remove Project API By Id",
            summary = "Hard Remove Project API By Id"
    )
    @Parameter(
            in = ParameterIn.HEADER,
            name = "Authorization",
            required = true
    )
    @Parameter(
            in = ParameterIn.PATH,
            name = "project_id",
            required = true
    )
    @Parameter(
            in = ParameterIn.PATH,
            name = "id",
            required = true
    )
    @Path("/hard-remove/{project_id}/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response hardRemoveApi(@NotEmpty( message = GlobalConstant.HEADER_REQUIRED_MESSAGE) @HeaderParam(GlobalConstant.HEADER_AUTHORIZATION) String accessToken, @PathParam("project_id")Long projectId, @PathParam("id") Long id){
        logger.info("\nREST - API HARD REMOVE ID - {} PROJECT ID -  {}\nREST - API VIEW HEADER - {}",projectId,id,accessToken);
        Map<String,Object> response = apisService.hardRemoveApi(accessToken,projectId,id);
        return Response.ok().entity(response).build();
    }
}
