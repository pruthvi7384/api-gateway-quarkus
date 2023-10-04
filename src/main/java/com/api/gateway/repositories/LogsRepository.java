package com.api.gateway.repositories;

import com.api.gateway.domain.Apis;
import com.api.gateway.domain.Logs;
import com.api.gateway.dto.LogResponse;
import com.api.gateway.utils.AESUtils;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import org.bouncycastle.jcajce.provider.symmetric.AES;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class LogsRepository implements PanacheRepositoryBase<Logs,Long> {
    @Inject
    AESUtils aesUtils;
    public List<LogResponse> findByApis(Long id){
        Apis apis = new Apis();
        apis.setId(id);
        return find("apis",apis).stream().map(logs -> {
            LogResponse logResponse = new LogResponse();
            logResponse.setId(logs.getId());
            logResponse.setLogDate(logs.getLogDate());
            logResponse.setRemoveStatus(logs.getRemoveStatus());
            logResponse.setHeaders(aesUtils.decryptionAES(logs.getHeaders()));
            logResponse.setRequestBody(aesUtils.decryptionAES(logs.getRequestBody()));
            logResponse.setResponse(aesUtils.decryptionAES(logs.getResponse()));
            return logResponse;
        }).collect(Collectors.toList());
    }
}
