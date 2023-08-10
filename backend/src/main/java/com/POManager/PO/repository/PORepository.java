package com.POManager.PO.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.POManager.PO.models.PO;

@Repository
public interface PORepository extends MongoRepository<PO, String> {

    List<PO> findByPOvendor(String vendor);
    PO findByPOref(String POref);
    List<PO> findByPOstatusFalse();
    List<PO> findByPOstatusTrue();
    
}