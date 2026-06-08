package com.onlineassessment.controller;

import com.onlineassessment.dto.OverviewResponseDto;
import com.onlineassessment.service.OverviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/overview")
public class OverviewController {

    private final OverviewService overviewService;

    public OverviewController(OverviewService overviewService) {
        this.overviewService = overviewService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<OverviewResponseDto> getOverview(
            @PathVariable long userId
    ) {
        OverviewResponseDto overview = overviewService.getOverview(userId);
        return ResponseEntity.ok(overview);
    }
}