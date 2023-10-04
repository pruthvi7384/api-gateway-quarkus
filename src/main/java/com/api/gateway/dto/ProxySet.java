package com.api.gateway.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor @NoArgsConstructor @Getter @Setter
public class ProxySet {
    private Long apisId;
    private JsonNode requestTemplate;
    private JsonNode headerTemplate;
    private JsonNode appRequest;
    private JsonNode appHeaders;
    private List<String> headersFields;
    private String method;
    private String apiUrl;
    private JsonNode response;
    private String errorCode;
    private String errorMessage;
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}

