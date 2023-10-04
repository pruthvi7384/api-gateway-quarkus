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
@Table(name = "companies")
@NamedStoredProcedureQuery(name = "findAll",procedureName = "getCompanies",resultClasses = Companies.class)
@NamedStoredProcedureQuery(name = "findByUsername",procedureName = "getCompanyByUsername",parameters = {
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "name", type = String.class)
},resultClasses = Companies.class)
@AllArgsConstructor @NoArgsConstructor @Getter @Setter
public class Companies {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(hidden = true)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "email_id")
    private String emailId;
    @Column(name = "contact_number")
    private String contactNumber;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "created_date")
    @Schema(hidden = true)
    private Date createdDate;
    @Column(name = "updated_date")
    @Schema(hidden = true)
    private Date updatedDate;
    @Column(name = "last_login_date")
    @Schema(hidden = true)
    private Date lastLoginDate;
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
