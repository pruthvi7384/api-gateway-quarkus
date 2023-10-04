package com.api.gateway.services;

import com.api.gateway.domain.Companies;
import com.api.gateway.dto.CompanyLogin;
import com.api.gateway.dto.CompanyResponse;
import com.api.gateway.dto.LoginResponse;
import com.api.gateway.repositories.CompaniesRepository;
import com.api.gateway.utils.GlobalConstant;
import com.api.gateway.utils.JwtUtil;
import com.api.gateway.utils.RSAUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.*;

@ApplicationScoped
public class CompanyService {
    private static final Logger logger = LoggerFactory.getLogger(CompanyService.class);
    @Inject
    RSAUtils rsaUtils;
    @Inject
    CompaniesRepository companiesRepository;
    @Inject
    JwtUtil jwtUtil;
    

    /**
     * Add Company Service
     */
    public Map<String,Object> addCompany(Companies requestBody){
        logger.info("CompanyService add request - {}",requestBody);
        Map<String,Object> response = new HashMap<>();
        Optional<Companies> companiesOptional = Optional.ofNullable(companiesRepository.findByUsername(requestBody.getUsername()));
        if (companiesOptional.isPresent()){
            response.put(GlobalConstant.STATUS,GlobalConstant.FAILURE);
            response.put("companyAddResponse","Company Already Added !");
        }else {
            requestBody.setName(rsaUtils.encryptMessage(requestBody.getName()));
            requestBody.setPassword(rsaUtils.encryptMessage(requestBody.getPassword()));
            requestBody.setEmailId(rsaUtils.encryptMessage(requestBody.getEmailId()));
            requestBody.setContactNumber(rsaUtils.encryptMessage(requestBody.getContactNumber()));
            requestBody.setCreatedDate(new Date());
            requestBody.setUpdatedDate(new Date());
            requestBody.setLastLoginDate(new Date());
            logger.info("CompanyService updated request - {}",requestBody);
            companiesRepository.persist(requestBody);
            response.put(GlobalConstant.STATUS,GlobalConstant.SUCCESS);
            response.put(GlobalConstant.RESPONSE,requestBody);
        }
        return response;
    }

    /**
     * Remove CompanyById Service
     */
    public Map<String,String> removeCompany(Long id){
        logger.info("Company remove request processed - {}",id);
        Map<String,String> response = new HashMap<>();
        boolean aBoolean = companiesRepository.deleteById(id);
        if (aBoolean){
            response.put(GlobalConstant.STATUS,GlobalConstant.SUCCESS);
            response.put(GlobalConstant.RESPONSE, "Company remove successfully");
        }else {
            response.put(GlobalConstant.STATUS,GlobalConstant.FAILURE);
            response.put(GlobalConstant.RESPONSE, "Something want wrong for company removing");
        }
        return response;
    }

    /**
     * Update CompanyById Service
     */
    public Map<String, Object> updateCompany(Companies requestBody, Long id){
        logger.info("CompanyService update plain request - {}",requestBody);
        Map<String,Object> response = new HashMap<>();
        Optional<Companies> companiesOptional = Optional.ofNullable(companiesRepository.findById(id));
        if (companiesOptional.isPresent()){
            Companies companies = companiesOptional.get();
            logger.info("CompanyService get company details by id - {}",companies);
            companies.setName(requestBody.getName().isEmpty() ? companies.getName() : rsaUtils.encryptMessage(requestBody.getName()));
            companies.setUsername(requestBody.getName().isEmpty() ? companies.getUsername() : requestBody.getUsername());
            companies.setEmailId(requestBody.getEmailId().isEmpty() ? companies.getEmailId() : rsaUtils.encryptMessage(requestBody.getEmailId()));
            companies.setContactNumber(requestBody.getContactNumber().isEmpty() ? companies.getContactNumber() : rsaUtils.encryptMessage(requestBody.getContactNumber()));
            companies.setUpdatedDate(new Date());

            logger.info("CompanyService updated company details - {}",companies);
            companiesRepository.persist(companies);
            response.put("companyUpdateResponse",companies);
            response.put(GlobalConstant.STATUS,GlobalConstant.SUCCESS);
        }else {
            response.put(GlobalConstant.STATUS,GlobalConstant.FAILURE);
            response.put(GlobalConstant.RESPONSE, "Something want wrong for company updating");
        }

        return response;
    }

    /**
     * Company Login Response
     */
    public Map<String,Object> login(CompanyLogin requestBody){
        logger.info("CompanyService login request received - {}",requestBody);
        Map<String,Object> response = new HashMap<>();
        Optional<Companies> companiesOptional = Optional.ofNullable(companiesRepository.findByUsername(requestBody.getUsername()));
        if (companiesOptional.isPresent()){
            Companies companies = companiesOptional.get();
            if (rsaUtils.decryptMessage(companies.getPassword()).equalsIgnoreCase(requestBody.getPassword())){
                companies.setLastLoginDate(new Date());
                companiesRepository.persist(companies);
                CompanyResponse companyResponse = new CompanyResponse();
                companyResponse.setId(companies.getId());
                companyResponse.setName(rsaUtils.decryptMessage(companies.getName()));
                companyResponse.setUsername(companies.getUsername());
                companyResponse.setEmailId(rsaUtils.decryptMessage(companies.getEmailId()));
                companyResponse.setContactNumber(rsaUtils.decryptMessage(companies.getContactNumber()));
                companyResponse.setCreatedDate(companies.getCreatedDate());
                companyResponse.setUpdatedDate(companies.getUpdatedDate());
                companyResponse.setLastLoginDate(companies.getLastLoginDate());
                String[] roles = {"USER","ADMIN"};
                String accessToken = jwtUtil.getSignedToken.apply(companyResponse, roles);
                LoginResponse loginResponse = new LoginResponse();
                loginResponse.setAccessToken(accessToken);
                response.put(GlobalConstant.STATUS,GlobalConstant.SUCCESS);
                response.put("companyLoginResponse",loginResponse);
            }
        }else{
            response.put(GlobalConstant.STATUS,GlobalConstant.FAILURE);
            response.put("companyLoginResponse", "Something want wrong");
        }
        return response;
    }

    /**
     *  Get All Company
     */
    public Map<String,Object> getAllCompany() {
        logger.info("CompanyService getAllCompany profile request received");
        Map<String,Object> response = new HashMap<>();
        List<Companies> companiesList = companiesRepository.listAll();
        if (companiesList.isEmpty()){
            response.put(GlobalConstant.STATUS,GlobalConstant.FAILURE);
            response.put("companyGetResponse", "Something want wrong (no one company found");
        }else {
            List<CompanyResponse> companyResponseList = new ArrayList<>();
            companiesList.parallelStream().forEach(companies -> {
                CompanyResponse companyResponse = new CompanyResponse();
                companyResponse.setId(companies.getId());
                companyResponse.setName(rsaUtils.decryptMessage(companies.getName()));
                companyResponse.setUsername(companies.getUsername());
                companyResponse.setEmailId(rsaUtils.decryptMessage(companies.getEmailId()));
                companyResponse.setContactNumber(rsaUtils.decryptMessage(companies.getContactNumber()));
                companyResponse.setCreatedDate(companies.getCreatedDate());
                companyResponse.setUpdatedDate(companies.getUpdatedDate());
                companyResponse.setLastLoginDate(companies.getLastLoginDate());
                companyResponseList.add(companyResponse);
            });
            response.put(GlobalConstant.STATUS,GlobalConstant.SUCCESS);
            response.put("companyGetResponse", companyResponseList);
        }
        return response;
    }

    /**
     *  Store Procedure Call
     */
    public Map<String,Object> getAll(){
        Map<String,Object> response = new HashMap<>();
        List list = companiesRepository.findAllByProcedure();
        List list1 = companiesRepository.findByUsernameProcedure("sam");
        logger.info("LIST COMPANIES USING STORE PROCEDURE - {}",list);
        logger.info("COMPANY FIND BY USERNAME USING STORE PROCEDURE - {}",list1);
        response.put(GlobalConstant.STATUS,GlobalConstant.SUCCESS);
        response.put("getCompanies - SP",list);
        response.put("getCompanyByUsername - SP",list1);
        return response;
    }
}

