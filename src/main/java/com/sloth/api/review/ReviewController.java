package com.sloth.api.review;

import com.sloth.api.review.dto.ReviewDetailResponseDto;
import com.sloth.api.review.dto.ReviewRequestDto;
import com.sloth.api.review.dto.ReviewSaveDto;
import com.sloth.api.review.dto.ReviewUpdateDto;
import com.sloth.global.resolver.CurrentEmail;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@Tag(name = "리뷰", description = "리뷰 API")
@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ApiReviewService apiReviewService;

    @Tag(name = "리뷰")
    @Operation(summary = "리뷰 저장")
    @PostMapping
    public ResponseEntity<ReviewSaveDto.Response> saveReview(@Validated @RequestBody ReviewSaveDto.Request requestDto,
                                                             @CurrentEmail String email) {
        ReviewSaveDto.Response responseDto = apiReviewService.saveReview(requestDto, email);
        return ResponseEntity.ok(responseDto);
    }

    @Tag(name = "리뷰")
    @Operation(summary = "리뷰 수정")
    @PatchMapping("/{reviewId}")
    public ResponseEntity<String> updateReview(@PathVariable Long reviewId, @Validated @RequestBody ReviewUpdateDto.Request requestDto,
                                                             @CurrentEmail String email) {
        apiReviewService.updateReview(reviewId, requestDto, email);
        return ResponseEntity.ok("success");
    }

    @Tag(name = "리뷰")
    @Operation(summary = "리뷰 리스트 조회")
    @GetMapping
    public ResponseEntity<List<ReviewRequestDto.Response>> findReviews(@Validated @ModelAttribute ReviewRequestDto.Request requestDto,
                                                                       @CurrentEmail String email) {
        List<ReviewRequestDto.Response> responseDtos = apiReviewService.findReviews(requestDto, email);
        return ResponseEntity.ok(responseDtos);
    }

    @Tag(name = "리뷰")
    @Operation(summary = "리뷰 상세 조회")
    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewDetailResponseDto> findReview(@PathVariable("reviewId") Long reviewId,
                                                                       @CurrentEmail String email) {
        ReviewDetailResponseDto responseDto = apiReviewService.findReview(reviewId, email);
        return ResponseEntity.ok(responseDto);
    }


}
