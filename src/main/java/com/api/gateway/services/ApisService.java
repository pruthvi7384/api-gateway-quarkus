package com.api.gateway.services;

import com.api.gateway.domain.ApiSecurity;
import com.api.gateway.domain.Apis;
import com.api.gateway.domain.Projects;
import com.api.gateway.domain.ProxyApis;
import com.api.gateway.dto.ApiResponse;
import com.api.gateway.dto.CompanyResponse;
import com.api.gateway.dto.ProjectResponse;
import com.api.gateway.repositories.*;
import com.api.gateway.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.HttpHeaders;
import java.util.*;
import java.util.function.BiFunction;

@ApplicationScoped
public class ApisService {
    private static final Logger logger = LoggerFactory.getLogger(ApisService.class);
    private static final String VALID_REQUEST = "not valid project to you access";
    @Inject
    ApiRepository apiRepository;
    @Inject
    ProjectsRepository projectsRepository;
    @Inject
    ProxyApisRepository proxyApisRepository;
    @Inject
    ApiSecurityRepository apiSecurityRepository;
    @Inject
    LogsRepository logsRepository;
    @Inject
    JwtUtil jwtUtil;
    @Inject
    GlobalUtils globalUtils;
    @Inject
    AESUtils aesUtils;
    @Inject
    RSAUtils rsaUtils;


    /**
     *  Add Api Service
     */
    public Map<String,Object> addApi(Apis request, String authorization, Long id, HttpHeaders headers){
        Map<String,Object> response = new HashMap<>();
        String accessToken = authorization.substring(GlobalConstant.BEARER.length()).trim();
        logger.info(GlobalConstant.ACCESS_TOKEN,accessToken);
        Map<String,Object> tokenVerification = jwtUtil.verifyToken.apply(accessToken);
        logger.info(GlobalConstant.TOKEN_VERIFICATION_RESPONSE,tokenVerification);
        if(tokenVerification.get(GlobalConstant.STATUS).equals(GlobalConstant.FAILURE)){
            response = tokenVerification;
        }else{
            String payload = globalUtils.getCompanyInfoPayloadResponse.apply(tokenVerification);
            CompanyResponse companyResponse = globalUtils.getCompanyInfoJson.apply(payload);
            Optional<ProjectResponse> checkValidProject = projectsRepository.findByCompanyIdAndProjectId(companyResponse.getId(), id);
            if (checkValidProject.isPresent()){
                Optional<Apis> apisCheck = apiRepository.findApiByNameAndProjectId(request.getName(),checkValidProject.get().getId());
                if (apisCheck.isPresent()){
                    response.put(GlobalConstant.STATUS,GlobalConstant.FAILURE);
                    response.put(GlobalConstant.RESPONSE,"api already added");
                }else {
                    request.setName(aesUtils.encryptionAES(request.getName()));
                    request.setDescription(aesUtils.encryptionAES(request.getDescription()));
                    request.setApiUrl(aesUtils.encryptionAES(request.getApiUrl()));
                    request.setMethod(aesUtils.encryptionAES(request.getMethod()));
                    request.setHeaders(aesUtils.encryptionAES(request.getHeaders()));
                    request.setRequest(aesUtils.encryptionAES(request.getRequest()));

                    Projects project = new Projects();
                    project.setId(checkValidProject.get().getId());
                    request.setProjects(project);
                    request.setCreatedDate(new Date());
                    request.setUpdatedDate(new Date());

                    apiRepository.persist(request);

                    logger.info("company response - {}",companyResponse);
                    String url = "http://".concat(headers.getHeaderString("Host"))
                            .concat("/")
                            .concat(companyResponse.getUsername()).toLowerCase()
                            .concat("/")
                            .concat(checkValidProject.get().getName()).toLowerCase()
                            .concat("/")
                            .concat(aesUtils.decryptionAES(request.getName())).toLowerCase();
                    logger.info("ProxyApi URL - {}",url);

                    String requestBody = "{payload:".concat(aesUtils.decryptionAES(request.getRequest())).concat("}");

                    ProxyApis api = new ProxyApis();
                    api.setUrl(aesUtils.encryptionAES(url));
                    api.setMethod(aesUtils.encryptionAES("post"));
                    api.setHeaders(request.getHeaders());
                    api.setRequest(aesUtils.encryptionAES(requestBody));
                    api.setGeneratedAt(new Date());
                    api.setApis(request);
                    api.setStatus(true);

                    proxyApisRepository.persist(api);

                    ApiSecurity apiSecurity = new ApiSecurity();
                    apiSecurity.setName(rsaUtils.encryptMessage("security status"));
                    apiSecurity.setValue(rsaUtils.encryptMessage("false"));
                    apiSecurity.setApis(request);

                    ApiSecurity apiSecurity1 = new ApiSecurity();
                    apiSecurity1.setName(rsaUtils.encryptMessage("client id"));
                    apiSecurity1.setValue(rsaUtils.encryptMessage(aesUtils.encryptionAES("CLIENT_ID_".concat(companyResponse.getUsername()).concat("_").concat(checkValidProject.get().getName()).toLowerCase().concat("_").concat(aesUtils.decryptionAES(request.getName())).concat("_").concat(api.getId() == null ? "1" : api.getId().toString()))));
                    apiSecurity1.setApis(request);

                    apiSecurityRepository.persist(apiSecurity);
                    apiSecurityRepository.persist(apiSecurity1);

                    response.put(GlobalConstant.STATUS,GlobalConstant.SUCCESS);
                    response.put(GlobalConstant.RESPONSE,api);
                }
            }else {
                response.put(GlobalConstant.STATUS,GlobalConstant.FAILURE);
                response.put(GlobalConstant.RESPONSE,VALID_REQUEST);
            }
        }
        return response;
    }

    /**
     *  Views Api Service
     */
    public Map<String,Object> viewApis(String authorization, Long id){
        Map<String,Object> response = new HashMap<>();
        String accessToken = authorization.substring(GlobalConstant.BEARER.length()).trim();
        logger.info(GlobalConstant.ACCESS_TOKEN,accessToken);
        Map<String,Object> tokenVerification = jwtUtil.verifyToken.apply(accessToken);
        logger.info(GlobalConstant.TOKEN_VERIFICATION_RESPONSE,tokenVerification);
        if(tokenVerification.get(GlobalConstant.STATUS).equals(GlobalConstant.FAILURE)){
            response = tokenVerification;
        }else{
            String payload = globalUtils.getCompanyInfoPayloadResponse.apply(tokenVerification);
            CompanyResponse companyResponse = globalUtils.getCompanyInfoJson.apply(payload);
            Optional<ProjectResponse> checkValidProject = projectsRepository.findByCompanyIdAndProjectId(companyResponse.getId(), id);
            if (checkValidProject.isPresent()){
                List<ApiResponse> apis = apiRepository.findByProjectId(checkValidProject.get().getId());
                response.put(GlobalConstant.STATUS,GlobalConstant.SUCCESS);
                response.put(GlobalConstant.RESPONSE,apis);
            }else {
                response.put(GlobalConstant.STATUS,GlobalConstant.FAILURE);
                response.put(GlobalConstant.RESPONSE,VALID_REQUEST);
            }
        }
        return response;
    }

    /**
     *  View Api Service
     */
    public Map<String,Object> viewApiInformation(String authorization, Long projectId, Long id){
        Map<String,Object> response = new HashMap<>();
        String accessToken = authorization.substring(GlobalConstant.BEARER.length()).trim();
        logger.info(GlobalConstant.ACCESS_TOKEN,accessToken);
        Map<String,Object> tokenVerification = jwtUtil.verifyToken.apply(accessToken);
        logger.info(GlobalConstant.TOKEN_VERIFICATION_RESPONSE,tokenVerification);
        if(tokenVerification.get(GlobalConstant.STATUS).equals(GlobalConstant.FAILURE)){
            response = tokenVerification;
        }else{
            String payload = globalUtils.getCompanyInfoPayloadResponse.apply(tokenVerification);
            CompanyResponse companyResponse = globalUtils.getCompanyInfoJson.apply(payload);
            Optional<ProjectResponse> checkValidProject = projectsRepository.findByCompanyIdAndProjectId(companyResponse.getId(), projectId);
            if (checkValidProject.isPresent()){
                Optional<Apis> apis = Optional.ofNullable(apiRepository.findById(id));
                if (apis.isPresent()){
                    Apis api = apis.get();
                    ApiResponse apiResponse = new ApiResponse();
                    apiResponse.setId(api.getId());
                    apiResponse.setName(aesUtils.decryptionAES(api.getName()));
                    apiResponse.setDescription(aesUtils.decryptionAES(api.getDescription()));
                    apiResponse.setApiUrl(aesUtils.decryptionAES(api.getApiUrl()));
                    apiResponse.setMethod(aesUtils.decryptionAES(api.getMethod()));
                    apiResponse.setHeaders(aesUtils.decryptionAES(api.getHeaders()));
                    apiResponse.setRequest(aesUtils.decryptionAES(api.getRequest()));
                    apiResponse.setStatus(api.getStatus());
                    apiResponse.setRemoveStatus(api.getRemoveStatus());
                    apiResponse.setCreatedDate(api.getCreatedDate());
                    apiResponse.setUpdatedDate(api.getUpdatedDate());
                    apiResponse.setRemoveDate(api.getRemoveDate());
                    apiResponse.setProxyApiResponse(proxyApisRepository.findByApiId(api.getId()).get());
                    apiResponse.setApiSecurityResponses(apiSecurityRepository.findByApiId(api.getId()));
                    apiResponse.setLogResponses(logsRepository.findByApis(api.getId()));
                    response.put(GlobalConstant.STATUS,GlobalConstant.SUCCESS);
                    response.put(GlobalConstant.RESPONSE,apiResponse);
                }else{
                    response.put(GlobalConstant.STATUS,GlobalConstant.SUCCESS);
                    response.put(GlobalConstant.RESPONSE,"api details not found something is wrong");
                }
            }else {
                response.put(GlobalConstant.STATUS,GlobalConstant.FAILURE);
                response.put(GlobalConstant.RESPONSE,VALID_REQUEST);
            }
        }
        return response;
    }

    /**
     *  Update Api Service
     */
    public Map<String,Object> updateApi(String authorization, Long projectId, Long id, ApiResponse request){
        Map<String,Object> response = new HashMap<>();
        String accessToken = authorization.substring(GlobalConstant.BEARER.length()).trim();
        logger.info(GlobalConstant.ACCESS_TOKEN,accessToken);
        Map<String,Object> tokenVerification = jwtUtil.verifyToken.apply(accessToken);
        logger.info(GlobalConstant.TOKEN_VERIFICATION_RESPONSE,tokenVerification);
        if(tokenVerification.get(GlobalConstant.STATUS).equals(GlobalConstant.FAILURE)){
            response = tokenVerification;
        }else{
            String payload = globalUtils.getCompanyInfoPayloadResponse.apply(tokenVerification);
            CompanyResponse companyResponse = globalUtils.getCompanyInfoJson.apply(payload);
            Optional<ProjectResponse> checkValidProject = projectsRepository.findByCompanyIdAndProjectId(companyResponse.getId(), projectId);
            if (checkValidProject.isPresent()){
                Optional<Apis> apis = Optional.ofNullable(apiRepository.findById(id));
                response = checkApiName.apply(apis,request);
            }else {
                response.put(GlobalConstant.STATUS,GlobalConstant.FAILURE);
                response.put(GlobalConstant.RESPONSE,VALID_REQUEST);
            }
        }
        return response;
    }

    BiFunction<Optional<Apis>,ApiResponse,Map<String,Object>> checkApiName = (apis, request) -> {
        Map<String,Object> response = new HashMap<>();
        if (apis.isPresent()){
            Apis api = apis.get();
            Optional<Apis> checkCount = apiRepository.findApiByNameAndProjectId(request.getName(),api.getProjects().getId());
            if (checkCount.isEmpty()){
                api.setName(request.getName().isEmpty() ? api.getName() : aesUtils.encryptionAES(request.getName()));
                api.setDescription(request.getDescription().isEmpty() ? api.getDescription() : aesUtils.encryptionAES(request.getDescription()));
                api.setApiUrl(request.getApiUrl().isEmpty() ? api.getApiUrl() : aesUtils.encryptionAES(request.getApiUrl()));
                api.setMethod(request.getMethod().isEmpty() ? api.getMethod() : aesUtils.encryptionAES(request.getMethod()));
                api.setHeaders(request.getHeaders().isEmpty() ? api.getHeaders() : aesUtils.encryptionAES(request.getHeaders()));
                api.setRequest(request.getRequest().isEmpty() ? api.getRequest() : aesUtils.encryptionAES(request.getRequest()));
                api.setUpdatedDate(new Date());
                apiRepository.persist(api);

                request.getApiSecurityResponses().forEach(apiSecurityResponse -> {
                    Optional<ApiSecurity> checkApiSecurity = apiSecurityRepository.findByApiIdAndName(api.getId(),apiSecurityResponse.getName());
                    if (checkApiSecurity.isPresent()){
                        ApiSecurity apiSecurity = checkApiSecurity.get();
                        apiSecurity.setValue(rsaUtils.encryptMessage(apiSecurityResponse.getValue()));
                        apiSecurityRepository.persist(apiSecurity);
                    }
                });

                response.put(GlobalConstant.STATUS,GlobalConstant.SUCCESS);
                response.put(GlobalConstant.RESPONSE,"api details updated successfully");
            } else {
                Apis checkApi = checkCount.get();
                if(
                     aesUtils.decryptionAES(checkApi.getName()).equalsIgnoreCase(request.getName()) &&
                             aesUtils.decryptionAES(checkApi.getDescription()).equalsIgnoreCase(request.getDescription()) &&
                             aesUtils.decryptionAES(checkApi.getApiUrl()).equalsIgnoreCase(request.getApiUrl()) &&
                             aesUtils.decryptionAES(checkApi.getMethod()).equalsIgnoreCase(request.getMethod()) &&
                             aesUtils.decryptionAES(checkApi.getHeaders()).equalsIgnoreCase(request.getHeaders()) &&
                             aesUtils.decryptionAES(checkApi.getRequest()).equalsIgnoreCase(request.getRequest()) &&
                             request.getApiSecurityResponses().isEmpty()
                ){
                    response.put(GlobalConstant.STATUS,GlobalConstant.FAILURE);
                    response.put(GlobalConstant.RESPONSE,"api name already present");
                }else {
                    api.setDescription(request.getDescription().isEmpty() ? api.getDescription() : aesUtils.encryptionAES(request.getDescription()));
                    api.setApiUrl(request.getApiUrl().isEmpty() ? api.getApiUrl() : aesUtils.encryptionAES(request.getApiUrl()));
                    api.setMethod(request.getMethod().isEmpty() ? api.getMethod() : aesUtils.encryptionAES(request.getMethod()));
                    api.setHeaders(request.getHeaders().isEmpty() ? api.getHeaders() : aesUtils.encryptionAES(request.getHeaders()));
                    api.setRequest(request.getRequest().isEmpty() ? api.getRequest() : aesUtils.encryptionAES(request.getRequest()));
                    api.setUpdatedDate(new Date());
                    apiRepository.persist(api);

                    request.getApiSecurityResponses().forEach(apiSecurityResponse -> {
                        Optional<ApiSecurity> checkApiSecurity = apiSecurityRepository.findByApiIdAndName(api.getId(),apiSecurityResponse.getName());
                        if (checkApiSecurity.isPresent()){
                            ApiSecurity apiSecurity = checkApiSecurity.get();
                            apiSecurity.setValue(rsaUtils.encryptMessage(apiSecurityResponse.getValue()));
                            apiSecurityRepository.persist(apiSecurity);
                        }
                    });
                    response.put(GlobalConstant.STATUS,GlobalConstant.SUCCESS);
                    response.put(GlobalConstant.RESPONSE,"api details updated successfully");
                }
            }
        }else{
            response.put(GlobalConstant.STATUS,GlobalConstant.SUCCESS);
            response.put(GlobalConstant.RESPONSE,"api details not found something is wrong");
        }
        return response;
    };

    /**
     *  Soft delete Api Service
     */
    public Map<String,Object>softRemoveApi(String authorization, Long projectId, Long id){
        Map<String,Object> response = new HashMap<>();
        String accessToken = authorization.substring(GlobalConstant.BEARER.length()).trim();
        logger.info(GlobalConstant.ACCESS_TOKEN,accessToken);
        Map<String,Object> tokenVerification = jwtUtil.verifyToken.apply(accessToken);
        logger.info(GlobalConstant.TOKEN_VERIFICATION_RESPONSE,tokenVerification);
        if(tokenVerification.get(GlobalConstant.STATUS).equals(GlobalConstant.FAILURE)){
            response = tokenVerification;
        }else{
            String payload = globalUtils.getCompanyInfoPayloadResponse.apply(tokenVerification);
            CompanyResponse companyResponse = globalUtils.getCompanyInfoJson.apply(payload);
            Optional<ProjectResponse> checkValidProject = projectsRepository.findByCompanyIdAndProjectId(companyResponse.getId(), projectId);
            if (checkValidProject.isPresent()){
                Optional<Apis> apis = Optional.ofNullable(apiRepository.findById(id));
                if (apis.isPresent()){
                    apis.get().setRemoveStatus(true);
                    apis.get().setRemoveDate(new Date());
                    apiRepository.persist(apis.get());
                response.put(GlobalConstant.STATUS,GlobalConstant.SUCCESS);
                    response.put(GlobalConstant.RESPONSE,"api removed successfully");
                }else {
                    response.put(GlobalConstant.STATUS,GlobalConstant.FAILURE);
                    response.put(GlobalConstant.RESPONSE,"no api information present");
                }
            }else {
                response.put(GlobalConstant.STATUS,GlobalConstant.FAILURE);
                response.put(GlobalConstant.RESPONSE,VALID_REQUEST);
            }
        }
        return response;
    }
    /**
     *  Hard delete Api Service
     */
    public Map<String,Object>hardRemoveApi(String authorization, Long projectId, Long id){
        Map<String,Object> response = new HashMap<>();
        String accessToken = authorization.substring(GlobalConstant.BEARER.length()).trim();
        logger.info(GlobalConstant.ACCESS_TOKEN,accessToken);
        Map<String,Object> tokenVerification = jwtUtil.verifyToken.apply(accessToken);
        logger.info(GlobalConstant.TOKEN_VERIFICATION_RESPONSE,tokenVerification);
        if(tokenVerification.get(GlobalConstant.STATUS).equals(GlobalConstant.FAILURE)){
            response = tokenVerification;
        }else{
            String payload = globalUtils.getCompanyInfoPayloadResponse.apply(tokenVerification);
            CompanyResponse companyResponse = globalUtils.getCompanyInfoJson.apply(payload);
            Optional<ProjectResponse> checkValidProject = projectsRepository.findByCompanyIdAndProjectId(companyResponse.getId(), projectId);
            if (checkValidProject.isPresent()){
                Optional<Apis> apis = Optional.ofNullable(apiRepository.findById(id));
                if (apis.isPresent()){
                    apiRepository.delete(apis.get());
                    response.put(GlobalConstant.STATUS,GlobalConstant.SUCCESS);
                    response.put(GlobalConstant.RESPONSE,"api removed successfully");
                }else {
                    response.put(GlobalConstant.STATUS,GlobalConstant.FAILURE);
                    response.put(GlobalConstant.RESPONSE,"no api information present");
                }
            }else {
                response.put(GlobalConstant.STATUS,GlobalConstant.FAILURE);
                response.put(GlobalConstant.RESPONSE,VALID_REQUEST);
            }
        }
        return response;
    }
}
