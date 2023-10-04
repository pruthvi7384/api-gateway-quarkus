package com.api.gateway.dto;


import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter
public class ApiResponse {
    private Long id;
    private String name;
    private String description;
    private String apiUrl;
    private String method;
    private String headers;
    private String request;
    private Boolean status;
    private Boolean removeStatus;
    private Date createdDate;
    private Date updatedDate;
    private Date removeDate;
    private ProxyApiResponse proxyApiResponse;
    private List<ApiSecurityResponse> apiSecurityResponses;
    private List<LogResponse> logResponses;
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
