package com.sloth.api.health;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/")
public class HealthController {

    private final Environment env;

    @Operation(summary = "profile", description = "profile api")
    @GetMapping("/profile")
    public ResponseEntity<String> profile() {
        List<String> profiles = Arrays.asList(env.getActiveProfiles());
        List<String> realProfiles = Arrays.asList("real1", "real2");
        String defaultProfile = profiles.isEmpty()? "default" : profiles.get(0);

        String activeProfile = profiles.stream()
                .filter(realProfiles::contains)
                .findAny()
                .orElse(defaultProfile);

        return ResponseEntity.ok(activeProfile);
    }

    @Operation(summary = "health check", description = "health check api")
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Heal Check OK");
    }

}
