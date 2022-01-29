package com.sloth.api.lesson.controller;

import com.sloth.global.dto.ApiResult;
import com.sloth.api.lesson.dto.*;
import com.sloth.api.lesson.service.LessonService;
import com.sloth.domain.lesson.Lesson;
import com.sloth.domain.member.Member;
import com.sloth.global.exception.InvalidParameterException;
import com.sloth.global.resolver.CurrentMember;
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
import java.time.LocalDate;
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
    public ResponseEntity<LessonNumberDto.Response> updateLessonmPresentNumber(@CurrentMember Member member, @Valid @RequestBody LessonNumberDto.Request request) {

        log.info("lesson number update api start");
        log.info("request : {}", request.toString());

        Lesson lesson = lessonService.updatePresentNumber(member, request.getLessonId(), request.getCount());
        LessonNumberDto.Response response = LessonNumberDto.Response.create(lesson);

        log.info("response : {}", response.toString());
        log.info("lesson number update api end");

        return ok(response);
    }

    @GetMapping(value = "/detail/{lessonId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "강의 조회 API", description = "강의 상세 조회 api")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", defaultValue ="jwt access token", dataType = "string", value = "jwt access token", required = true, paramType = "header")
    })
    public ResponseEntity<LessonDetailDto.Response> getLessonDetail(@CurrentMember Member member, @Valid @PathVariable Long lessonId) {
        Lesson lesson = lessonService.findLessonWithSiteCategory(member, lessonId);
        LessonDetailDto.Response lessonDetail = LessonDetailDto.Response.create(lesson, LocalDate.now());
        return ok(lessonDetail);
    }

    @GetMapping("/doing")
    @Operation(summary = "투데이 강의 목록 조회 API", description = "투데이 강의 목록 조회 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", defaultValue ="jwt access token", dataType = "string", value = "jwt access token", required = true, paramType = "header")
    })
    public ResponseEntity<List<DoingLessonDto.Response>> getDoingLesson(@CurrentMember Member member) {
        List<Lesson> lessons = lessonService.getDoingLessons(member);
        if (lessons == null) {
            return ResponseEntity.notFound().build();
        }

        LocalDate now = LocalDate.now();

        List<DoingLessonDto.Response> doingLessonResponses = new ArrayList<>();
        lessonService.sortByRemainDay(lessons);

        for (Lesson lesson : lessons) {
            DoingLessonDto.Response doingLessonResponse = DoingLessonDto.Response.create(lesson, LocalDate.now());
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

        log.info("lesson update api start");
        log.info("request : {}", lessonUpdateDto.toString());

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

        log.info("response : {}", responseLessonUpdateDto.toString());
        log.info("lesson update api end");

        return ok(responseLessonUpdateDto);
    }

    @GetMapping("/list")
    @Operation(summary = "강의 목록 조회 API", description = "강의 목록 리스트 조회 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", defaultValue ="jwt access token", dataType = "string", value = "jwt access token", required = true, paramType = "header")
    })
    public ResponseEntity<List<LessonListDto.Response>> getLessonList(@CurrentMember Member member) {

        log.info("lesson list api start");

        log.info("member id : {}", member.getMemberId());

        List<LessonListDto.Response> lessonListDto = new ArrayList<>();

        List<Lesson> lessons = lessonService.getLessons(member);

        if (lessons == null) {
            return ResponseEntity.noContent().build();
        }

        for (Lesson lesson : lessons) {
            lessonListDto.add(LessonListDto.Response.create(lesson, LocalDate.now()));
        }

        lessonListDto.stream().forEach(lesson -> log.info("lessonDto : {}", lesson));


        log.info("lesson list api end");

        return ok(lessonListDto);
    }

    @PostMapping
    @Operation(summary = "강의 생성 API", description = "강의 생성 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", defaultValue ="jwt access token", dataType = "string", value = "jwt access token", required = true, paramType = "header")
    })
    public ResponseEntity<LessonCreateDto.Response> saveLesson(@CurrentMember Member member, @RequestBody LessonCreateDto.Request requestDto) {
        Lesson lesson = requestDto.toEntity(member);
        Long lessonId = lessonService.saveLesson(lesson, requestDto.getSiteId(), requestDto.getCategoryId());
        LessonCreateDto.Response response= LessonCreateDto.Response.builder()
                .lessonId(lessonId)
                .build();
        return ok(response);
    }

    @DeleteMapping("/{lessonId}")
    @Operation(summary = "강의 삭제 API", description = "강의 삭제 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", defaultValue ="jwt access token", dataType = "string", value = "jwt access token", required = true, paramType = "header")
    })
    public ResponseEntity<ApiResult> deleteLesson(@PathVariable("lessonId") Long lessonId,
                                                                 @CurrentMember Member member) {

        log.info("lesson delete start");
        log.info("member id : {}", member.getMemberId());
        log.info("delete lesson id : {}", lessonId);

        // 강의 업데이트
        lessonService.deleteLesson(member, lessonId);

        ApiResult result = ApiResult.createOk();

        log.info("lesson delete end");
        return ok(result);
    }

}