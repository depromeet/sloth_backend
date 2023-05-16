package com.sloth.api.review.dto;

import com.sloth.domain.review.Review;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDate;

@Getter @NoArgsConstructor @AllArgsConstructor @Builder
public class ReviewDetailResponseDto {

    @ApiModelProperty(value = "리뷰 아이디", required = true)
    private Long reviewId;

    @ApiModelProperty(value = "리뷰 날짜", required = true)
    private LocalDate reviewDate;

    @ApiModelProperty(value = "리뷰 내용", required = true)
    private String reviewContent;

    public static ReviewDetailResponseDto of(Review review) {
        return ReviewDetailResponseDto.builder()
                .reviewId(review.getReviewId())
                .reviewDate(review.getReviewDate())
                .reviewContent(review.getReviewContent())
                .build();
    }

}
