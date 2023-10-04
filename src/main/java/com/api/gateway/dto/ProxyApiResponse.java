package com.api.gateway.dto;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter
public class ProxyApiResponse {
    private Long id;
    private String url;
    private String method;
    private String headers;
    private String request;
    private Date generatedAt;
    private Boolean status;
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
