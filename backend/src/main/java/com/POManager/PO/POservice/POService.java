package com.POManager.PO.POservice;
import java.util.List;

import org.springframework.stereotype.Service;
import com.POManager.PO.models.PO;
import com.POManager.PO.repository.PORepository;


@Service
public class POService {
    private final PORepository poRepository;

    public POService(PORepository poRepository) {
        this.poRepository = poRepository;
    }

    public PO createPO(PO po, String username) {
        // Set other fields or perform any additional operations as needed before saving
    	po.setUsername(username);
        return poRepository.save(po);
    }

    public PO getPOByRef(String POref) {
        return poRepository.findByPOref(POref);
    }

    public void deletePOByRef(String POref) {
        PO po = poRepository.findByPOref(POref);
        if (po != null) {
            poRepository.delete(po);
        }
    }

    public PO updatePO(String POref, PO updatedPO) {
        PO existingPO = poRepository.findByPOref(POref);
        if (existingPO != null) {
            // Update the fields you want to allow modification
            existingPO.setPOvendor(updatedPO.getPOvendor());
            // Set other fields as needed
            return poRepository.save(existingPO);
        }
        return null;
    }
    
    public List<PO> getPOsWithStatusFalse() {
        return poRepository.findByPOstatusFalse();
    }
    
    public List<PO> getPOsWithStatusTrue() {
        return poRepository.findByPOstatusTrue();
    }
    
    public PO savePO(PO po) {
        return poRepository.save(po);
    }
}