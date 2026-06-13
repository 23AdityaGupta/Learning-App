package com.learningapp.backend.repository;

import com.learningapp.backend.model.ApplicationForm;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FormRepository extends JpaRepository<ApplicationForm, Long> {

    List<ApplicationForm> findByUserId(Long userId);
}