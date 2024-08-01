package com.example.springsecurityjwt.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/resources")
@Tag(name = "Resources", description = "API for resource management")
public class ResourceController {
    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<String> getUserResource(Authentication authentication) {
        String userString = "User resource.";
        return ResponseEntity.ok(userString);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> getAdminResource(Authentication authentication) {
        String adminString = "Admin resource.";
        return ResponseEntity.ok(adminString);
    }
}

