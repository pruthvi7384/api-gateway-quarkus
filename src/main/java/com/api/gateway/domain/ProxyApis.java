package com.api.gateway.domain;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "proxy_apis")
@AllArgsConstructor @NoArgsConstructor @Getter @Setter
public class ProxyApis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(hidden = true)
    private Long id;
    private String url;
    private String method;
    private String headers;
    private String request;
    @Schema(hidden = true)
    private Date generatedAt;
    @Schema(hidden = true)
    private Boolean status;
    @OneToOne
    @Schema(hidden = true)
    private Apis apis;
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
