package com.api.gateway.utils;

import com.api.gateway.dto.CompanyResponse;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.Map;
import java.util.function.Function;

@Singleton
public class GlobalUtils {
    private static final Logger logger = LoggerFactory.getLogger(GlobalUtils.class);
    /**
     * Company Info String To Json
     */
    public final Function<String, CompanyResponse> getCompanyInfoJson = company -> new Gson().fromJson(company,CompanyResponse.class);

    /**
     * JWT Payload get response
     */
    public final Function<Map<String,Object>,String> getCompanyInfoPayloadResponse = companyInfo -> {
        Map<String,Object> payload = (Map<String, Object>) companyInfo.get("payload");
        return payload.get("response").toString();
    };
}
