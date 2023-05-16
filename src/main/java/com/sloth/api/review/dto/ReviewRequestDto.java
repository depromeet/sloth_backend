package com.sloth.api.review.dto;

import com.sloth.domain.review.Review;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class ReviewRequestDto {

    @ApiModel(value = "리뷰 조회 요청 객체")
    @Getter @Setter
    public static class Request {

        @NotNull(message = "리뷰 조회 시작일을 입력해주세요")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        @ApiModelProperty(value = "리뷰 조회 시작일", required = true)
        private LocalDate searchStartDate;

        @NotNull(message = "리뷰 조회 시작일을 입력해주세요")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        @ApiModelProperty(value = "리뷰 조회 시작일", required = true)
        private LocalDate searchEndDate;

    }

    @ApiModel(value = "리뷰 조회 반환 객체")
    @AllArgsConstructor @NoArgsConstructor
    @Getter @Builder
    public static class Response {

        @ApiModelProperty(value = "리뷰 아이디", required = true)
        private Long reviewId;

        @ApiModelProperty(value = "리뷰 날짜", required = true)
        private LocalDate reviewDate;

        public static ReviewRequestDto.Response of(Review review) {
            return Response.builder()
                    .reviewId(review.getReviewId())
                    .reviewDate(review.getReviewDate())
                    .build();
        }

    }

}