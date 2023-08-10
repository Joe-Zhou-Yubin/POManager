package com.POManager.PO.repository;

import com.POManager.PO.models.POInvoice;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface POInvoiceRepository extends MongoRepository<POInvoice, String> {
	List<POInvoice> findByPOref(String POref);
	void deleteByInvRef(String InvRef);
}
