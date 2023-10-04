package com.api.gateway.services;

import com.api.gateway.domain.Companies;
import com.api.gateway.domain.Projects;
import com.api.gateway.dto.CompanyResponse;
import com.api.gateway.dto.ProjectResponse;
import com.api.gateway.repositories.ProjectsRepository;
import com.api.gateway.utils.GlobalConstant;
import com.api.gateway.utils.GlobalUtils;
import com.api.gateway.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.*;
import java.util.function.BiFunction;

@ApplicationScoped
public class ProjectsService {
    @Inject
    JwtUtil jwtUtil;
    private static final Logger logger = LoggerFactory.getLogger(ProjectsService.class);
    @Inject
    ProjectsRepository projectsRepository;

    @Inject
    GlobalUtils globalUtils;
    /**
     * Add project service
     */
    public Map<String,Object> addProject(String authorization, Projects requestBody){
        Map<String,Object> response;
        String accessToken = authorization.substring(GlobalConstant.BEARER.length()).trim();
        logger.info(GlobalConstant.ACCESS_TOKEN,accessToken);
        Map<String,Object> tokenVerification = jwtUtil.verifyToken.apply(accessToken);
        logger.info(GlobalConstant.TOKEN_VERIFICATION_RESPONSE,tokenVerification);
        if(tokenVerification.get(GlobalConstant.STATUS).equals(GlobalConstant.FAILURE)){
            response = tokenVerification;
        }else{
            response = addProjectFunction.apply(requestBody, globalUtils.getCompanyInfoPayloadResponse.apply(tokenVerification));
        }
        return response;
    }

    BiFunction<Projects,String,Map<String,Object>> addProjectFunction = (requestBody, company) -> {
        logger.info("Company Response String - {}",company);
        Map<String,Object> response = new HashMap<>();
        CompanyResponse companyResponse = globalUtils.getCompanyInfoJson.apply(company);
        logger.info("CompanyProfile from access token - {}",companyResponse);
        Optional<Projects> projectCheck = Optional.ofNullable(projectsRepository.findByNameAndCompanyId(requestBody.getName(), companyResponse.getId()));
        if(projectCheck.isPresent()){
            response.put(GlobalConstant.STATUS,GlobalConstant.FAILURE);
            response.put(GlobalConstant.RESPONSE,"project already added");
        }else {
            Companies companies = new Companies();
            companies.setId(companyResponse.getId());

            requestBody.setCompanies(companies);
            requestBody.setStatus(true);
            requestBody.setCreatedDate(new Date());
            requestBody.setUpdatedDate(new Date());

            projectsRepository.persist(requestBody);
            response.put(GlobalConstant.RESPONSE,requestBody);
            response.put(GlobalConstant.STATUS,GlobalConstant.SUCCESS);
        }
        return response;
    };

    /**
     *  View projects service
     */
    public Map<String,Object> viewProjects(String authorization){
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
             Optional<List<ProjectResponse>> optionalProjects = Optional.ofNullable(projectsRepository.findByCompanyId(companyResponse.getId()));
             if (optionalProjects.isPresent()){
                 response.put(GlobalConstant.STATUS,GlobalConstant.SUCCESS);
                 response.put(GlobalConstant.RESPONSE,optionalProjects.get());
             }else {
                 response.put(GlobalConstant.STATUS,GlobalConstant.FAILURE);
                 response.put(GlobalConstant.RESPONSE,"no any project added");
             }
        }
        return response;
    }

    /**
     *  View project profile
     */
    public Map<String,Object> viewProjectSpecific(String authorization, Long id){
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
            Optional<ProjectResponse> optionalProjects = projectsRepository.findByCompanyIdAndProjectId(companyResponse.getId(),id);
            if (optionalProjects.isPresent()){
                response.put(GlobalConstant.STATUS,GlobalConstant.SUCCESS);
                response.put(GlobalConstant.RESPONSE,optionalProjects.get());
            }else {
                response.put(GlobalConstant.STATUS,GlobalConstant.FAILURE);
                response.put(GlobalConstant.RESPONSE,"no any project profile found");
            }
        }
        return response;
    }

    /**
     * Update Project Service
     */
    public Map<String,Object> updateProject(Projects request,String authorization, Long id){
        Map<String,Object> response;
        String accessToken = authorization.substring(GlobalConstant.BEARER.length()).trim();
        logger.info(GlobalConstant.ACCESS_TOKEN,accessToken);
        Map<String,Object> tokenVerification = jwtUtil.verifyToken.apply(accessToken);
        logger.info(GlobalConstant.TOKEN_VERIFICATION_RESPONSE,tokenVerification);
        response = updateCompanyDetails(tokenVerification,request,id);
        return response;
    }

    public Map<String,Object> updateCompanyDetails(Map<String,Object> tokenVerification,Projects request, Long id){
        Map<String,Object> response = new HashMap<>();
        if(tokenVerification.get(GlobalConstant.STATUS).equals(GlobalConstant.FAILURE)){
            response = tokenVerification;
        }else{
            String payload = globalUtils.getCompanyInfoPayloadResponse.apply(tokenVerification);
            CompanyResponse companyResponse = globalUtils.getCompanyInfoJson.apply(payload);
            Optional<Projects> optionalProjects = Optional.ofNullable(projectsRepository.findByCompanyIdAndProjectIdSingle(companyResponse.getId(), id));
            if (optionalProjects.isPresent()){
                Projects project = optionalProjects.get();
                response = updateRecord.apply(project,request);
            }else {
                response.put(GlobalConstant.STATUS,GlobalConstant.FAILURE);
                response.put(GlobalConstant.RESPONSE,"no any project profile found");
            }
        }
        return response;
    }

    BiFunction<Projects,Projects,Map<String,Object> > updateRecord = ( project, request) -> {
        Map<String,Object> response = new HashMap<>();
        Long checkCountName = projectsRepository.findByNameAndCompanyIdCount(request.getName(), project.getCompanies().getId());
        Long checkCountAll = projectsRepository.findByAllParameterCount(request.getName(), project.getCompanies().getId(),request.getInformation());
        if (checkCountName == 0){
            project.setName(request.getName().isEmpty() ? project.getName() : request.getName());
            project.setInformation(request.getInformation().isEmpty() ? project.getInformation() : request.getInformation());
            project.setUpdatedDate(new Date());
            projectsRepository.persist(project);
            response.put(GlobalConstant.STATUS,GlobalConstant.SUCCESS);
            response.put(GlobalConstant.RESPONSE,"project details updated successfully");
        } else if (checkCountAll == 0) {
            project.setInformation(request.getInformation().isEmpty() ? project.getInformation() : request.getInformation());
            project.setUpdatedDate(new Date());
            projectsRepository.persist(project);
            response.put(GlobalConstant.STATUS,GlobalConstant.SUCCESS);
            response.put(GlobalConstant.RESPONSE,"project details updated successfully");
        } else {
            response.put(GlobalConstant.STATUS,GlobalConstant.FAILURE);
            response.put(GlobalConstant.RESPONSE,"project name already present");
        }
        return response;
    };

    /**
     * Remove project service
     */
    public Map<String,Object> removeProject(String authorization,Long id){
        Map<String,Object> response = new HashMap<>();
        String accessToken = authorization.substring(GlobalConstant.BEARER.length()).trim();
        logger.info(GlobalConstant.ACCESS_TOKEN,accessToken);
        Map<String,Object> tokenVerification = jwtUtil.verifyToken.apply(accessToken);
        logger.info(GlobalConstant.TOKEN_VERIFICATION_RESPONSE,tokenVerification);
        String payload = globalUtils.getCompanyInfoPayloadResponse.apply(tokenVerification);
        CompanyResponse companyResponse = globalUtils.getCompanyInfoJson.apply(payload);
        Optional<ProjectResponse> checkOptional = projectsRepository.findByCompanyIdAndProjectId(companyResponse.getId(), id);
        if (checkOptional.isPresent()){
            projectsRepository.deleteById(id);
            response.put(GlobalConstant.STATUS,GlobalConstant.SUCCESS);
            response.put(GlobalConstant.RESPONSE,"project removed successfully");
        }else {
            response.put(GlobalConstant.STATUS,GlobalConstant.FAILURE);
            response.put(GlobalConstant.RESPONSE,"project not be present");
        }
        return response;
    }

}
