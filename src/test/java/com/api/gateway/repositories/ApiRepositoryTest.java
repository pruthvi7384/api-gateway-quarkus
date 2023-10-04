package com.api.gateway.repositories;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

@QuarkusTest
class ApiRepositoryTest {
    @Inject
    ApiRepository apiRepository;
    @Test
    void test(){
        Assertions.assertNotNull(apiRepository.findApiByNameAndProjectId("SAM",1L));
        Assertions.assertNotNull(apiRepository.findApiByProjectIdAndApiId(1L,1L));
        Assertions.assertNotNull(apiRepository.findByProjectId(1L));
    }
}
