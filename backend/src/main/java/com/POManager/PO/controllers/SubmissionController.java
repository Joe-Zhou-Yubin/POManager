package com.POManager.PO.controllers;

import com.POManager.PO.POservice.SubmissionService;
import com.POManager.PO.models.Submission;
import com.POManager.PO.repository.SubmissionRepository;
import com.POManager.PO.security.jwt.JwtUtils;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/submissions")
@CrossOrigin(origins = "*", maxAge = 3600)
public class SubmissionController {

    private final SubmissionService submissionService;
    private final JwtUtils jwtUtils;

    @Autowired
    public SubmissionController(SubmissionService submissionService, JwtUtils jwtUtils) {
        this.submissionService = submissionService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/createsub/{POref}")
    public ResponseEntity<Submission> createSubmission(@PathVariable String POref, @RequestBody Submission submission, HttpServletRequest request) {
        // Extract the JWT token from the Authorization header
        String jwtToken = request.getHeader("Authorization");
        if (jwtToken != null && jwtToken.startsWith("Bearer ")) {
            jwtToken = jwtToken.substring(7); // Remove the "Bearer " prefix
        }

        // Get the username from the JWT token
        String username = jwtUtils.getUserNameFromJwtToken(jwtToken);

        // Set the obtained username in the submission object
        submission.setUsername(username);
        submission.setPOref(POref);

        // Create the submission using the SubmissionService
        Submission createdSubmission = submissionService.createSubmission(submission);

        return ResponseEntity.ok(createdSubmission);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteSubmissionById(@PathVariable String id) {
        // Check if the submission exists in the database
        Submission submission = submissionService.findSubmissionById(id);
        if (submission == null) {
            return ResponseEntity.notFound().build();
        }

        // Delete the submission using the SubmissionService
        submissionService.deleteSubmissionById(id);

        return ResponseEntity.ok("Submission with ID " + id + " deleted successfully");
    }
    
    @GetMapping("/totalsubamount/{POref}")
    public ResponseEntity<Double> getTotalSubAmountForPOref(@PathVariable String POref) {
        double totalSubAmount = submissionService.getTotalSubAmountForPOref(POref);
        return ResponseEntity.ok(totalSubAmount);
    }
    
    @GetMapping("/get/{POref}")
    public ResponseEntity<List<Submission>> getSubmissionsForPOref(@PathVariable String POref) {
        List<Submission> submissions = submissionService.getSubmissionsForPOref(POref);
        return ResponseEntity.ok(submissions);
    }
    

}
