package com.POManager.PO.controllers;

import java.security.Key;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.POManager.PO.POservice.POInvoiceService;
import com.POManager.PO.POservice.POService;
import com.POManager.PO.POservice.SubmissionService;
import com.POManager.PO.models.PO;
import com.POManager.PO.security.jwt.JwtUtils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/po")
public class POController {
	private final POService poService;
	private final JwtUtils jwtUtils;
	private final POInvoiceService poInvoiceService;
	private final SubmissionService submissionService;
	
    public POController(POService poService, JwtUtils jwtUtils, POInvoiceService poInvoiceService, SubmissionService submissionService) {
        this.poService = poService;
        this.jwtUtils = jwtUtils;
        this.poInvoiceService = poInvoiceService;
        this.submissionService = submissionService;
    }

    @PostMapping("/createPO")
    @PreAuthorize("hasRole('ROLE_SALES') or hasRole('ROLE_MODERATOR') or hasRole('ROLE_FINANCE')")
    public ResponseEntity<PO> createPO(@RequestBody PO po, HttpServletRequest request) {
    	// Extract the JWT token from the Authorization header
        String jwtToken = request.getHeader("Authorization");
        if (jwtToken != null && jwtToken.startsWith("Bearer ")) {
            jwtToken = jwtToken.substring(7); // Remove the "Bearer " prefix
        }

        // Get the username from the JWT token
        String username = jwtUtils.getUserNameFromJwtToken(jwtToken);

        // Create the PO using the POService
        PO createdPO = poService.createPO(po, username);

        return ResponseEntity.ok(createdPO);
    }
    
    

    @GetMapping("/getPObyRef/{POref}")
    @PreAuthorize("hasRole('ROLE_SALES') or hasRole('ROLE_MODERATOR') or hasRole('ROLE_FINANCE')")
    public ResponseEntity<PO> getPOByRef(@PathVariable String POref) {
        PO po = poService.getPOByRef(POref);
        if (po != null) {
            return ResponseEntity.ok(po);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/deletePO/{POref}")
    @PreAuthorize("hasRole('ROLE_SALES') or hasRole('ROLE_MODERATOR') or hasRole('ROLE_FINANCE')")
    public ResponseEntity<Void> deletePO(@PathVariable String POref) {
        poService.deletePOByRef(POref);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/updatePO/{POref}")
    @PreAuthorize("hasRole('ROLE_SALES') or hasRole('ROLE_MODERATOR') or hasRole('ROLE_FINANCE')")
    public ResponseEntity<PO> updatePO(@PathVariable String POref, @RequestBody PO updatedPO) {
        PO po = poService.updatePO(POref, updatedPO);
        if (po != null) {
            return ResponseEntity.ok(po);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/getPOsWithStatusFalse")
    public ResponseEntity<List<PO>> getPOsWithStatusFalse() {
        List<PO> poList = poService.getPOsWithStatusFalse();
        if (!poList.isEmpty()) {
            return ResponseEntity.ok(poList);
        } else {
            return ResponseEntity.noContent().build();
        }
    }
    
    @GetMapping("/getPOsWithStatusTrue")
    public ResponseEntity<List<PO>> getPOsWithStatusTrue() {
        List<PO> poList = poService.getPOsWithStatusTrue();
        return ResponseEntity.ok(poList);
    }
    
    
    @PostMapping("/updatePOStatus/{POref}")
    public ResponseEntity<String> updatePOStatus(@PathVariable String POref) {
        // Get the PO by its reference
        PO po = poService.getPOByRef(POref);
        if (po == null) {
            return ResponseEntity.notFound().build();
        }
        
        // Update the PO status to true
        po.setPOstatus(true);
        poService.savePO(po); // Make sure you have a method to save the updated PO in your POService

        return ResponseEntity.ok("PO status updated to true.");
    }


    
    
}
