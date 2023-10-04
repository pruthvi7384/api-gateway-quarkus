package com.api.gateway.dto;

import com.google.gson.Gson;
import lombok.*;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class LoginResponse {
    private String accessToken;
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
