package com.api.gateway.domain;

import com.google.gson.Gson;
import lombok.*;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "project_setting")
@AllArgsConstructor @NoArgsConstructor @Getter @Setter
public class ProjectSetting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(hidden = true)
    private Long id;
    @Column(name = "name")
    private String name;
    @Lob
    @Column(name = "value")
    private String value;
    @Column(name = "created_date")
    @Schema(hidden = true)
    private Date createdDate;
    @Column(name = "updated_date")
    @Schema(hidden = true)
    private Date updatedDate;
    @ManyToOne
    @Schema(hidden = true)
    private Projects projects;
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
