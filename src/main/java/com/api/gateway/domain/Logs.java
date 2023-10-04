package com.api.gateway.domain;

import com.google.gson.Gson;
import lombok.*;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "logs")
@AllArgsConstructor @NoArgsConstructor @Getter @Setter
public class Logs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(hidden = true)
    private Long id;
    @Lob
    @Column(name = "header")
    private String headers;
    @Lob
    @Column(name = "request_body")
    private String requestBody;
    @Lob
    @Column(name = "response")
    private String response;
    @Column(name = "log_date")
    @Schema(hidden = true)
    private Date logDate;
    @Column(name = "remove_status")
    private Boolean removeStatus;
    @ManyToOne
    @Schema(hidden = true)
    private Apis apis;
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
