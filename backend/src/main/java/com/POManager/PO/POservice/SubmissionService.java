package com.POManager.PO.POservice;

import com.POManager.PO.models.Submission;
import com.POManager.PO.repository.SubmissionRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubmissionService {

    private final SubmissionRepository submissionRepository;

    @Autowired
    public SubmissionService(SubmissionRepository submissionRepository) {
        this.submissionRepository = submissionRepository;
    }

    public Submission createSubmission(Submission submission) {
        // Add any business logic or validation here before saving the submission
        // For example, you may want to check if the POref exists or if the subamount is valid

        // Save the submission to the database
        return submissionRepository.save(submission);
    }
    
    public Submission findSubmissionById(String id) {
        return submissionRepository.findById(id).orElse(null);
    }
    
    public void deleteSubmissionById(String id) {
        // Delete the submission from the database using the submission ID
        submissionRepository.deleteById(id);
    }
    
    public double getTotalSubAmountForPOref(String POref) {
        double totalSubAmount = 0.0;

        // Retrieve submissions with the specified POref
        Iterable<Submission> submissions = submissionRepository.findByPOref(POref);

        // Calculate the total subamount
        for (Submission submission : submissions) {
            totalSubAmount += submission.getSubamount();
        }

        return totalSubAmount;
    }
    
    public List<Submission> getSubmissionsForPOref(String POref) {
        return submissionRepository.findByPOref(POref);
    }


}

