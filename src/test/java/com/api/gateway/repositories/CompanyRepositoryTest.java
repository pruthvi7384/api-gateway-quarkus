package com.api.gateway.repositories;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

@QuarkusTest
class CompanyRepositoryTest {
    @Inject
    CompaniesRepository companiesRepository;

    @Test
    void test(){
        Assertions.assertNotNull(companiesRepository.findByUsername("SAM"));
        Assertions.assertNotNull(companiesRepository.findByUsernameProcedure("SAM"));
        Assertions.assertNotNull(companiesRepository.findAllByProcedure());
    }
}
