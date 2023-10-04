package com.api.gateway.dto;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter
public class ProxyRequest {
    @NotEmpty(message = "Payload must not be empty")
    private String payload;
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
