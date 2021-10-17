package com.sloth.api.lesson.controller;

import com.sloth.api.lesson.dto.*;
import com.sloth.domain.lesson.Lesson;
import com.sloth.api.lesson.service.LessonService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@Transactional
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class LessonController {

    private final ModelMapper modelMapper;
    private final LessonService lessonService;

    @Operation(summary = "Plus lesson number api", description = "들은 강의 수 추가 api")
    @PatchMapping(value = "/lesson/number/plus", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LessonNumberResponse> plusPresentNumber(@Valid @RequestBody LessonNumberRequest request) {
        Lesson lesson =lessonService.plusPresentNumber(request.getId(), request.getCount());
        LessonNumberResponse response = LessonNumberResponse.create(lesson);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Minus lesson number api", description = "들은 강의 수 감소 api")
    @PatchMapping(value = "/lesson/number/minus", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LessonNumberResponse> minusPresentNumber(@Valid @RequestBody LessonNumberRequest request) {
        Lesson lesson = lessonService.minusPresentNumber(request.getId(), request.getCount());
        LessonNumberResponse response = LessonNumberResponse.create(lesson);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get lesson detail", description = "강의 상세 조회 api")
    @GetMapping(value = "/lesson/detail", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LessonDetailResponse> getLessonDetail(@Valid @RequestBody LessonDetailRequest request) {
        Lesson lesson = lessonService.findLessonWithSiteCategory(request.getId());
        LessonDetailResponse lessonDetail = LessonDetailResponse.create(lesson);
        return ResponseEntity.ok(lessonDetail);
    }

    @GetMapping("/lesson/doing")
    public ResponseEntity<List<DoingLessonResponse>> getDoingLesson(@Valid @RequestBody DoingLessonRequest request) {
        List<Lesson> lessons = lessonService.getDoingLessons(request.getMemberId());
        List<DoingLessonResponse> doingLessonResponses = new ArrayList<>();
        for (Lesson lesson : lessons) {
            DoingLessonResponse doingLessonResponse = DoingLessonResponse.create(lesson);
            doingLessonResponses.add(doingLessonResponse);
        }
        return ResponseEntity.ok(doingLessonResponses);
    }

}
