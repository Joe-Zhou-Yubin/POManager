package com.POManager.PO.controllers;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;


import com.POManager.PO.models.ERole;
import com.POManager.PO.models.Role;
import com.POManager.PO.models.User;
import com.POManager.PO.payload.request.LoginRequest;
import com.POManager.PO.payload.request.SignupRequest;
import com.POManager.PO.payload.response.JwtResponse;
import com.POManager.PO.payload.response.MessageResponse;
import com.POManager.PO.repository.RoleRepository;
import com.POManager.PO.repository.UserRepository;
import com.POManager.PO.security.jwt.JwtUtils;
import com.POManager.PO.security.services.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class UserController {
	@Autowired
	  AuthenticationManager authenticationManager;

	  @Autowired
	  UserRepository userRepository;

	  @Autowired
	  RoleRepository roleRepository;

	  @Autowired
	  PasswordEncoder encoder;

	  @Autowired
	  JwtUtils jwtUtils;
	  
	  @PostMapping("/signin")
	  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

	    Authentication authentication = authenticationManager.authenticate(
	        new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

	    SecurityContextHolder.getContext().setAuthentication(authentication);
	    String jwt = jwtUtils.generateJwtToken(authentication);
	    
	    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();    
	    List<String> roles = userDetails.getAuthorities().stream()
	        .map(item -> item.getAuthority())
	        .collect(Collectors.toList());

	    return ResponseEntity.ok(new JwtResponse(jwt, 
	                         userDetails.getId(), 
	                         userDetails.getUsername(), 
	                         userDetails.getEmail(), 
	                         roles));
	  }
	  
	  @PostMapping("/signup")
	  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
	    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
	      return ResponseEntity
	          .badRequest()
	          .body(new MessageResponse("Error: Username is already taken!"));
	    }

	    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
	      return ResponseEntity
	          .badRequest()
	          .body(new MessageResponse("Error: Email is already in use!"));
	    }

	    // Create new user's account
	    User user = new User(signUpRequest.getUsername(), 
	               signUpRequest.getEmail(),
	               encoder.encode(signUpRequest.getPassword()));

	    Set<String> strRoles = signUpRequest.getRoles();
	    Set<Role> roles = new HashSet<>();

	    if (strRoles == null) {
	      Role userRole = roleRepository.findByName(ERole.ROLE_SALES)
	          .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
	      roles.add(userRole);
	    } else {
	      strRoles.forEach(role -> {
	        switch (role) {
	        case "finance":
	          Role adminRole = roleRepository.findByName(ERole.ROLE_FINANCE)
	              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
	          roles.add(adminRole);

	          break;
	        case "mod":
	          Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
	              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
	          roles.add(modRole);

	          break;
	        default:
	          Role userRole = roleRepository.findByName(ERole.ROLE_SALES)
	              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
	          roles.add(userRole);
	        }
	      });
	    }

	    user.setRoles(roles);
	    userRepository.save(user);

	    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	  }
	  
	  @GetMapping("/getallaccounts")
	  public ResponseEntity<List<User>> getAllAccounts() {
	        List<User> users = userRepository.findAll();
	        return ResponseEntity.ok(users);
	  }
	  
	  @GetMapping("/getaccount/{userId}")
	  public ResponseEntity<User> getAccountDetails(@PathVariable String userId) {
	      User user = userRepository.findByUserId(userId).orElse(null);
	      if (user != null) {
	          return ResponseEntity.ok(user);
	      } else {
	          return ResponseEntity.notFound().build();
	      }
	  }
	  @DeleteMapping("/deleteaccount/{userId}")
	    public ResponseEntity<String> deleteAccount(@PathVariable String userId) {
	        // Check if the user exists
	        if (!userRepository.existsByUserId(userId)) {
	            return ResponseEntity.notFound().build();
	        }

	        // Delete the user from the database
	        userRepository.deleteByUserId(userId);

	        return ResponseEntity.ok("Account deleted successfully.");
	   }
	  
	  @PostMapping("/logout")
	    public ResponseEntity<String> logout(HttpServletRequest request) {
	        HttpSession session = request.getSession(false);
	        if (session != null) {
	            session.invalidate(); // Invalidate the session to logout the user
	        }

	        return ResponseEntity.ok("Logged out successfully.");
	    }
	  
}
