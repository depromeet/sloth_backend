package com.sloth.api.site.controller;

import com.sloth.api.dto.ApiResult;
import com.sloth.api.site.dto.SiteNameDto;
import com.sloth.domain.site.Site;
import com.sloth.domain.site.repository.SiteRepository;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
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

    @Cacheable("siteNameDtos")
    @GetMapping(value = "/list",produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get all Site api", description = "모든 사이트 조회 api")
    public ResponseEntity<List<SiteNameDto>> getAllSite() {
        log.info("getAllSite start");

        List<Site> allSites = siteRepository.findAll();
        List<SiteNameDto> siteNameDtos = new ArrayList<>();
        for (Site l : allSites) {
            siteNameDtos.add(SiteNameDto.builder().siteId(l.getSiteId()).siteName(l.getSiteName()).build()); // TODO modelMapper 로 바꾸기
        }

        siteNameDtos.stream().forEach(site -> log.info("site id : {}, site name : {}", site.getSiteId(), site.getSiteName()));
        log.info("getAllSite end");
        return ResponseEntity.ok(siteNameDtos);
    }

    @CacheEvict("siteNameDtos")
    @DeleteMapping(value = "/cache",produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "site list cache clear", description = "사이트 리스트 캐시 삭제 api")
    public ResponseEntity<ApiResult> clearSiteCache( ) {
        return ResponseEntity.ok(ApiResult.createOk("site list cache clear success"));
    }

}
