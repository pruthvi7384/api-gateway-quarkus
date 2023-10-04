package com.api.gateway.dto;

import com.google.gson.Gson;
import lombok.*;

import java.util.Date;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter
public class CompanyResponse {
    private Long id;
    private String name;
    private String username;
    private String emailId;
    private String contactNumber;
    private Date createdDate;
    private Date updatedDate;
    private Date lastLoginDate;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
