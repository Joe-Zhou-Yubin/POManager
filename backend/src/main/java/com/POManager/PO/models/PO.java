package com.POManager.PO.models;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Document (collection = "PO")
public class PO {
	@Id
	private String id;
	
	private String POnumber;
	private String POref;//8digit string
	
	@NotBlank
	@Size(max=20)
	private String POvendor;
	
	private String username;
	private boolean POstatus;
	
	@CreatedDate
    private LocalDateTime createdDate;

    public PO() {
        // Set the current date and time when the PO object is created
        this.createdDate = LocalDateTime.now();
        this.POstatus = false;
        this.POnumber = generateUnique8CharString();
        this.POref = generateUnique8DigitString();
    }
    
    

    public PO(String id, String pOnumber, String pOref, @NotBlank @Size(max = 20) String pOvendor, String username,
			boolean pOstatus, LocalDateTime createdDate) {
		super();
		this.id = id;
		POnumber = pOnumber;
		POref = pOref;
		POvendor = pOvendor;
		this.username = username;
		POstatus = pOstatus;
		this.createdDate = createdDate;
	}



	public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
    
 // Generate a unique 8-character alphanumeric string for POnumber using UUID
    private String generateUnique8CharString() {
        UUID uuid = UUID.randomUUID();
        String uuidStr = uuid.toString().replace("-", "").substring(0, 8);
        return uuidStr;
    }

    // Generate a unique 8-digit string for POref using UUID
    private String generateUnique8DigitString() {
        UUID uuid = UUID.randomUUID();
        long uuidValue = uuid.getMostSignificantBits() & Long.MAX_VALUE;
        return String.format("%08d", uuidValue % 100000000);
    }



	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public String getPOnumber() {
		return POnumber;
	}



	public void setPOnumber(String pOnumber) {
		POnumber = pOnumber;
	}



	public String getPOref() {
		return POref;
	}



	public void setPOref(String pOref) {
		POref = pOref;
	}



	public String getPOvendor() {
		return POvendor;
	}



	public void setPOvendor(String pOvendor) {
		POvendor = pOvendor;
	}



	public String getUsername() {
		return username;
	}



	public void setUsername(String username) {
		this.username = username;
	}



	public boolean isPOstatus() {
		return POstatus;
	}



	public void setPOstatus(boolean pOstatus) {
		POstatus = pOstatus;
	}
    
    
	
    

	
}
