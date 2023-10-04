package com.api.gateway.dto;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter
public class ApiSecurityResponse {
    private Long id;
    private String name;
    private String value;
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
