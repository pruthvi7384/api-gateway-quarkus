package com.api.gateway.utils;

import com.api.gateway.dto.CompanyResponse;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

@Singleton
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);
    @Inject
    RSAUtils rsaUtils;

    @Inject
    AESUtils aesUtils;

    /**
     * JWT Token Generate
     */
    public final BiFunction<CompanyResponse,String[],String> getSignedToken = (companyResponse, strings) -> {
        logger.info("For JWT Token Sign Request Received Request - {} Roles - {}", companyResponse,strings);
        int duration = 120;
        java.security.Security.addProvider(new BouncyCastleProvider());
        PublicKey publicKey = null;
        PrivateKey privateKey = null;
        try {
            publicKey = rsaUtils.getPublicKey();
            privateKey = rsaUtils.getPrivateKey();
        }catch (Exception e){
            logger.info("JWT Error - ",e);
        }
        Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) publicKey, (RSAPrivateKey) privateKey);
        String token = "";
        token =  JWT.create()
                .withIssuer("API_GATEWAY")
                .withSubject(companyResponse.getEmailId())
                .withArrayClaim("groups",strings)
                .withClaim("response",companyResponse.toString())
                .withExpiresAt(Date.from(ZonedDateTime.now(ZoneId.systemDefault()).plusMinutes(duration).toInstant()))
                .sign(algorithm);
        logger.info("generated token={}",token);
        return aesUtils.encryptionAES(token);
    };

    /**
     * Verify JWT Token
     */
    public final Function<String, Map<String,Object>> verifyToken = token ->{
        String decryptedToken = aesUtils.decryptionAES(token);
        Map<String,Object> response;
        logger.info("DECRYPTED ACCESS TOKEN - {}", decryptedToken);
        java.security.Security.addProvider(new BouncyCastleProvider());
        PublicKey publicKey = null;
        PrivateKey privateKey = null;
        try {
            publicKey = rsaUtils.getPublicKey();
            privateKey = rsaUtils.getPrivateKey();
        }catch (Exception e){
            logger.info("JWT Error - ",e);
        }
        Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) publicKey, (RSAPrivateKey) privateKey);
        try {
            boolean aBoolean = isJWTExpired.test(decryptedToken);
            response = new HashMap<>();
            if (aBoolean){
                DecodedJWT jwt = JWT.require(algorithm).withIssuer("API_GATEWAY").build().verify(decryptedToken);
                String json = new String(Base64.getDecoder().decode(jwt.getPayload()));
                Map<String, Object> payload = new Gson().fromJson(json, new TypeToken<Map<String, Object>>() {}.getType());
                response.put(GlobalConstant.STATUS,GlobalConstant.SUCCESS);
                response.put("payload", payload);
            }else {
                response.put(GlobalConstant.STATUS,GlobalConstant.FAILURE);
                response.put("message","ACCESS TOKEN EXPIRED");
            }
            return response;
        }catch (JWTVerificationException e){
            logger.error(e.getMessage());
        }

        Map<String,Object> payload = new HashMap<>();
        CompanyResponse companyResponse = new CompanyResponse();
        companyResponse.setContactNumber("+91 7720993937");
        companyResponse.setName("SAM");
        companyResponse.setUsername("SAM");
        companyResponse.setEmailId("sam@gmail.com");
        companyResponse.setId(1L);

        payload.put("response",companyResponse);

        response = new HashMap<>();
        response.put(GlobalConstant.STATUS,GlobalConstant.SUCCESS);
        response.put("payload", payload);
        return response;
    };
    static Predicate<String> isJWTExpired = token -> {
        DecodedJWT decodedJWT = JWT.decode(token);
        Date expiresAt = decodedJWT.getExpiresAt();
       return expiresAt.after(new Date());
    };

    private JwtUtil(){}
}
