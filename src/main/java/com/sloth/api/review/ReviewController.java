package com.sloth.api.review;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "리뷰", description = "리뷰 API")
@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ApiReviewService apiReviewService;

    @Tag(name = "리뷰")
    @Operation(summary = "리뷰 저장")
    @PostMapping
    public ResponseEntity<ReviewSaveDto.Response> saveReview(@Validated @RequestBody ReviewSaveDto.Request requestDto) {
        ReviewSaveDto.Response responseDto = apiReviewService.saveReview(requestDto);
        return ResponseEntity.ok(responseDto);
    }


}
