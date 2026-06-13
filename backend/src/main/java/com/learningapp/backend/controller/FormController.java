package com.learningapp.backend.controller;

import com.learningapp.backend.dto.FormRequest;
import com.learningapp.backend.model.ApplicationForm;
import com.learningapp.backend.service.FormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/forms")
public class FormController {

    @Autowired
    private FormService formService;

    // Logged-in user ka username nikalo (JwtFilter ne set kiya tha)
    private String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    // CREATE — POST /api/forms
    @PostMapping
    public ResponseEntity<?> createForm(@RequestBody FormRequest request) {
        try {
            ApplicationForm form = formService.createForm(request, getCurrentUsername());
            return ResponseEntity.ok(form);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // READ ALL — GET /api/forms
    @GetMapping
    public List<ApplicationForm> getAllForms() {
        return formService.getFormsByUser(getCurrentUsername());
    }

    // READ ONE — GET /api/forms/{id}
    @GetMapping("/{id}")
    public ResponseEntity<?> getFormById(@PathVariable Long id) {
        try {
            ApplicationForm form = formService.getFormById(id, getCurrentUsername());
            return ResponseEntity.ok(form);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // UPDATE — PUT /api/forms/{id}
    @PutMapping("/{id}")
    public ResponseEntity<?> updateForm(@PathVariable Long id, @RequestBody FormRequest request) {
        try {
            ApplicationForm form = formService.updateForm(id, request, getCurrentUsername());
            return ResponseEntity.ok(form);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // DELETE — DELETE /api/forms/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteForm(@PathVariable Long id) {
        try {
            formService.deleteForm(id, getCurrentUsername());
            return ResponseEntity.ok("Form deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}