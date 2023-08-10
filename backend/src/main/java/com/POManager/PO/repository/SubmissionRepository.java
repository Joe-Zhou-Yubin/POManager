package com.POManager.PO.repository;

import com.POManager.PO.models.Submission;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface SubmissionRepository extends MongoRepository<Submission, String> {
	List<Submission> findByPOref(String POref);

}
