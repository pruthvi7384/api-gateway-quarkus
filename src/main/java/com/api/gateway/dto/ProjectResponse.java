package com.api.gateway.dto;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter
public class ProjectResponse {
    private Long id;
    private String name;
    private String information;
    private Date createdDate;
    private Date updatedDate;
    private Boolean status;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
