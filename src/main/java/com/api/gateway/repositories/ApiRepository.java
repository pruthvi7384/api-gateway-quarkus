package com.api.gateway.repositories;

import com.api.gateway.domain.Apis;
import com.api.gateway.domain.Projects;
import com.api.gateway.dto.ApiResponse;
import com.api.gateway.utils.AESUtils;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class ApiRepository implements PanacheRepositoryBase<Apis,Long> {
    @Inject
    AESUtils aesUtils;

    @Inject
    ProxyApisRepository proxyApisRepository;
    @Inject
    ApiSecurityRepository apiSecurityRepository;
    @Inject
    LogsRepository logsRepository;

    public Optional<Apis> findApiByNameAndProjectId(String name,Long id){
        Projects projects = new Projects();
        projects.setId(id);
        return find("projects",projects).stream().filter(apis -> aesUtils.decryptionAES(apis.getName()).equalsIgnoreCase(name)).findFirst();
    }

    public Optional<Apis> findApiByProjectIdAndApiId(Long apiId, Long projectId){
        Projects projects = new Projects();
        projects.setId(projectId);
        return find("id = :id and projects = :projects", Parameters.with("id",apiId).and("projects",projects)).stream().findFirst();
    }

    public List<ApiResponse> findByProjectId(Long id){
        Projects projects = new Projects();
        projects.setId(id);
        return find("projects", Sort.by("createdDate").descending(),projects).stream().map(apis -> {
            ApiResponse apiResponse = new ApiResponse();
            apiResponse.setId(apis.getId());
            apiResponse.setName(aesUtils.decryptionAES(apis.getName()));
            apiResponse.setDescription(aesUtils.decryptionAES(apis.getDescription()));
            apiResponse.setApiUrl(aesUtils.decryptionAES(apis.getApiUrl()));
            apiResponse.setMethod(aesUtils.decryptionAES(apis.getMethod()));
            apiResponse.setHeaders(aesUtils.decryptionAES(apis.getHeaders()));
            apiResponse.setRequest(aesUtils.decryptionAES(apis.getRequest()));
            apiResponse.setStatus(apis.getStatus());
            apiResponse.setRemoveStatus(apis.getRemoveStatus());
            apiResponse.setCreatedDate(apis.getCreatedDate());
            apiResponse.setUpdatedDate(apis.getUpdatedDate());
            apiResponse.setRemoveDate(apis.getRemoveDate());
            apiResponse.setProxyApiResponse(proxyApisRepository.findByApiId(apis.getId()).get());
            apiResponse.setApiSecurityResponses(apiSecurityRepository.findByApiId(apis.getId()));
            apiResponse.setLogResponses(logsRepository.findByApis(apis.getId()));
            return apiResponse;
        }).collect(Collectors.toList());
    }

}
