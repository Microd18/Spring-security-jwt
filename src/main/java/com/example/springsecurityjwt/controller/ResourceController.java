package com.example.springsecurityjwt.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/resources")
public class ResourceController {
    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<String> getUserResource() {
        String userString = "Доступ для пользователей с ролью 'USER' или 'ADMIN'";
        return ResponseEntity.ok(userString);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> getAdminResource() {
        String adminString = "Доступ для пользователей с ролью 'ADMIN'";
        return ResponseEntity.ok(adminString);
    }
}

