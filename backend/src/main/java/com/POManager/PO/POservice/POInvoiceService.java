package com.POManager.PO.POservice;

import com.POManager.PO.models.POInvoice;
import com.POManager.PO.repository.POInvoiceRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class POInvoiceService {
    private final POInvoiceRepository poInvoiceRepository;

    @Autowired
    public POInvoiceService(POInvoiceRepository poInvoiceRepository) {
        this.poInvoiceRepository = poInvoiceRepository;
    }

    public POInvoice createPOInvoice(String POref, String InvRef, String InvName, String InvDetail, double InvAmount) {
        // Check if a POInvoice with the given POref already exists
        List<POInvoice> existingInvoice = poInvoiceRepository.findByPOref(POref);
        if (existingInvoice != null) {
            // You may choose to handle this case according to your requirements
            // For example, throw an exception or update the existing invoice
            // Here, we'll just return null to indicate that the operation failed.
            return null;
        }

        // If no existing POInvoice is found, create a new one
        POInvoice poInvoice = new POInvoice(POref, InvRef, InvName, InvDetail, InvAmount);
        return poInvoiceRepository.save(poInvoice);
    }
    
    public double getTotalInvAmountForPOref(String POref) {
        List<POInvoice> invoicesForPO = poInvoiceRepository.findByPOref(POref);
        double totalInvAmount = 0.0;

        for (POInvoice invoice : invoicesForPO) {
            totalInvAmount += invoice.getInvAmount();
        }

        return totalInvAmount;
    }


}
