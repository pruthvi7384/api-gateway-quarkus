package com.api.gateway.repositories;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

@QuarkusTest
class ApiSecurityRepositoryTest {
    @Inject
    ApiSecurityRepository apiSecurityRepository;

    @Test
    void test(){
        Assertions.assertNotNull(apiSecurityRepository.findByApiIdAndName(1L,"SAM"));
        Assertions.assertNotNull(apiSecurityRepository.findByNameApis(1L,"SAM"));
        Assertions.assertNotNull(apiSecurityRepository.findByApiId(1L));
    }
}
