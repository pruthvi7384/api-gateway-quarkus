package com.api.gateway.exceptions;

import com.api.gateway.utils.GlobalConstant;
import org.jboss.resteasy.reactive.RestResponse;

import javax.validation.ValidationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;

@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ValidationException> {
    @Override
    public Response toResponse(ValidationException exception) {
        Map<String,String> response = new HashMap<>();
        if (exception.getMessage().contains(GlobalConstant.HEADER_AUTHORIZATION)) {
            response.put("errorCode","403");
            response.put("errorDescription",exception.getMessage());
            return Response.status(Response.Status.FORBIDDEN).entity(response).build();
        } else {
            response.put("errorCode","400");
            response.put("errorDescription",exception.getMessage());
            return Response.status(RestResponse.Status.BAD_REQUEST).entity(response).build();
        }

    }
}
