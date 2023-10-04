package com.api.gateway.domain;

import com.google.gson.Gson;
import lombok.*;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "projects")
@AllArgsConstructor @NoArgsConstructor @Getter @Setter
public class Projects {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(hidden = true)
    private Long id;
    @Column(name = "name")
    private String name;
    @Lob
    @Column(name = "information")
    private String information;
    @Column(name = "created_date")
    @Schema(hidden = true)
    private Date createdDate;
    @Column(name = "updated_date")
    @Schema(hidden = true)
    private Date updatedDate;
    @Column(name = "status")
    @Schema(hidden = true)
    private Boolean status;
    @ManyToOne
    @Schema(hidden = true)
    private Companies companies;
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
