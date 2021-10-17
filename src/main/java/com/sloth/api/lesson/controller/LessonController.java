package com.sloth.api.lesson.controller;

import com.sloth.api.lesson.dto.LessonDetailRequest;
import com.sloth.api.lesson.dto.LessonDetailResponse;
import com.sloth.api.lesson.dto.LessonNumberRequest;
import com.sloth.api.lesson.dto.LessonNumberResponse;
import com.sloth.api.site.dto.SiteNameDto;
import com.sloth.domain.lesson.Lesson;
import com.sloth.domain.lesson.repository.LessonRepository;
import com.sloth.domain.lesson.service.LessonService;
import com.sloth.exception.LessonNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Transactional
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class LessonController {

    private final ModelMapper modelMapper;
    private final LessonService lessonService;

    @Operation(summary = "Plus lesson number api", description = "들은 강의 수 추가 api")
    @PostMapping(value = "/lesson/number/plus", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LessonNumberResponse> plusPresentNumber(@Valid @RequestBody LessonNumberRequest request) {
        Lesson lesson = lessonService.findLesson(request.getId());
        lesson.plusPresentNumber(request.getCount());
        LessonNumberResponse response = LessonNumberResponse.createLessonNumberResponse(lesson);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Minus lesson number api", description = "들은 강의 수 감소 api")
    @PostMapping(value = "/lesson/number/minus", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LessonNumberResponse> minusPresentNumber(@Valid @RequestBody LessonNumberRequest request) {
        Lesson lesson = lessonService.findLesson(request.getId());
        lesson.minusPresentNumber(request.getCount());
        LessonNumberResponse response = LessonNumberResponse.createLessonNumberResponse(lesson);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get lesson detail", description = "강의 상세 조회 api")
    @GetMapping(value = "/lesson/detail", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LessonDetailResponse> getLessonDetail(@Valid @RequestBody LessonDetailRequest request) {
        Lesson lesson = lessonService.findLessonWithSiteCategory(request.getId());
        LessonDetailResponse lessonDetail = LessonDetailResponse.createLessonDetail(lesson);
        return ResponseEntity.ok(lessonDetail);
    }


}
