package com.api.gateway.repositories;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

@QuarkusTest
class ProjectsRepositoryTest {

    @Inject
    ProjectsRepository projectsRepository;

    @Test
    void test(){
        Assertions.assertNotNull(projectsRepository.findByNameAndCompanyId("SAM",1L));
        Assertions.assertNotNull(projectsRepository.findByNameAndCompanyIdCount("SAM",1L));
        Assertions.assertNotNull(projectsRepository.findByAllParameterCount("SAM",1L,"SAM"));
        Assertions.assertNotNull(projectsRepository.findByCompanyId(1L));
        Assertions.assertNotNull(projectsRepository.findByCompanyIdAndProjectId(1L,1L));
    }
}
