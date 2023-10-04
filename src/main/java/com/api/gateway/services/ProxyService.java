package com.api.gateway.services;

import com.api.gateway.domain.Logs;
import com.api.gateway.domain.Apis;
import com.api.gateway.domain.Companies;
import com.api.gateway.domain.Projects;
import com.api.gateway.dto.ApiSecurityResponse;
import com.api.gateway.dto.ProxyRequest;
import com.api.gateway.dto.ProxyResponse;
import com.api.gateway.dto.ProxySet;
import com.api.gateway.repositories.*;
import com.api.gateway.utils.AESUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;
import javax.ws.rs.core.HttpHeaders;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * <a href="https://www.baeldung.com/gson-string-to-jsonobject">...</a>
 */

@ApplicationScoped
public class ProxyService {
    private static final Logger logger = LoggerFactory.getLogger(ProxyService.class);
    private static final String ERROR_COUNT = "errorCount";
    private static final String RESPONSE_CODE = "responseCode";
    @Inject
    CompaniesRepository companiesRepository;
    @Inject
    ProjectsRepository projectsRepository;
    @Inject
    ApiRepository apiRepository;
    @Inject
    ApiSecurityRepository apiSecurityRepository;
    @Inject
    AESUtils aesUtils;

    @Inject
    LogsRepository logsRepository;

    /**
     *  Proxy Server
     */
    public ProxyResponse proxyService(ProxyRequest request, HttpHeaders httpHeaders, String companyName, String projectName, String apiName){
        logger.info("Proxy Service processing");
        ProxySet proxySet = new ProxySet();
        ProxyResponse proxyResponse = new ProxyResponse();
        Optional<Companies> companyCheck = Optional.ofNullable(companiesRepository.findByUsername(companyName));
        if (companyCheck.isPresent()){
            Optional<Projects> projectCheck = Optional.ofNullable(projectsRepository.findByNameAndCompanyId(projectName, companyCheck.get().getId()));
            if(projectCheck.isPresent()){
                Optional<Apis> apiCheck = apiRepository.findApiByNameAndProjectId(apiName,projectCheck.get().getId());
                if (apiCheck.isPresent()){
                    Optional<ApiSecurityResponse> security = apiSecurityRepository.findByNameApis(apiCheck.get().getId(),"security status");
                    if (security.isPresent()){
                        try {
                            Apis apis = apiCheck.get();
                            proxySet.setApisId(apis.getId());
                            proxySet.setApiUrl(aesUtils.decryptionAES(apis.getApiUrl()));
                            proxySet.setMethod(aesUtils.decryptionAES(apis.getMethod()).toUpperCase());
                            proxySet.setHeaderTemplate(new ObjectMapper().readTree(aesUtils.decryptionAES(apis.getHeaders()).replace("'","\"")));
                            proxySet.setRequestTemplate(new ObjectMapper().readTree(aesUtils.decryptionAES(apis.getRequest()).replace("'","\"")));
                            proxySet.setAppRequest(new ObjectMapper().readTree(request.getPayload().replace("'","\"")));
                            proxySet.setAppHeaders(new ObjectMapper().readTree(httpheadersToJsonNode.apply(httpHeaders)));

                            if(security.get().getValue().equalsIgnoreCase("true")){
                                Optional<ApiSecurityResponse> verifyClientId = apiSecurityRepository.findByNameApisClientId(apiCheck.get().getId(),"client id");
                                if (verifyClientId.isPresent()){
                                    logger.info("Client Id  - {}",verifyClientId.get());
                                    if (aesUtils.decryptionAES(verifyClientId.get().getValue()).equalsIgnoreCase(aesUtils.decryptionAES(httpHeaders.getHeaderString("clientId")))){
                                        proxyResponse = callProxyToServer.apply(request,proxySet);
                                    }else {
                                        proxyResponse.setErrorCode("403");
                                        proxyResponse.setErrorMessage("'client id' not be valid");
                                    }
                                }else{
                                    proxyResponse.setErrorCode("400");
                                    proxyResponse.setErrorMessage("'client id' not be configured");
                                }
                            }else {
                                proxyResponse = callProxyToServer.apply(request,proxySet);
                            }
                        }catch (JsonProcessingException e){
                            logger.error("JsonProcessing Error - {}",e.getMessage());
                        }
                    }
                }else {
                    proxyResponse.setErrorCode("403");
                    proxyResponse.setErrorMessage("not access (api is not valid)");
                }
            }else{
                proxyResponse.setErrorCode("403");
                proxyResponse.setErrorMessage("not access (project is not valid)");
            }
        }else {
            proxyResponse.setErrorCode("403");
            proxyResponse.setErrorMessage("not access (company is not valid)");
        }
        return proxyResponse;
    }

    /**
     * Proxy Service Process
     */

    BiFunction<ProxyRequest, ProxySet, ProxyResponse> callProxyToServer = (request, proxySet) -> {
        ProxyResponse proxyResponse = new ProxyResponse();
        List<String> requestApiTemplateKey = keysGetFromJsonNode.apply(proxySet.getRequestTemplate());
        logger.info("\nAPI Request Key's - {}",requestApiTemplateKey);

        List<String> headersApiTemplateKey = keysGetFromJsonNode.apply(proxySet.getHeaderTemplate());
        logger.info("\nAPI Headers Key's - {}",headersApiTemplateKey);

        Map<String,Object> validateRequestBodyResponse = validateRequestBody.apply(requestApiTemplateKey,proxySet.getAppRequest());
        logger.info("Validate Request APP AND API - {}",validateRequestBodyResponse);

        Map<String,Object> validateRequestHeaderResponse = validateRequestHeaders.apply(headersApiTemplateKey,proxySet.getAppHeaders());
        logger.info("Validate Request Headers APP AND API - {}",validateRequestHeaderResponse);

        if(validateRequestBodyResponse.get(ERROR_COUNT).equals(0) && validateRequestHeaderResponse.get(ERROR_COUNT).equals(0)){
            proxySet.setHeadersFields(headersApiTemplateKey);
            if (proxySet.getApiUrl().startsWith("http")){
                proxyResponse.setProxyServerResponse(callBackendServerProxyHTTP(proxySet));
            }else {
                proxyResponse.setProxyServerResponse(callBackendServerProxyHTTPS(proxySet));
            }
            proxyResponse.setErrorCode(proxyResponse.getProxyServerResponse().get(RESPONSE_CODE).toString());
            proxyResponse.setErrorMessage("api call successfully using proxy server");
        }else{
            proxyResponse.setErrorCode("400");
            proxyResponse.setErrorMessage(validateRequestBodyResponse.toString().concat(" || ").concat(validateRequestHeaderResponse.toString()));
        }
        return proxyResponse;
    };

    /**
     * JsonNode find keys
     */
    static Function<JsonNode, List<String>> keysGetFromJsonNode = jsonNode -> {
        List<String> keys = new ArrayList<>();
        Iterator<String> iterator = jsonNode.fieldNames();
        iterator.forEachRemaining(keys::add);
        return keys;
    };

    /**
     * Validate API request body and APP request body
     */
    static BiFunction<List<String>,JsonNode, Map<String,Object>> validateRequestBody = (apiKeys, appRequest) -> {
        Map<String,Object> response = new HashMap<>();
        AtomicInteger count = new AtomicInteger();
        List<String> errorRequest = new ArrayList<>();
        for (String key : apiKeys){
            String value = appRequest.findPath(key).isMissingNode() ? "" : appRequest.findPath(key).textValue();
            if (value.equals("")){
                logger.info(key);
                errorRequest.add(key);
                count.getAndIncrement();
            }
        }
        response.put(ERROR_COUNT,count.get());
        response.put("keysMissing",errorRequest);
        response.put("message","bad request keys messing in app request");
        return response;
    };

    /**
     * Validate API request headers and APP request headers
     */
    static BiFunction<List<String>, JsonNode, Map<String,Object>> validateRequestHeaders = (apiKeys, appRequestHeaders) -> {
        Map<String,Object> response = new HashMap<>();
        AtomicInteger count = new AtomicInteger();
        List<String> errorRequest = new ArrayList<>();
        for (String key : apiKeys){
            String value = appRequestHeaders.findPath(key).isMissingNode() ? "" : appRequestHeaders.findPath(key).textValue();
            if (value.equals("")){
                logger.info(key);
                errorRequest.add(key);
                count.getAndIncrement();
            }
        }
        response.put(ERROR_COUNT,count.get());
        response.put("keysMissing",errorRequest);
        response.put("message","bad request keys messing in app request headers");
        return response;
    };

    /**
     * Headers covert to json node
     */
    static Function<HttpHeaders,String> httpheadersToJsonNode = httpHeaders -> {
            Map<String,String> headers = new HashMap<>();
            httpHeaders.getRequestHeaders().forEach((s, strings) -> headers.put("\""+s+"\"","\""+strings.get(0)+"\""));
            return headers.toString().replace("=",":");
    };

    /**
     * Call Backend Server - HTTP
     */

    public JsonNode callBackendServerProxyHTTP(ProxySet proxyResponse){
        logger.info("====================== PROXY SERVER CALLING START =======================");
        logger.info("====================== PROXY REQUEST =====================\n-{}",proxyResponse);
        // API LOGS STORE
        Apis apis = new Apis();
        apis.setId(proxyResponse.getApisId());
        Logs log = new Logs();
        log.setApis(apis);

        try {
           URL url = new URL(proxyResponse.getApiUrl());
           HttpURLConnection con = (HttpURLConnection) url.openConnection();
           proxyResponse.getHeadersFields().forEach(s -> con.setRequestProperty(s,proxyResponse.getAppHeaders().get(s).textValue()));

           con.setRequestMethod(proxyResponse.getMethod());

           log.setHeaders(aesUtils.encryptionAES(con.getRequestProperties().toString()));

           if (methodCheck.test(proxyResponse.getMethod())) {
               con.setDoOutput(true);
               con.setInstanceFollowRedirects(false);
               DataOutputStream wr = new DataOutputStream(con.getOutputStream());
               wr.writeBytes(proxyResponse.getAppRequest().toString());
               wr.flush();
               wr.close();
               log.setRequestBody(aesUtils.encryptionAES(proxyResponse.getAppRequest().toString()));
           } else if("GET".equalsIgnoreCase(proxyResponse.getMethod())) {
               con.setDoOutput(false);
               log.setRequestBody(aesUtils.encryptionAES(null));
           }

            int responseCode = 0;
            InputStream inputStream = null;
            String error = "";

            try {
                responseCode = con.getResponseCode();
                if (responseCode == 200 || responseCode == 201) {
                    inputStream = con.getInputStream();
                } else {
                    inputStream = con.getErrorStream();
                }
            } catch (Exception e) {
                error = e.getMessage();
                e.printStackTrace();
            }

            StringBuilder response = new StringBuilder();

            if (inputStream != null) {
                BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                con.disconnect();
            }else {
                Map<String,String> responseError = new HashMap<>();
                responseError.put("errorDescription",error);
                response.append(responseError.toString().replace("=",":"));
            }
            Map<String,Object> responseFinal = new HashMap<>();
            responseFinal.put("\""+RESPONSE_CODE+"\"",responseCode);
            responseFinal.put("\""+"response"+"\"",response);

            log.setResponse(aesUtils.encryptionAES(new ObjectMapper().readTree(responseFinal.toString().replace("=",":")).toString()));
            log.setLogDate(new Date());

            logger.info("MS LOGS - {}",log);

            logsRepository.persist(log);

            return new ObjectMapper().readTree(responseFinal.toString().replace("=",":"));

       }catch (Exception e){
           logger.error("call backend server proxy error - ",e);
       }
        return null;
    }

    /**
     * Call Backend Server - HTTPS
     */

    public JsonNode callBackendServerProxyHTTPS(ProxySet proxyResponse){
        logger.info("====================== PROXY SERVER CALLING START =======================");
        logger.info("====================== PROXY REQUEST =====================\n-{}",proxyResponse);

        // API LOGS STORE
        Apis apis = new Apis();
        apis.setId(proxyResponse.getApisId());
        Logs log = new Logs();
        log.setApis(apis);

        try {
            URL url = new URL(proxyResponse.getApiUrl());
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            proxyResponse.getHeadersFields().forEach(s -> con.setRequestProperty(s,proxyResponse.getAppHeaders().get(s).textValue()));

            con.setRequestMethod(proxyResponse.getMethod());

            log.setHeaders(aesUtils.encryptionAES(con.getRequestProperties().toString()));

            if (methodCheck.test(proxyResponse.getMethod())) {
                con.setDoOutput(true);
                con.setInstanceFollowRedirects(false);
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes(proxyResponse.getAppRequest().toString());
                wr.flush();
                wr.close();
                log.setRequestBody(aesUtils.encryptionAES(proxyResponse.getAppRequest().toString()));
            } else if("GET".equalsIgnoreCase(proxyResponse.getMethod())) {
                con.setDoOutput(false);
                log.setRequestBody(aesUtils.encryptionAES(null));
            }

            int responseCode = 0;
            InputStream inputStream = null;
            String error = "";

            try {
                responseCode = con.getResponseCode();
                if (responseCode == 200 || responseCode == 201) {
                    inputStream = con.getInputStream();
                } else {
                    inputStream = con.getErrorStream();
                }
            } catch (Exception e) {
                error = e.getMessage();
                e.printStackTrace();
            }

            StringBuilder response = new StringBuilder();

            if (inputStream != null) {
                BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                con.disconnect();
            }else {
                Map<String,String> responseError = new HashMap<>();
                responseError.put("errorDescription",error);
                response.append(responseError.toString().replace("=",":"));
            }
            Map<String,Object> responseFinal = new HashMap<>();
            responseFinal.put("\""+RESPONSE_CODE+"\"",responseCode);
            responseFinal.put("\""+"response"+"\"",response);

            log.setResponse(aesUtils.encryptionAES(new ObjectMapper().readTree(responseFinal.toString().replace("=",":")).toString()));
            log.setLogDate(new Date());

            logsRepository.persist(log);

            return new ObjectMapper().readTree(responseFinal.toString().replace("=",":"));

        }catch (Exception e){
            logger.error("call backend server proxy error - ",e);
        }
        return null;
    }

    /**
     * Method Check
     */
    static Predicate<String> methodCheck = s -> "POST".equalsIgnoreCase(s) || "PUT".equalsIgnoreCase(s) || "DELETE".equalsIgnoreCase(s);
}

