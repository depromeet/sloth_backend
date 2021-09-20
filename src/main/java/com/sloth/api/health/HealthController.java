package com.sloth.api.health;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/")
public class HealthController {

    private final Environment env;

    @Operation(summary = "profile check api", description = "현재 실행중인 Profile 체크 api")
    @GetMapping(value = "/profile", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> profile() {

        List<String> profiles = Arrays.asList(env.getActiveProfiles());
        List<String> realProfiles = Arrays.asList("real1", "real2");

        profiles = profiles.stream().filter(profile -> "real1".equals(profile) || "real2".equals(profile))
                .collect(Collectors.toList());

        String defaultProfile = profiles.isEmpty()? "default" : profiles.get(0);

        String activeProfile = profiles.stream()
                .filter(realProfiles::contains)
                .findAny()
                .orElse(defaultProfile);

        return ResponseEntity.ok(activeProfile);
    }

    @Operation(summary = "health check api", description = "서버 health check용 API")
    @GetMapping(value = "/health", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Heal Check OK");
    }

}
