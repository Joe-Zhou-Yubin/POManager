package com.POManager.PO.models;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "POInvoices")
public class POInvoice {
    @Id
    private String id;
    
    private String POref;
    
    @Field("InvRef")
    private String InvRef;

    @Field("InvName")
    private String InvName;

    @Field("InvDetail")
    private String InvDetail;

    @Field("InvAmount")
    private double InvAmount;

    
    public POInvoice() {
        this.InvRef = generate8DigitUUID();
    }
    
    public POInvoice(String POref, String InvRef, String InvName, String InvDetail, double InvAmount) {
        super();
        this.POref = POref;
        this.InvRef = InvRef;
        this.InvName = InvName;
        this.InvDetail = InvDetail;
        this.InvAmount = InvAmount;
    }

    private static String generate8DigitUUID() {
        UUID uuid = UUID.randomUUID();
        long mostSignificantBits = uuid.getMostSignificantBits();
        long leastSignificantBits = uuid.getLeastSignificantBits();

        long combinedBits = mostSignificantBits ^ leastSignificantBits;
        // Convert the combinedBits to a positive number by using the bitwise AND operation
        long positiveBits = combinedBits & Long.MAX_VALUE;

        // Convert the positiveBits to a String and take the last 8 characters
        String uniqueReference = String.valueOf(positiveBits).substring(0, 8);

        return uniqueReference;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPOref() {
        return POref;
    }

    public void setPOref(String POref) {
        this.POref = POref;
    }

    public String getInvRef() {
        return InvRef;
    }

    public void setInvRef(String invRef) {
        InvRef = invRef;
    }

    public String getInvName() {
        return InvName;
    }

    public void setInvName(String invName) {
        InvName = invName;
    }

    public String getInvDetail() {
        return InvDetail;
    }

    public void setInvDetail(String invDetail) {
        InvDetail = invDetail;
    }

    public double getInvAmount() {
        return InvAmount;
    }

    public void setInvAmount(double invAmount) {
        InvAmount = invAmount;
    }
}
