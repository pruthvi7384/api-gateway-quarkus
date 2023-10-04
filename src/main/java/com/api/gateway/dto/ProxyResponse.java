package com.api.gateway.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter
public class ProxyResponse {
    private JsonNode proxyServerResponse;
    private String errorCode;
    private String errorMessage;
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
