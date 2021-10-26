package com.sloth.api.lesson.controller;

import com.sloth.api.lesson.dto.*;
import com.sloth.api.lesson.service.LessonService;
import com.sloth.domain.lesson.Lesson;
import com.sloth.domain.member.Member;
import com.sloth.exception.InvalidParameterException;
import com.sloth.resolver.CurrentMember;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@Transactional
@RequestMapping("/api/lesson")
@RequiredArgsConstructor
public class LessonController {

    private final LessonService lessonService;

    @PatchMapping(value = "/number", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "들은 강의 수 수정 API", description = "들은 강의 수 수정 api")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", defaultValue ="jwt access token", dataType = "string", value = "jwt access token", required = true, paramType = "header")
    })
    public ResponseEntity<LessonNumberDto.Response> plusPresentNumber(@CurrentMember Member member, @Valid @RequestBody LessonNumberDto.Request request) {
        Lesson lesson = lessonService.updatePresentNumber(member, request.getLessonId(), request.getCount());
        LessonNumberDto.Response response = LessonNumberDto.Response.create(lesson);
        return ok(response);
    }

    @GetMapping(value = "/detail", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "강의 조회 API", description = "강의 상세 조회 api")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", defaultValue ="jwt access token", dataType = "string", value = "jwt access token", required = true, paramType = "header")
    })
    public ResponseEntity<LessonDetailDto.Response> getLessonDetail(@CurrentMember Member member, @Valid @RequestBody LessonDetailDto.Request request) {
        Lesson lesson = lessonService.findLessonWithSiteCategory(member, request.getLessonId());
        LessonDetailDto.Response lessonDetail = LessonDetailDto.Response.create(lesson);
        return ok(lessonDetail);
    }

    @GetMapping("/doing")
    @Operation(summary = "멤버가 현재 진행중인 강의 조회 API", description = "멤버가 현재 진행중인 강의 조회 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", defaultValue ="jwt access token", dataType = "string", value = "jwt access token", required = true, paramType = "header")
    })
    public ResponseEntity<List<DoingLessonDto.Response>> getDoingLesson(@CurrentMember Member member) {
        List<Lesson> lessons = lessonService.getDoingLessons(member);
        if (lessons == null) {
            return ResponseEntity.notFound().build();
        }
        List<DoingLessonDto.Response> doingLessonResponses = new ArrayList<>();
        for (Lesson lesson : lessons) {
            DoingLessonDto.Response doingLessonResponse = DoingLessonDto.Response.create(lesson);
            doingLessonResponses.add(doingLessonResponse);
        }
        return ok(doingLessonResponses);
    }


    @PatchMapping("/{lessonId}")
    @Operation(summary = "강의 수정 API", description = "강의 상세 내용 수정 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", defaultValue ="jwt access token", dataType = "string", value = "jwt access token", required = true, paramType = "header")
    })
    public ResponseEntity<LessonUpdateDto.Response> updateLesson(@PathVariable("lessonId") Long lessonId,
                                                                 @CurrentMember Member member,
                                                                 @Valid @RequestBody LessonUpdateDto.Request lessonUpdateDto,
                                                                 BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            InvalidParameterException.throwErrorMessage(bindingResult);
        }

        // 강의 업데이트
        Lesson updatedLesson = lessonService.updateLesson(member, lessonUpdateDto, lessonId);

        // 반환 객체 생성
        LessonUpdateDto.Response responseLessonUpdateDto = LessonUpdateDto.Response.builder()
                .lessonId(updatedLesson.getLessonId())
                .lessonName(updatedLesson.getLessonName())
                .totalNumber(updatedLesson.getTotalNumber())
                .siteId(updatedLesson.getSite().getSiteId())
                .categoryId(updatedLesson.getCategory().getCategoryId())
                .build();

        return ok(responseLessonUpdateDto);
    }

    @GetMapping("/list")
    @Operation(summary = "강의 목록 조회 API", description = "강의 목록 리스트 조회 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", defaultValue ="jwt access token", dataType = "string", value = "jwt access token", required = true, paramType = "header")
    })
    public ResponseEntity<List<LessonListDto.Response>> getLessonList(@CurrentMember Member member) {

        List<LessonListDto.Response> lessonListDto = new ArrayList<>();

        List<Lesson> lessons = lessonService.getLessons(member);

        if (lessons == null) {
            return ResponseEntity.noContent().build();
        }
        for (Lesson lesson : lessons) {
            lessonListDto.add(LessonListDto.Response.create(lesson));
        }

        return ok(lessonListDto);
    }

    @PostMapping
    @Operation(summary = "강의 생성 API", description = "강의 생성 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", defaultValue ="jwt access token", dataType = "string", value = "jwt access token", required = true, paramType = "header")
    })
    public ResponseEntity<LessonCreateDto.Response> saveLesson(@CurrentMember Member member, @RequestBody LessonCreateDto.Request requestDto) {
        Long lessonId = lessonService.saveLesson(member, requestDto);
        LessonCreateDto.Response response= LessonCreateDto.Response.builder()
                .lessonId(lessonId)
                .build();
        return ok(response);
    }
}