package com.sloth.api.lesson.controller;

import com.sloth.domain.member.service.MemberService;
import com.sloth.global.dto.ApiResult;
import com.sloth.api.lesson.dto.*;
import com.sloth.api.lesson.service.LessonService;
import com.sloth.domain.lesson.Lesson;
import com.sloth.domain.member.Member;
import com.sloth.global.exception.InvalidParameterException;
import com.sloth.global.resolver.CurrentEmail;
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
    private final MemberService memberService;

    @RequestMapping(value = "/number", method = {RequestMethod.PATCH, RequestMethod.PATCH}, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "들은 강의 수 수정 API", description = "들은 강의 수 수정 api")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", defaultValue ="jwt access token", dataType = "string", value = "jwt access token", required = true, paramType = "header")
    })
    public ResponseEntity<LessonNumberDto.Response> updateLessonmPresentNumber(@CurrentEmail String email, @Valid @RequestBody LessonNumberDto.Request request) {

        log.info("lesson number update api start");
        log.info("request : {}", request.toString());
        System.out.println(request.hashCode());
        System.out.println(email);
        Lesson lesson = lessonService.updatePresentNumber(email, request.getLessonId(), request.getCount());
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
    public ResponseEntity<LessonDetailDto.Response> getLessonDetail(@CurrentEmail String email, @Valid @PathVariable Long lessonId) {
        Lesson lesson = lessonService.findLessonWithSiteCategory(email, lessonId);
        LessonDetailDto.Response lessonDetail = LessonDetailDto.Response.create(lesson, LocalDate.now());
        return ok(lessonDetail);
    }

    @GetMapping("/doing")
    @Operation(summary = "투데이 강의 목록 조회 API", description = "투데이 강의 목록 조회 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", defaultValue ="jwt access token", dataType = "string", value = "jwt access token", required = true, paramType = "header")
    })
    public ResponseEntity<List<DoingLessonDto.Response>> getDoingLesson(@CurrentEmail String email) {

        List<Lesson> lessons = lessonService.getDoingLessons(email);
        if (lessons == null) {
            return ResponseEntity.notFound().build();
        }

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
                                                                 @CurrentEmail String email,
                                                                 @Valid @RequestBody LessonUpdateDto.Request lessonUpdateDto,
                                                                 BindingResult bindingResult) {

        log.info("lesson update api start");
        log.info("request : {}", lessonUpdateDto.toString());

        if(bindingResult.hasErrors()) {
            InvalidParameterException.throwErrorMessage(bindingResult);
        }

        // 강의 업데이트
        Lesson updatedLesson = lessonService.updateLesson(email, lessonUpdateDto, lessonId);

        // 반환 객체 생성
        LessonUpdateDto.Response responseLessonUpdateDto = LessonUpdateDto.Response.builder()
                .lessonId(updatedLesson.getLessonId())
                .lessonName(updatedLesson.getLessonName())
                .totalNumber(updatedLesson.getTotalNumber())
                .siteId(updatedLesson.getSite().getSiteId())
                .categoryId(updatedLesson.getCategory().getCategoryId())
                .price(updatedLesson.getPrice())
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
    public ResponseEntity<List<LessonListDto.Response>> getLessonList(@CurrentEmail String email) {

        log.info("lesson list api start");

        log.info("email : {}", email);

        List<LessonListDto.Response> lessonListDto = new ArrayList<>();

        List<Lesson> lessons = lessonService.getLessons(email);

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
    public ResponseEntity<LessonCreateDto.Response> saveLesson(@CurrentEmail String email, @RequestBody LessonCreateDto.Request requestDto) {
        Member member = memberService.findByEmail(email);
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
                                                                 @CurrentEmail String email) {

        log.info("lesson delete start");
        log.info("email : {}", email);
        log.info("delete lesson id : {}", lessonId);

        Member member = memberService.findByEmail(email);

        // 강의 삭제
        lessonService.deleteLesson(member, lessonId);

        ApiResult result = ApiResult.createOk();

        log.info("lesson delete end");
        return ok(result);
    }

    @PatchMapping("/{lessonId}/finish")
    @Operation(summary = "강의 완료 처리 API", description = "강의 수를 다 채웠을 때 마지막으로 완료하는 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", defaultValue ="jwt access token", dataType = "string", value = "jwt access token", required = true, paramType = "header")
    })
    public ResponseEntity<FinishedLessonDto> finishLesson(@PathVariable("lessonId") Long lessonId,
                                                                 @CurrentEmail String email) {

        log.info("finished lesson api start");

        // 강의 완료 확인
        Lesson lesson = lessonService.finishLesson(email, lessonId);

        FinishedLessonDto responseFinishedLessonDto = FinishedLessonDto.builder()
                .isFinished(lesson.getIsFinished())
                .build();

        log.info("response : {}", responseFinishedLessonDto.toString());
        log.info("finished lesson check api end");

        return ok(responseFinishedLessonDto);
    }

    @GetMapping("/stats")
    @Operation(summary = "강의 현황 통계 API", description = "강의 현황 통계 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", defaultValue ="jwt access token", dataType = "string", value = "jwt access token", required = true, paramType = "header")
    })
    public ResponseEntity<LessonStatsResponseDto> getFinishedLesson(@CurrentEmail String email) {

        log.info("lesson stats api start");

        log.info("email : {}", email);

        int expiredLessonsCnt = lessonService.getExpiredLessons(email);
        long expiredLessonsPrice = lessonService.getTotalPriceExpiredLessons(email);

        int finishedLessonsCnt = lessonService.getFinishedLessons(email);
        long finishedLessonsPrice = lessonService.getTotalPriceFinishedLessons(email);

        int notFinishedLessonsCnt = expiredLessonsCnt - finishedLessonsCnt;
        long notFinishedLessonsPrice = expiredLessonsPrice - finishedLessonsPrice;

        LessonStatsResponseDto response = LessonStatsResponseDto.builder()
                .expiredLessonsCnt(expiredLessonsCnt)
                .expiredLessonsPrice(expiredLessonsPrice)
                .finishedLessonsCnt(finishedLessonsCnt)
                .finishedLessonsPrice(finishedLessonsPrice)
                .notFinishedLessonsCnt(notFinishedLessonsCnt)
                .notFinishedLessonsPrice(notFinishedLessonsPrice)
                .build();

        log.info("response : {}", response.toString());
        log.info("lesson stats api end");

        return ok(response);
    }

}