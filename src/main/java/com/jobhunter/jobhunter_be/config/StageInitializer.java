package com.jobhunter.jobhunter_be.config;

import com.jobhunter.jobhunter_be.entity.Stage;
import com.jobhunter.jobhunter_be.repository.StageRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class StageInitializer {
    private final StageRepository stageRepository;

    private final List<String> defaultStages = List.of(
            "PENDING",
            "REVIEWED",
            "INTERVIEW",
            "OFFERED",
            "REJECTED"
    );

    @PostConstruct
    public void initRoles() {
        for (String progress : defaultStages) {
            boolean exists = stageRepository.existsByProgress(progress);
            if (!exists) {
                stageRepository.save(new Stage(0, progress));
                log.info("Created stage: {}", progress);
            } else {
                log.info("Stage already exists: {}", progress);
            }
        }
    }
}
