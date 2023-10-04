package com.api.gateway.domain;

import com.google.gson.Gson;
import lombok.*;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "apis")
@AllArgsConstructor @NoArgsConstructor @Getter @Setter
public class Apis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(hidden = true)
    private Long id;
    @Column(name = "name")
    private String name;
    @Lob
    @Column(name = "description")
    private String description;
    @Column(name = "api_url")
    private String apiUrl;
    @Column(name = "method")
    private String method;
    @Lob
    @Column(name = "headers")
    private String headers;
    @Lob
    @Column(name = "request")
    private String request;
    @Column(name = "status")
    @Schema(hidden = true)
    private Boolean status;
    @Column(name = "remove_status")
    @Schema(hidden = true)
    private Boolean removeStatus;
    @Column(name = "created_date")
    @Schema(hidden = true)
    private Date createdDate;
    @Column(name = "updated_date")
    @Schema(hidden = true)
    private Date updatedDate;
    @Column(name = "remove_date")
    @Schema(hidden = true)
    private Date removeDate;
    @ManyToOne
    @Schema(hidden = true)
    private Projects projects;
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
