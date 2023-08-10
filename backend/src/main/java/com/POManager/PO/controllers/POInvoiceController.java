package com.POManager.PO.controllers;

import com.POManager.PO.POservice.POInvoiceService;
import com.POManager.PO.models.POInvoice;
import com.POManager.PO.repository.POInvoiceRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/invoices")
@CrossOrigin(origins = "*", maxAge = 3600)
public class POInvoiceController {

    private final POInvoiceRepository poInvoiceRepository;
    private final POInvoiceService poInvoiceService;
    
    @Autowired
    public POInvoiceController(POInvoiceRepository poInvoiceRepository, POInvoiceService poInvoiceService) {
        this.poInvoiceRepository = poInvoiceRepository;
        this.poInvoiceService = poInvoiceService;
        
    }

    @PostMapping("/createinvoice/{POref}")
    public POInvoice createInvoice(@PathVariable String POref, @RequestBody POInvoice poInvoice) {
        poInvoice.setPOref(POref);
        return poInvoiceRepository.save(poInvoice);
    }
    
    @GetMapping("/{POref}")
    public List<POInvoice> getInvoicesByPOref(@PathVariable String POref) {
        return poInvoiceRepository.findByPOref(POref);
    }
    
    @GetMapping("/get/{id}")
    public ResponseEntity<POInvoice> getInvoiceById(@PathVariable String id) {
        Optional<POInvoice> invoiceOptional = poInvoiceRepository.findById(id);
        if (invoiceOptional.isPresent()) {
            return ResponseEntity.ok(invoiceOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteInvoiceById(@PathVariable String id) {
        Optional<POInvoice> invoiceOptional = poInvoiceRepository.findById(id);
        if (invoiceOptional.isPresent()) {
            poInvoiceRepository.deleteById(id);
            return ResponseEntity.ok("Invoice with id " + id + " has been deleted.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/totalinvamount/{POref}")
    public ResponseEntity<Double> getTotalInvAmountForPOref(@PathVariable String POref) {
        double totalInvAmount = poInvoiceService.getTotalInvAmountForPOref(POref);
        return ResponseEntity.ok(totalInvAmount);
    }
    
    
    

}
