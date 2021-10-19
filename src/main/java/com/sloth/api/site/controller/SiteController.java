package com.sloth.api.site.controller;

import com.sloth.api.site.dto.SiteNameDto;
import com.sloth.domain.lesson.Lesson;
import com.sloth.domain.site.Site;
import com.sloth.domain.site.repository.SiteRepository;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@Transactional
@RequestMapping("/api/site")
@RequiredArgsConstructor
@Slf4j
public class SiteController {

    private final SiteRepository siteRepository;

    @Operation(summary = "Get all Site api", description = "모든 사이트 조회 api")
    @GetMapping(value = "/list",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SiteNameDto>> getAllSite() {
        List<Site> allSites = siteRepository.findAll();
        List<SiteNameDto> siteNameDtos = new ArrayList<>();
        for (Site l : allSites) {
            siteNameDtos.add(SiteNameDto.builder().id(l.getId()).name(l.getName()).build()); // TODO modelMapper 로 바꾸기
        }
        return ResponseEntity.ok(siteNameDtos);
    }
}
