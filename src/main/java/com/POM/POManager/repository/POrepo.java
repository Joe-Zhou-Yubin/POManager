package com.POM.POManager.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


import com.POM.POManager.model.POmodel;
public interface POrepo extends JpaRepository<POmodel, Long> {
//	List<POmodel> findByPOvendorContaining(String POvendor);
//	List<POmodel> findByPOorderContainingList (String POorder);
}
