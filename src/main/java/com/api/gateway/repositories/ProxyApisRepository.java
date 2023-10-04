package com.api.gateway.repositories;

import com.api.gateway.domain.Apis;
import com.api.gateway.domain.ProxyApis;
import com.api.gateway.dto.ProxyApiResponse;
import com.api.gateway.utils.AESUtils;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

@ApplicationScoped
public class ProxyApisRepository implements PanacheRepositoryBase<ProxyApis,Long> {
    @Inject
    AESUtils aesUtils;

    public Optional<ProxyApiResponse> findByApiId(Long id){
        Apis apis = new Apis();
        apis.setId(id);
        return find("apis",apis).stream().map(proxyApis -> {
            ProxyApiResponse apiResponse = new ProxyApiResponse();
            apiResponse.setUrl(aesUtils.decryptionAES(proxyApis.getUrl()));
            apiResponse.setHeaders(aesUtils.decryptionAES(proxyApis.getHeaders()));
            apiResponse.setRequest(aesUtils.decryptionAES(proxyApis.getRequest()));
            apiResponse.setMethod(aesUtils.decryptionAES(proxyApis.getMethod()));
            apiResponse.setGeneratedAt(proxyApis.getGeneratedAt());
            apiResponse.setStatus(proxyApis.getStatus());
            return apiResponse;
        }).findFirst();
    }
}
