package com.api.gateway.dto;

import com.google.gson.Gson;
import lombok.*;

import javax.validation.constraints.NotEmpty;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter
public class CompanyLogin {
    @NotEmpty(message = "username not be empty")
    private String username;
    @NotEmpty(message = "password not be empty")
    private String password;
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
