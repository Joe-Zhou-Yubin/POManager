package com.POManager.PO.models;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.Random;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Document (collection = "users")
public class User {
	@Id
	private String id;
	
	private String userId;
	
	@NotBlank
	@Size(max=20)
	private String username;
	
	@NotBlank
	@Size(max = 50)
	@Email
	private String email;
	
	@NotBlank
	@Size(max = 120)
	private String password;
	
	@DBRef
	private Set<Role> roles = new HashSet<>();

	public User() {
		
	}
	
	public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.userId = generateUnique8CharString();
    }
	
	// Generate a unique 8-character alphanumeric string for userId using UUID
    private String generateUnique8CharString() {
        UUID uuid = UUID.randomUUID();
        String uuidStr = uuid.toString().replace("-", "").substring(0, 8);
        return uuidStr;
    }
	
	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        // You can add some validation here if needed
        this.userId = userId;
    }

    // Generate an 8-character random alphanumeric string for userId
    public void generateUniqueUserId() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        // Append a UUID to ensure uniqueness
        String uuid = UUID.randomUUID().toString().replace("-", "");
        sb.append(uuid.substring(0, 8));

        // Append random characters to complete the 8-character length
        for (int i = 0; i < 8 - sb.length(); i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }

        this.userId = sb.toString();
    }

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
    
	public Set<Role> getRoles() {
	    return roles;
	}

	public void setRoles(Set<Role> roles) {
	    this.roles = roles;
	}
    
	
	
}
