package com.api.gateway.domain;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "api_security")
@AllArgsConstructor @NoArgsConstructor @Setter @Getter
public class ApiSecurity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(hidden = true)
    private Long id;
    @Column(name = "name")
    @NotEmpty(message = "name is not be empty")
    private String name;
    @Column(name = "value")
    @NotEmpty(message = "value is not be empty")
    private String value;
    @ManyToOne
    @Schema(hidden = true)
    private Apis apis;
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
