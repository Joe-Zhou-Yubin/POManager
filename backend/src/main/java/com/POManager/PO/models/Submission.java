package com.POManager.PO.models;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "submissions")
public class Submission {
    @Id
    private String id;
    
    private String POref;
    private String username;
    private double subamount;

    public Submission() {
    }

    public Submission(String POref, String username, double subamount) {
        this.POref = POref;
        this.username = username;
        this.subamount = subamount;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getSubamount() {
        return subamount;
    }

    public void setSubamount(double subamount) {
        this.subamount = subamount;
    }
}
