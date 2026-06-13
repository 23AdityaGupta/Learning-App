package com.learningapp.backend.service;

import com.learningapp.backend.dto.FormRequest;
import com.learningapp.backend.model.ApplicationForm;
import com.learningapp.backend.model.User;
import com.learningapp.backend.repository.FormRepository;
import com.learningapp.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class FormService {

    @Autowired
    private FormRepository formRepository;

    @Autowired
    private UserRepository userRepository;

    // Username se User ka ID nikalo
    private Long getUserId(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getId();
    }

    // CREATE — Naya form submit karo
    public ApplicationForm createForm(FormRequest request, String username) {
        ApplicationForm form = new ApplicationForm();
        form.setFullName(request.getFullName());
        form.setEmail(request.getEmail());
        form.setPhone(request.getPhone());
        form.setAddress(request.getAddress());
        form.setPurpose(request.getPurpose());
        form.setSubmittedDate(LocalDate.now());
        form.setStatus("PENDING");
        form.setUserId(getUserId(username));

        return formRepository.save(form);
    }

    // READ — Logged-in user ke saare forms
    public List<ApplicationForm> getFormsByUser(String username) {
        Long userId = getUserId(username);
        return formRepository.findByUserId(userId);
    }

    // READ ONE — Single form (with ownership check)
    public ApplicationForm getFormById(Long id, String username) {
        ApplicationForm form = formRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Form not found"));

        // Check: ye form isi user ka hai?
        if (!form.getUserId().equals(getUserId(username))) {
            throw new RuntimeException("Access denied: This form does not belong to you");
        }

        return form;
    }

    // UPDATE — Form edit karo
    public ApplicationForm updateForm(Long id, FormRequest request, String username) {
        ApplicationForm form = getFormById(id, username); // ownership check bhi ho jayega

        form.setFullName(request.getFullName());
        form.setEmail(request.getEmail());
        form.setPhone(request.getPhone());
        form.setAddress(request.getAddress());
        form.setPurpose(request.getPurpose());

        return formRepository.save(form);
    }

    // DELETE — Form delete karo
    public void deleteForm(Long id, String username) {
        ApplicationForm form = getFormById(id, username); // ownership check bhi ho jayega
        formRepository.delete(form);
    }
}