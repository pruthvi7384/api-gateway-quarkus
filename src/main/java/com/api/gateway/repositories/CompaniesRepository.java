package com.api.gateway.repositories;

import com.api.gateway.domain.Companies;
import com.api.gateway.utils.RSAUtils;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

@ApplicationScoped
public class CompaniesRepository implements PanacheRepositoryBase<Companies, Long> {

    @Inject
    EntityManager em;
    @Inject
    RSAUtils rsaUtils;

    public Companies findByUsername(String username){
        return find("userName",username).firstResult();
    }

    /**
     * Get Store Procedure Call
     */
    public List<Companies> findAllByProcedure(){
       return em.createNamedStoredProcedureQuery("findAll").getResultList();
    }

    /**
     *  Search By Username Store Procedure call
     */
    public List<Companies> findByUsernameProcedure(String username){
       return em.createNamedStoredProcedureQuery("findByUsername").setParameter("name",username).getResultList();
    }
}
