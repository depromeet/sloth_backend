package com.sloth.api.lesson.controller;

import com.sloth.api.lesson.constant.LessonType;
import com.sloth.api.lesson.dto.*;
import com.sloth.api.lesson.service.LessonService;
import com.sloth.domain.lesson.Lesson;
import com.sloth.exception.InvalidParameterException;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@Transactional
@RequestMapping("/api/lesson")
@RequiredArgsConstructor
@Slf4j
public class LessonController {

    private final LessonService lessonService;

    @PatchMapping(value = "/number/plus", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "들은 강의 수 수정 API", description = "들은 강의 수 추가 api")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", defaultValue ="jwt access token", dataType = "string", value = "jwt access token", required = true, paramType = "header")
    })
    public ResponseEntity<LessonNumberDto.Response> plusPresentNumber(@Valid @RequestBody LessonNumberDto.Request request) {
        Lesson lesson = lessonService.plusPresentNumber(request.getLessonId(), request.getCount());
        LessonNumberDto.Response response = LessonNumberDto.Response.create(lesson);
        return ok(response);
    }

    @PatchMapping(value = "/number/minus", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "들은 강의 수 수정 API", description = "들은 강의 수 감소 api")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", defaultValue ="jwt access token", dataType = "string", value = "jwt access token", required = true, paramType = "header")
    })
    public ResponseEntity<LessonNumberDto.Response> minusPresentNumber(@Valid @RequestBody LessonNumberDto.Request request) {
        Lesson lesson = lessonService.minusPresentNumber(request.getLessonId(), request.getCount());
        LessonNumberDto.Response response = LessonNumberDto.Response.create(lesson);
        return ok(response);
    }

    @GetMapping(value = "/detail", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "강의 조회 API", description = "강의 상세 조회 api")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", defaultValue ="jwt access token", dataType = "string", value = "jwt access token", required = true, paramType = "header")
    })
    public ResponseEntity<LessonDetailDto.Response> getLessonDetail(@Valid @RequestBody LessonDetailDto.Request request) {
        Lesson lesson = lessonService.findLessonWithSiteCategory(request.getLessonId());
        LessonDetailDto.Response lessonDetail = LessonDetailDto.Response.create(lesson);
        return ok(lessonDetail);
    }

    @GetMapping("/doing")
    @Operation(summary = "멤버가 현재 진행중인 강의 조회 API", description = "멤버가 현재 진행중인 강의 조회 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", defaultValue ="jwt access token", dataType = "string", value = "jwt access token", required = true, paramType = "header")
    })
    public ResponseEntity<List<DoingLessonDto.Response>> getDoingLesson(@Valid @RequestBody DoingLessonDto.Request request) {
        List<Lesson> lessons = lessonService.getDoingLessons(request.getMemberId());
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
                                                                 @Valid @RequestBody LessonUpdateDto.Request lessonUpdateDto,
                                                                 BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            InvalidParameterException.throwErrorMessage(bindingResult);
        }

        Lesson lesson = lessonUpdateDto.toEntity(lessonId);

        // 강의 업데이트
        Lesson updatedLesson = lessonService.updateLesson(lessonUpdateDto.getMemberId(), lessonUpdateDto.getSiteId(),
                lessonUpdateDto.getCategoryId(), lesson);

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
    public ResponseEntity<List<LessonListDto.Response>> getLessonList(@RequestBody LessonListDto.Request request) {

        List<LessonListDto.Response> lessonListDto = new ArrayList<>();

        List<Lesson> lessons = lessonService.getLessons(request.getMemberId());
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
    public ResponseEntity<LessonCreateDto.Response> saveLesson(@RequestBody LessonCreateDto.Request requestDto) {
        Long lessonId = lessonService.saveLesson(requestDto);
        LessonCreateDto.Response response= LessonCreateDto.Response.builder()
                .lessonId(lessonId)
                .build();
        return ok(response);
    }

    @GetMapping("/render/{page-number}")
    @Operation(summary = "강의 등록 화면 렌더링 순서 API", description = "강의 등록 화면 렌더링 순서 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", defaultValue ="jwt access token", dataType = "string", value = "jwt access token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "page-number", defaultValue ="1", dataType = "string", value = "렌더링할 페이지 번호", required = true, paramType = "path")
    })
    public ResponseEntity<RenderOrderDto> viewRenderOrder(@PathVariable("page-number") int pageNumber) {

        if(pageNumber < 1) {
            throw new InvalidParameterException("강의 등록 화면은 1페이지부터 시작 입니다.");
        }

        RenderOrderDto renderOrderDto = lessonService.viewRenderOrder(pageNumber);
        return ResponseEntity.ok(renderOrderDto);
    }

}