package com.onlineassessment.repository;

import com.onlineassessment.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    boolean existsByTitle(String title);
    Optional<Question> findById(long id);
}
