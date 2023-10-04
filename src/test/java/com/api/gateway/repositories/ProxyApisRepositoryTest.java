package com.api.gateway.repositories;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

@QuarkusTest
class ProxyApisRepositoryTest {

    @Inject
    ProxyApisRepository proxyApisRepository;

    @Test
    void test(){
        Assertions.assertNotNull(proxyApisRepository.findByApiId(1L));
    }
}
