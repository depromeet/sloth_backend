package com.sloth.api.lesson.controller;

import com.sloth.api.lesson.dto.LessonNumberRequest;
import com.sloth.api.lesson.dto.LessonNumberResponse;
import com.sloth.api.lesson.dto.LessonThisWeekResponse;
import com.sloth.api.site.dto.SiteNameDto;
import com.sloth.domain.lesson.Lesson;
import com.sloth.domain.lesson.repository.LessonRepository;
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
import java.util.ArrayList;
import java.util.List;

@RestController
@Transactional
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class LessonController {

    private final LessonRepository lessonRepository;
    private final ModelMapper modelMapper;

    @Operation(summary = "Plus lesson number api", description = "들은 강의 수 추가 api")
    @PostMapping(value = "/lesson/number/plus", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LessonNumberResponse> plusPresentNumber(@Valid @RequestBody LessonNumberRequest request) {
        Lesson lesson = lessonRepository.findById(request.getId()).orElseThrow(() -> new LessonNotFoundException("해당 강의는 존재하지 않습니다."));
        lesson.plusPresentNumber(request.getCount());
        LessonNumberResponse response = LessonNumberResponse.builder().id(lesson.getId()).presentNumber(lesson.getPresentNumber()).isFinished(lesson.getIsFinished()).build();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Minus lesson number api", description = "들은 강의 수 감소 api")
    @PostMapping(value = "/lesson/number/minus", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LessonNumberResponse> minusPresentNumber(@Valid @RequestBody LessonNumberRequest request) {
        Lesson lesson = lessonRepository.findById(request.getId()).orElseThrow(() -> new LessonNotFoundException("해당 강의는 존재하지 않습니다."));
        lesson.minusPresentNumber(request.getCount());
        LessonNumberResponse response = LessonNumberResponse.builder().id(lesson.getId()).presentNumber(lesson.getPresentNumber()).isFinished(lesson.getIsFinished()).build();
        return ResponseEntity.ok(response);
    }


}
