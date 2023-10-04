package com.api.gateway.repositories;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

@QuarkusTest
class LogsRepositoryTest {
    @Inject
    LogsRepository logsRepository;

    @Test
    void test(){
        Assertions.assertNotNull(logsRepository.findByApis(1L));
    }
}
