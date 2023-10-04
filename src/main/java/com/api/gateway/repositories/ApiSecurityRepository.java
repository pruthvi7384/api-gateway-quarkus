package com.api.gateway.repositories;

import com.api.gateway.domain.ApiSecurity;
import com.api.gateway.domain.Apis;
import com.api.gateway.dto.ApiSecurityResponse;
import com.api.gateway.utils.RSAUtils;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class ApiSecurityRepository implements PanacheRepositoryBase<ApiSecurity,Long> {
    @Inject
    RSAUtils rsaUtils;

    public Optional<ApiSecurity> findByApiIdAndName(Long id, String name){
        Apis apis = new Apis();
        apis.setId(id);
        return find("apis",apis).stream().filter(apiSecurity -> rsaUtils.decryptMessage(apiSecurity.getName()).equalsIgnoreCase(name)).findFirst();
    }
    public Optional<ApiSecurityResponse> findByNameApis(Long id, String name){
        Apis apis = new Apis();
        apis.setId(id);
        return find("apis",apis).stream().filter(apiSecurity -> rsaUtils.decryptMessage(apiSecurity.getName()).equalsIgnoreCase(name)).findFirst().map(apiSecurity -> {
            ApiSecurityResponse apiSecurityResponse = new ApiSecurityResponse();
            apiSecurityResponse.setId(apiSecurity.getId());
            apiSecurityResponse.setName(rsaUtils.decryptMessage(apiSecurity.getName()));
            apiSecurityResponse.setValue(rsaUtils.decryptMessage(apiSecurity.getValue()));
            return apiSecurityResponse;
        });
    }

    public Optional<ApiSecurityResponse> findByNameApisClientId(Long id, String name){
        Apis apis = new Apis();
        apis.setId(id);
        return find("apis",apis).stream().filter(apiSecurity -> rsaUtils.decryptMessage(apiSecurity.getName()).equalsIgnoreCase(name)).findFirst().map(apiSecurity -> {
            ApiSecurityResponse apiSecurityResponse = new ApiSecurityResponse();
            apiSecurityResponse.setId(apiSecurity.getId());
            apiSecurityResponse.setName(rsaUtils.decryptMessage(apiSecurity.getName()));
            apiSecurityResponse.setValue(rsaUtils.decryptMessage(apiSecurity.getValue()));
            return apiSecurityResponse;
        });
    }

    public List<ApiSecurityResponse> findByApiId(Long id){
        Apis apis = new Apis();
        apis.setId(id);
        return find("apis",apis).stream().map(apiSecurity -> {
            ApiSecurityResponse apiSecurityResponse = new ApiSecurityResponse();
            apiSecurityResponse.setId(apiSecurity.getId());
            apiSecurityResponse.setName(rsaUtils.decryptMessage(apiSecurity.getName()));
            apiSecurityResponse.setValue(rsaUtils.decryptMessage(apiSecurity.getValue()));
            return apiSecurityResponse;
        }).collect(Collectors.toList());
    }
}
