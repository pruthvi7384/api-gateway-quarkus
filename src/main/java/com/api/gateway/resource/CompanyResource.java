package com.api.gateway.resource;

import com.api.gateway.domain.Companies;
import com.api.gateway.dto.CompanyLogin;
import com.api.gateway.services.CompanyService;
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

/**
 * Company related api
 */
@Path("/api/company")
public class CompanyResource {
    private static final Logger logger = LoggerFactory.getLogger(CompanyResource.class);

    @Inject
    CompanyService companyService;

    /**
     * Add company
     */
    @Operation(
            operationId = "Company Registration",
            description = "Company Registration",
            summary = "Company Registration"
    )
    @Path("/add")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response addCompany(@Valid Companies companies) {
        logger.info("\nREST - COMPANY ADD REQUEST BODY - {}", companies);
        Map<String,Object> response = companyService.addCompany(companies);
        return Response.ok().entity(response).build();
    }

    /**
     * Remove Company
     */
    @Operation(
            operationId = "Company Remove Id",
            description = "Company Remove Id",
            summary = "Company Remove Id"
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
    public Response removeCompany(@NotEmpty(message = GlobalConstant.HEADER_REQUIRED_MESSAGE) @HeaderParam(GlobalConstant.HEADER_AUTHORIZATION) String accessToken, @PathParam("id") Long id){
        logger.info("\nREST - COMPANY REMOVE REQUEST - {}\nREST - COMPANY REMOVE HEADERS - {}",id,accessToken);
        Map<String,String> response = companyService.removeCompany(id);
        return Response.ok().entity(response).build();
    }

    /**
     * Update Company
     */
    @Operation(
            operationId = "Company Update By Id",
            description = "Company Update By Id",
            summary = "Company Update By Id"
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
    public Response updateCompany(@Valid Companies companies, @PathParam("id")Long id, @NotEmpty(message = GlobalConstant.HEADER_REQUIRED_MESSAGE) @HeaderParam(GlobalConstant.HEADER_AUTHORIZATION) String accessToken){
        logger.info("REST - COMPANY UPDATE REQUEST - {} , ID - {} \nREST - COMPANY UPDATE REQUEST HEADERS - {}", companies,id,accessToken);
        Map<String, Object> response = companyService.updateCompany(companies,id);
        return Response.ok().entity(response).build();
    }

    /**
     * Login Company
     */
    @Operation(
            operationId = "Company Login",
            description = "Company Login",
            summary = "Company Login"
    )
    @Path("/login")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response loginCompany(@Valid CompanyLogin companyLogin){
        logger.info("REST - COMPANY LOGIN REQUEST - {}", companyLogin);
        Map<String,Object> response = companyService.login(companyLogin);
        return Response.ok().entity(response).build();
    }

    /**
     * Get All Company
     */

    @Operation(
            operationId = "Get All Company",
            description = "Get All Company",
            summary = "Get All Company"
    )
    @Parameter(
            in = ParameterIn.HEADER,
            name = "Authorization",
            required = true
    )
    @Path("/all")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response getAllCompanies(@NotEmpty(message = GlobalConstant.HEADER_REQUIRED_MESSAGE) @HeaderParam(GlobalConstant.HEADER_AUTHORIZATION) String accessToken){
        logger.info("REST - COMPANY GET ALL REQUEST  \nREST - COMPANY UPDATE REQUEST HEADERS - {}", accessToken);
        Map<String,Object> response = companyService.getAllCompany();
        return Response.ok().entity(response).build();
    }

    /**
     * Store Procedure Call
     */
    @Path("/all/storeProcedure")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response getAllCompanies() {
        logger.info("REST - COMPANY GET ALL USING STORE PROCEDURE");
        Map<String,Object> response = companyService.getAll();
        return Response.ok().entity(response).build();
    }
}