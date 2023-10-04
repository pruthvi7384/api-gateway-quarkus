package com.api.gateway.dto;

import com.api.gateway.domain.Apis;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter
public class LogResponse {
    private Long id;
    private String headers;
    private String requestBody;
    private String response;
    private Date logDate;
    private Boolean removeStatus;
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
