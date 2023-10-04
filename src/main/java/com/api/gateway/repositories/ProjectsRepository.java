package com.api.gateway.repositories;

import com.api.gateway.domain.Companies;
import com.api.gateway.domain.Projects;
import com.api.gateway.dto.ProjectResponse;
import com.api.gateway.utils.GlobalConstant;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class ProjectsRepository implements PanacheRepositoryBase<Projects,Long> {
    public Projects findByNameAndCompanyId(String name, Long id){
        Companies companies = new Companies();
        companies.setId(id);
        return find("name = :name and companies = :companies",Parameters.with("name",name).and(GlobalConstant.COMPANIES,companies)).firstResult();
    }

    public Long findByNameAndCompanyIdCount(String name, Long id){
        Companies companies = new Companies();
        companies.setId(id);
        return find("name = :name and companies = :companies",Parameters.with("companies",companies).and("name",name)).count();
    }

    public Long findByAllParameterCount(String name, Long id, String information){
        Companies companies = new Companies();
        companies.setId(id);
        return find("name = :name and information != :information and companies = :companies",Parameters.with("companies",companies).and("name",name).and("information",information)).count();
    }

    public List<ProjectResponse> findByCompanyId(Long id){
        Companies companies = new Companies();
        companies.setId(id);
        return find(GlobalConstant.COMPANIES, Sort.descending("createdDate"),companies).list().stream().map(projects -> {
             ProjectResponse projectResponse = new ProjectResponse();
             projectResponse.setId(projects.getId());
             projectResponse.setName(projects.getName());
             projectResponse.setStatus(projects.getStatus());
             projectResponse.setInformation(projects.getInformation());
             projectResponse.setCreatedDate(projects.getCreatedDate());
             projectResponse.setUpdatedDate(projects.getUpdatedDate());
             return projectResponse;
        }).collect(Collectors.toList());
    }
    public Optional<ProjectResponse> findByCompanyIdAndProjectId(Long companyId, Long projectId){
        Companies companies = new Companies();
        companies.setId(companyId);
        return find("id = :projectId and companies = :companies",Parameters.with("projectId",projectId).and(GlobalConstant.COMPANIES,companies)).stream().map(projects -> {
            ProjectResponse projectResponse = new ProjectResponse();
            projectResponse.setId(projects.getId());
            projectResponse.setName(projects.getName());
            projectResponse.setStatus(projects.getStatus());
            projectResponse.setInformation(projects.getInformation());
            projectResponse.setCreatedDate(projects.getCreatedDate());
            projectResponse.setUpdatedDate(projects.getUpdatedDate());
            return projectResponse;
        }).findFirst();
    }
    public Projects findByCompanyIdAndProjectIdSingle(Long companyId, Long projectId){
        Companies companies = new Companies();
        companies.setId(companyId);
        return find("id = :projectId and companies = :companies",Parameters.with("projectId",projectId).and(GlobalConstant.COMPANIES,companies)).firstResult();
    }
}
