package com.POM.POManager.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.support.Repositories;
import org.springframework.stereotype.Service;
import com.POM.POManager.repository.POrepo;
import com.POM.POManager.PoManagerApplication;
import com.POM.POManager.model.*;

public class POcontroller {
	
	private final POrepo repo;
	@Autowired
	public POcontroller(POrepo repo) {
		this.repo = repo;
	}
	

    // Example of CRUD operations:
    public POmodel savePOManager(POmodel poModel) {
    	return repo.save(poModel);
    }

    public POmodel getPOManagerById(long id) {
    	return repo.findById(id).orElse(null);
    }

    public List<POmodel> getAllPOManagers(){
    	return repo.findAll();
    }
    public void deletePOManager(long id) {
    	repo.deleteById(id);
    }
}
