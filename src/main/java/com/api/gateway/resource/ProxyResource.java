package com.api.gateway.resource;

import com.api.gateway.dto.ProxyRequest;
import com.api.gateway.dto.ProxyResponse;
import com.api.gateway.services.ProxyService;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/{COMPANY_NAME}/{PROJECT_NAME}/{API_NAME}")
public class ProxyResource {
    private static final Logger logger = LoggerFactory.getLogger(ProxyResource.class);
    @Inject
    ProxyService proxyService;

    @Operation(
            operationId = "Call Proxy Api",
            description = "Call Proxy Api",
            summary = "Call Proxy Api"
    )
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response proxyResource(@Valid ProxyRequest proxyRequest,
                                  @Context HttpHeaders httpHeaders,
                                  @PathParam("COMPANY_NAME")String companyName,
                                  @PathParam("PROJECT_NAME")String projectName,
                                  @PathParam("API_NAME")String apiName
                                  ){
        logger.info("\nREST - PROXY SERVICE CALLING REQUEST - {}\nREST - PROXY SERVICE CALLING HEADER - {}",proxyRequest,httpHeaders.getRequestHeaders());
        logger.info("\nCOMPANY NAME - {}\nPROJECT NAME - {}\nAPI_NAME - {}",companyName,projectName,apiName);
        ProxyResponse proxyResponse = proxyService.proxyService(proxyRequest,httpHeaders,companyName,projectName,apiName);
        return Response.ok().entity(proxyResponse).build();
    }
}
