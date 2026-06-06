package com.onlineassessment.repository;

import com.onlineassessment.dto.TestResponseDto;
import com.onlineassessment.entity.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestRepository extends JpaRepository<Test, Long> {


}
