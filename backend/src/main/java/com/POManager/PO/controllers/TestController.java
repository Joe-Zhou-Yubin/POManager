package com.POManager.PO.controllers;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {
  @GetMapping("/all")
  public String allAccess() {
    return "Public Content.";
  }
  
  @GetMapping("/sales")
  @PreAuthorize("hasRole('SALES') or hasRole('MODERATOR') or hasRole('FINANCE')")
  public String salesAccess() {
    return "Sales Content.";
  }

  @GetMapping("/mod")
  @PreAuthorize("hasRole('MODERATOR')")
  public String moderatorAccess() {
    return "Moderator Board.";
  }

  @GetMapping("/finance")
  @PreAuthorize("hasRole('FINANCE')")
  public String financeAccess() {
    return "Finance Board.";
  }
  
}
